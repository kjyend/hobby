package com.hobbyproject.dto.post.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostListDto {

    private Long postId;
    private String title;
    private String content;
    private Long count;
    private Long likeCount;
    private String createdDate;

    public PostListDto() {
    }

    @Builder
    public PostListDto(Long postId, String title, String content, Long count, Long likeCount,String createdDate) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.count = count;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
    }

}
