package com.hobbyproject.repository;

import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.entity.Comment;
import com.hobbyproject.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentResponseDto> getComment(Long postId) {
        List<Comment> comments = jpaQueryFactory
                .selectFrom(QComment.comment)
                .leftJoin(QComment.comment.parent).fetchJoin()  // 자식 댓글을 함께 가져옴
                .where(QComment.comment.post.postId.eq(postId))  // 최상위 댓글만 가져옴
                .fetch();

        return comments.stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getCommentId())
                        .content(c.getContent())
                        .isDeleted(c.getIsDeleted())
                        .parent(c.getParent())  // 부모 댓글 정보 전달
                        .name(c.getMember().getName())  // 작성자 이름
                        .build())
                .collect(Collectors.toList());
    }
}
