package com.hobbyproject.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedComment {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private Long parentId;

    @Builder
    public CreatedComment(String content, Long parentId) {
        this.content = content;
        this.parentId = parentId;
    }
}
