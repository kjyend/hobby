package com.hobbyproject.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostWriteDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    public PostWriteDto(){
    }

    @Builder
    public PostWriteDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
