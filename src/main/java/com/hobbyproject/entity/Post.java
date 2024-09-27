package com.hobbyproject.entity;

import com.hobbyproject.dto.post.request.PostEditDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    private Long postId;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments=new ArrayList<>();

    @Builder
    public Post(Long postId, String title, String content, Member member) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void edit(PostEditDto postEditDto) {
        title=postEditDto.getTitle();
        content=postEditDto.getContent();
    }
}
