package com.hobbyproject.repository.post;

import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.response.PostListDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListDto> getList(PostSearchDto postSearch);

    long postCount();

    void updateViewCount(Long postId, Long updatedCount);
}
