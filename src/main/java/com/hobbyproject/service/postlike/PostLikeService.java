package com.hobbyproject.service.postlike;

import com.hobbyproject.entity.Post;

public interface PostLikeService {

    public Post addPostLikeAndIncrementCount(Long boardId, String memberName);

    public Post cancelPostLikeAndDecrementCount(Long boardId, String memberName);
}
