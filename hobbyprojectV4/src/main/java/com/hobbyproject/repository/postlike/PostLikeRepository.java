package com.hobbyproject.repository.postlike;

import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    PostLike findByMemberAndPost(Member member, Post post);

    Long countAllByPost(Post post);

    boolean existsByPostAndMember(Post post, Member member);
}
