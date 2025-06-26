package com.hobbyproject.dto.post.response;

import lombok.Getter;
import java.util.List;

@Getter
public class PostPagingResponse {

    private List<PostListDto> posts;
    private long totalPostCount;

    public PostPagingResponse() {
    }

    public PostPagingResponse(List<PostListDto> posts, long totalPostCount) {
        this.posts = posts;
        this.totalPostCount = totalPostCount;
    }
}
