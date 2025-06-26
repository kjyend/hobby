package com.hobbyproject.service.post;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.SearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostListDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.dto.post.response.PostResponseDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.member.MemberRepository;
import com.hobbyproject.repository.post.PostRepository;
import com.hobbyproject.service.file.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


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
        if (images != null && !images.isEmpty() && !Objects.equals(images.get(0).getOriginalFilename(), "")) {
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

            if (images != null && !images.isEmpty() && !Objects.equals(images.get(0).getOriginalFilename(), "")) {
                uploadFileService.uploadFile(post, images);
            }
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
    public PostResponseDto findPost(Long postId) {
        return new PostResponseDto(postRepository.findById(postId).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public boolean postMemberCheck(Long postId, String loginId) {

        Member member = memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);

        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);

        return post.getMember().getLoginId().equals(member.getLoginId());
    }

    @Override
    public PostPagingResponse getList(SearchDto postSearch) {
        List<PostListDto> posts = postRepository.getList(postSearch);
        long totalPostCount = postRepository.postCount();

        return new PostPagingResponse(posts, totalPostCount);
    }

    @Override
    public Long getViewCount(Long postId) {
        return postRepository.findCountByPostId(postId);
    }

    @Transactional
    @Override
    public void incrementViewCount(Long postId) {
        postRepository.updateViewCount(postId, getViewCount(postId)+1);
    }
}
