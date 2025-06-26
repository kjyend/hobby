package com.hobbyproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    private String loginId;
    private String password;
    private String name;
    private LocalDate birthday;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts=new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PostLike> postLikes=new ArrayList<>();

    @Builder
    public Member(String loginId, String password, String name, LocalDate birthday) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
    }
}
