package com.hobbyproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1298469201L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    //inherited
    public final StringPath createdDate = _super.createdDate;

    public final QMember member;

    //inherited
    public final StringPath modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final ListPath<PostLike, QPostLike> postLikes = this.<PostLike, QPostLike>createList("postLikes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final ListPath<UploadFile, QUploadFile> uploadFiles = this.<UploadFile, QUploadFile>createList("uploadFiles", UploadFile.class, QUploadFile.class, PathInits.DIRECT2);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

