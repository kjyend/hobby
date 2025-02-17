package com.hobbyproject.repository.postlike;

import com.hobbyproject.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    boolean existsByPost_PostIdAndMember_LoginId(Long postId, String memberName);

    List<PostLike> findByPost_PostIdIn(Set<Long> postIds);
}
