package com.hobbyproject.repository;

import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.entity.Comment;
import com.hobbyproject.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.hobbyproject.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentResponseDto> getComment(Long postId) {
        List<Comment> comments = jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.postId.eq(postId))
                .distinct()
                .fetch();

        return comments.stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getCommentId())
                        .content(c.getContent())
                        .isDeleted(c.getIsDeleted())
                        .parent(c.getParent())
                        .name(c.getMember().getName())
                        .build())
                .collect(Collectors.toList());
    }
}
