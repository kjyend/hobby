package com.hobbyproject.repository.post;

import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearchDto postSearch);

    long postCount();

    void updateViewCount(Long postId, Long updatedCount);
}
