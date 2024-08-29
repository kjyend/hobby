package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Post;

import java.util.List;

public interface PostService {


    void postCreate(PostWriteDto postWriteDto,Long id);

    void postEdit(PostEditDto postEditDto, Long id);

    void postDelete(Long postId, Long id);

    List<Post> findPosts();

    Post findPost(Long postId);

    boolean postMemberCheck(Post post, Long id);
}
