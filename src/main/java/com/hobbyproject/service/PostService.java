package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;

public interface PostService {


    void postCreate(PostWriteDto postWriteDto);

    void postEdit(PostEditDto postEditDto);

    void postDelete(Long postId);
}
