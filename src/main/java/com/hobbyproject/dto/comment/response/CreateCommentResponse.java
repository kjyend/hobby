package com.hobbyproject.dto.comment.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCommentResponse {

    private String postOwnerId;
    private boolean success;
    private String content;

    public CreateCommentResponse() {
    }

    @Builder
    public CreateCommentResponse(String postOwnerId, boolean success, String content) {
        this.postOwnerId = postOwnerId;
        this.success = success;
        this.content = content;
    }
}
