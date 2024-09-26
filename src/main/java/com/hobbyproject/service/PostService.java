package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {


    void postCreate(PostWriteDto postWriteDto, String loginId, List<MultipartFile> images);

    boolean postEdit(PostEditDto postEditDto, String loginId, List<MultipartFile> images);

    boolean postDelete(Long postId, String loginId);


    Post findPost(Long postId);

    boolean postMemberCheck(Post post, String loginId);

    PostPagingResponse getList(PostSearchDto postSearch);
}
