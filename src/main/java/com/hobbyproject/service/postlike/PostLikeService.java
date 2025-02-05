package com.hobbyproject.service.postlike;


public interface PostLikeService {

    void likePost(Long postId, String memberName);

    void unlikePost(Long postId, String memberName);

    boolean isUserLikedPost(Long postId, String memberName);

    Long getLikeCount(Long postId);
}
