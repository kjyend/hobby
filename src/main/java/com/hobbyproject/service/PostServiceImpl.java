package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.dto.post.response.PostResponseDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.MemberRepository;
import com.hobbyproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final UploadFileService uploadFileService;

    @Override
    @Transactional
    public void postCreate(PostWriteDto postWriteDto, String loginId, List<MultipartFile> images) {

        Member memberCheck = memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);

        Post post=Post.builder()
                .title(postWriteDto.getTitle())
                .content(postWriteDto.getContent())
                .member(memberCheck)
                .build();

        postRepository.save(post);
        if(!images.getFirst().getOriginalFilename().equals("")) {
            uploadFileService.uploadFile(post, images);
        }
    }

    @Override
    @Transactional
    public boolean postEdit(PostEditDto postEditDto, String loginId, List<MultipartFile> images) {
        try {
            Member memberCheck = memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);

            Post post = postRepository.findById(postEditDto.getPostId()).orElseThrow(IllegalArgumentException::new);

            if (!post.getMember().equals(memberCheck)) {
                throw new IllegalArgumentException("회원이 일치하지 않습니다.");
            }

            post.edit(postEditDto);

            postRepository.save(post);

            uploadFileService.uploadFile(post,images);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean postDelete(Long postId, String loginId) {
        try {
            Member memberCheck = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException("회원이 일치하지 않습니다."));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

            if (!post.getMember().equals(memberCheck)) {
                throw new IllegalArgumentException("회원이 일치하지 않습니다.");
            }

            postRepository.delete(post);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean postMemberCheck(Post post, String loginId) {

        memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);

        return post.getMember().getLoginId().equals(loginId);
    }

    @Override
    public PostPagingResponse getList(PostSearchDto postSearch) {
        List<Post> posts = postRepository.getList(postSearch);
        long totalPostCount = postRepository.count();

        return new PostPagingResponse(posts.stream().map(PostResponseDto::new).collect(Collectors.toList()), totalPostCount);
    }
}
