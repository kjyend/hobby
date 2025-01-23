package com.hobbyproject.dto.post.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostListDto {

    private Long postId;
    private String title;
    private Long count;
    private Long likeCount;
    private String createdDate;
    private String name;

    public PostListDto() {
    }

    @Builder
    public PostListDto(Long postId, String title, Long count, Long likeCount,String createdDate, String name) {
        this.postId = postId;
        this.title = title;
        this.count = count;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
        this.name = name;
    }

}
