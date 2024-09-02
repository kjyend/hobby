package com.hobbyproject.dto.post.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDeleteDto {

    @NotNull(message = "포스트 번호는 필수입니다.")
    private Long postId;

    public PostDeleteDto(){

    }

    @Builder
    public PostDeleteDto(Long postId) {
        this.postId = postId;
    }
}
