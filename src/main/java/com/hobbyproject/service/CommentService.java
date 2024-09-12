package com.hobbyproject.service;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.entity.Member;

public interface CommentService {
    void commentCreate(CreatedComment createdComment, Long postId, Member member);

    void deleteComment(Long commentId);
}
