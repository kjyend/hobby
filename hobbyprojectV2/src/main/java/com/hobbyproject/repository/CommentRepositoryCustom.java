package com.hobbyproject.repository;

import com.hobbyproject.dto.comment.response.CommentResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> getComment(Long postId);
}
