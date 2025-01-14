package com.hobbyproject.service.postlike;

import com.hobbyproject.entity.Post;

public interface PostLikeService {

    Post addPostLikeAndIncrementCount(Long boardId, String memberName);

    Post cancelPostLikeAndDecrementCount(Long boardId, String memberName);

    boolean isUserLikedPost(Long postId, String memberName);

    Long getLikeCount(Long postId);
}
