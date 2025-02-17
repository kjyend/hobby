package com.hobbyproject.service.postlike;

import com.hobbyproject.dto.notification.NotificationMessage;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.PostLike;
import com.hobbyproject.pubsub.RedisPublisher;
import com.hobbyproject.repository.member.MemberRepository;
import com.hobbyproject.repository.post.PostRepository;
import com.hobbyproject.repository.postlike.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisPublisher redisPublisher;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private static final String LIKE_KEY = "post:likes:";
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void likePost(Long postId, String memberName) {
        redisTemplate.opsForSet().add(LIKE_KEY + postId, memberName);
        String postOwner = getPostOwner(postId);
        String name = getMemberName(memberName);
        redisPublisher.publish("notifications",new NotificationMessage(postOwner,"유저 "+name+"가 당신의 post에 좋아요를 눌렀습니다."));
    }

    private String getMemberName(String memberName) {
        return memberRepository.findMemberName(memberName);
    }

    private String getPostOwner(Long postId) {
        return postRepository.findPostOwnerLoginId(postId);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, String memberName) {
        redisTemplate.opsForSet().remove(LIKE_KEY + postId, memberName);
    }

    @Override
    public boolean isUserLikedPost(Long postId, String memberName) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(LIKE_KEY + postId, memberName));
    }

    @Override
    public Long getLikeCount(Long postId) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(LIKE_KEY + postId))) {
            return 0L;
        }
        return Optional.ofNullable(redisTemplate.opsForSet().size(LIKE_KEY + postId)).orElse(0L);
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Async
    public void syncLikesToDB() {
        Set<String> keys = redisTemplate.execute((RedisCallback<? extends Set<String>>) connection -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("post:likes:*").count(1000).build());
            Set<String> keySet = new HashSet<>();
            while (cursor.hasNext()) {
                keySet.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
            return keySet;
        });

        ConcurrentHashMap<Long, Set<Object>> redisLikeData = new ConcurrentHashMap<>();
        if (keys != null) {
            for (String key : keys) {
                try {
                    Long postId = Long.parseLong(key.replace("post:likes:", ""));
                    Set<Object> members = redisTemplate.opsForSet().members(key);
                    redisLikeData.put(postId, members);
                } catch (NumberFormatException e) {
                    System.err.println("❌ postId 변환 오류: " + key +e.getMessage());
                }
            }
        }
        List<PostLike> dbLikes = postLikeRepository.findByPost_PostIdIn(redisLikeData.keySet());

        List<PostLike> likesToRemove = new ArrayList<>();
        for (PostLike like : dbLikes) {
            Long postId = like.getPost().getPostId();
            String memberName = like.getMember().getLoginId();

            if (!redisLikeData.containsKey(postId) || !redisLikeData.get(postId).contains(memberName)) {
                likesToRemove.add(like);
            }
        }
        if (!likesToRemove.isEmpty()) {
            postLikeRepository.deleteAllInBatch(likesToRemove);
        }

        List<PostLike> likesToAdd = new ArrayList<>();
        for (Map.Entry<Long, Set<Object>> entry : redisLikeData.entrySet()) {
            Long postId = entry.getKey();
            Set<Object> members = entry.getValue();

            for (Object memberName : members) {
                if (!postLikeRepository.existsByPost_PostIdAndMember_LoginId(postId, memberName.toString())) {
                    Post post = postRepository.findById(postId).orElse(null);
                    Member member = memberRepository.findById(memberName.toString()).orElse(null);
                    if (post != null && member != null) {
                        likesToAdd.add(new PostLike(post, member));
                    }
                }
            }
        }
        if (!likesToAdd.isEmpty()) {
            postLikeRepository.saveAll(likesToAdd);
        }
    }
}
