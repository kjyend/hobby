package com.hobbyproject.service.postlike;

import com.hobbyproject.entity.PostLike;
import com.hobbyproject.repository.postlike.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PostLikeRepository postLikeRepository;
    private static final String LIKE_KEY = "post:likes:";

    @Override
    @Transactional
    public void likePost(Long postId, String memberName) {
        redisTemplate.opsForSet().add(LIKE_KEY + postId, memberName);
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
        return redisTemplate.opsForSet().size(LIKE_KEY + postId);
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Async
    public void syncLikesToDB() {
        Set<String> keys = redisTemplate.keys(LIKE_KEY + "*");
        ConcurrentHashMap<Long, Set<Object>> redisLikeData = new ConcurrentHashMap<>();
        if (keys != null) {
            for (String key : keys) {
                Long postId = Long.parseLong(key.replace("post:likes:", ""));
                Set<Object> members = redisTemplate.opsForSet().members(key);
                redisLikeData.put(postId, members);
            }
        }

        List<PostLike> dbLikes = postLikeRepository.findAll();
        for (PostLike like : dbLikes) {
            Long postId = like.getPost().getPostId();
            String memberName = like.getMember().getLoginId();

            if (!redisLikeData.containsKey(postId) || !redisLikeData.get(postId).contains(memberName)) {
                postLikeRepository.delete(like);
            }
        }

        for (Map.Entry<Long, Set<Object>> entry : redisLikeData.entrySet()) {
            Long postId = entry.getKey();
            Set<Object> members = entry.getValue();

            for (Object memberName : members) {
                if (!postLikeRepository.existsByPost_PostIdAndMember_LoginId(postId, memberName.toString())) {
                    postLikeRepository.save(new PostLike(postId, memberName.toString()));
                }
            }
        }
    }
}
