package com.hobbyproject.repository.post;

import com.hobbyproject.dto.post.request.SearchDto;
import com.hobbyproject.dto.post.response.PostListDto;
import com.hobbyproject.entity.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostListDto> getList(SearchDto postSearch) {
        return jpaQueryFactory.select(
                Projections.fields(PostListDto.class,
                        QPost.post.postId,
                        QPost.post.title,
                        QPost.post.count,
                        QPost.post.likeCount,
                        QPost.post.createdDate,
                        QPost.post.member.name
                        ))
                .from(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.postId.desc())
                .fetch();
    }

    @Override
    public long postCount() {
        return Optional.ofNullable(
                jpaQueryFactory.select(QPost.post.count())
                        .from(QPost.post)
                        .fetchOne())
                .orElse(0L);
    }

    @Override
    public void updateViewCount(Long postId, Long updatedCount) {
        jpaQueryFactory.update(QPost.post)
                .set(QPost.post.count,updatedCount)
                .where(QPost.post.postId.eq(postId))
                .execute();
    }

    @Override
    public List<PostListDto> findPostTitleContains(String title, SearchDto Searchdto) {
        return jpaQueryFactory.select(
                        Projections.fields(PostListDto.class,
                                QPost.post.postId,
                                QPost.post.title,
                                QPost.post.count,
                                QPost.post.likeCount,
                                QPost.post.createdDate,
                                QPost.post.member.name
                        ))
                .from(QPost.post)
                .where(QPost.post.title.contains(title))
                .limit(Searchdto.getSize())
                .offset(Searchdto.getOffset())
                .orderBy(QPost.post.postId.desc())
                .fetch();
    }
}
