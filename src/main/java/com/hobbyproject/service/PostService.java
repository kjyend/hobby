package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;

import java.util.List;

public interface PostService {


    void postCreate(PostWriteDto postWriteDto, Member member);

    boolean postEdit(PostEditDto postEditDto, Member member);

    boolean postDelete(Long postId, Member member);

    List<Post> findPosts();

    Post findPost(Long postId);

    boolean postMemberCheck(Post post, Member member);
}
