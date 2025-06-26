package com.hobbyproject.repository.post;

import com.hobbyproject.dto.post.request.SearchDto;
import com.hobbyproject.dto.post.response.PostListDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListDto> getList(SearchDto postSearch);

    long postCount();

    void updateViewCount(Long postId, Long updatedCount);

    List<PostListDto> findPostTitleContains(String title, SearchDto Searchdto);
}
