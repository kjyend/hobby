package com.hobbyproject.service.postlike;

import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.PostLike;
import com.hobbyproject.repository.member.MemberRepository;
import com.hobbyproject.repository.post.PostRepository;
import com.hobbyproject.repository.postlike.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional
    public Post addPostLikeAndIncrementCount(Long boardId, String memberName) {
        Post post = postRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Post값이 없습니다."));
        Member member=memberRepository.findByLoginId(memberName).orElseThrow(()->new IllegalArgumentException("같은 이름이 없습니다."));
        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post);

        if(postLike != null) {
            throw new IllegalArgumentException("좋아요를 이미 눌렀습니다.");
        } else {
            postLikeRepository.save(PostLike.builder().member(member).post(post).build());
            updateLikeCount(post);
        }
        return postRepository.save(post);
    }

    private void updateLikeCount(Post post) {
        Long count = postLikeRepository.countAllByPost(post);
        post.updateLikeCount(count);
    }

    @Override
    @Transactional
    public Post cancelPostLikeAndDecrementCount(Long boardId, String memberName) {
        Post post = postRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Post값이 없습니다."));
        Member member=memberRepository.findByLoginId(memberName).orElseThrow(()->new IllegalArgumentException("같은 이름이 없습니다."));
        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post);

        if(postLike != null) {
            postLikeRepository.delete(postLike);
            updateLikeCount(post);
        }else{
            throw new IllegalArgumentException("좋아요를 누르지 않았습니다.");
        }
        return postRepository.save(post);
    }

    @Override
    public boolean isUserLikedPost(Long postId, String memberName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Member member = memberRepository.findByLoginId(memberName)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return postLikeRepository.existsByPostAndMember(post,member);
    }

    @Override
    public Long getLikeCount(Long postId) {
        return postRepository.findLikeCountByPostId(postId);
    }

}
