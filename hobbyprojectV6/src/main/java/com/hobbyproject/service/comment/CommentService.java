package com.hobbyproject.service.comment;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.dto.comment.response.CreateCommentResponse;

import java.util.List;

public interface CommentService {
    CreateCommentResponse commentCreate(CreatedComment createdComment, Long postId, String loginId);

    void deleteComment(Long commentId);

    List<CommentResponseDto> getList(Long postId);

    boolean isCommentOwner(Long commentId, String loginId);
}
