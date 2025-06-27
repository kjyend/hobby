package com.hobbyproject.repository.commnet;

import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.hobbyproject.entity.QComment.comment;
import static com.hobbyproject.entity.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentResponseDto> getComment(Long postId) {
        return jpaQueryFactory.select(Projections.fields(CommentResponseDto.class,
                        comment.commentId.as("id"),
                        comment.content,
                        comment.isDeleted,
                        comment.parent.commentId.as("parentId"),
                        member.name,
                        member.loginId
                ))
                .from(comment)
                .leftJoin(comment.member, member)
                .where(comment.post.postId.eq(postId))
                .fetch();
    }
}
