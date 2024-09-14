package com.hobbyproject.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEditDto {

    @NotNull(message = "포스트 번호는 필수입니다.")
    private Long postId;
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    public PostEditDto(){

    }

    @Builder
    public PostEditDto(Long postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
