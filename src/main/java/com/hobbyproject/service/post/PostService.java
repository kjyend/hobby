package com.hobbyproject.service.post;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.SearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.dto.post.response.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {


    void postCreate(PostWriteDto postWriteDto, String loginId, List<MultipartFile> images);

    boolean postEdit(PostEditDto postEditDto, String loginId, List<MultipartFile> images);

    boolean postDelete(Long postId, String loginId);


    PostResponseDto findPost(Long postId);

    boolean postMemberCheck(Long postId, String loginId);

    PostPagingResponse getList(SearchDto postSearch);

    Long getViewCount(Long postId);

    void incrementViewCount(Long postId);
}
