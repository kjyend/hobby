package com.hobbyproject.dto.comment.response;

import com.hobbyproject.entity.Comment;
import com.hobbyproject.entity.DeleteStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private DeleteStatus isDeleted;
    private Long parentId;
    private String name;


    public CommentResponseDto() {
    }

    @Builder
    public CommentResponseDto(Long id, String content, DeleteStatus isDeleted, Comment parent, String name) {
        this.id = id;
        this.content = content;
        this.isDeleted = isDeleted;
        this.parentId = (parent != null) ? parent.getCommentId() : null;
        this.name = name;
    }
}
