package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.MemberRepository;
import com.hobbyproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void postCreate(PostWriteDto postWriteDto,Member member) {

        Member memberCheck = memberRepository.findById(member.getMemberId()).orElseThrow(IllegalArgumentException::new);

        Post post=Post.builder()
                .title(postWriteDto.getTitle())
                .content(postWriteDto.getContent())
                .member(memberCheck)
                .build();

        postRepository.save(post);
    }

    @Override
    @Transactional
    public boolean postEdit(PostEditDto postEditDto,Member member) {
        try {
            Member memberCheck = memberRepository.findById(member.getMemberId()).orElseThrow(IllegalArgumentException::new);

            Post post = postRepository.findById(postEditDto.getPostId()).orElseThrow(IllegalArgumentException::new);

            if (!post.getMember().equals(memberCheck)) {
                throw new IllegalArgumentException("회원이 일치하지 않습니다.");
            }

            post.edit(postEditDto);

            postRepository.save(post);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean postDelete(Long postId, Member member) {
        try {
            Member memberCheck = memberRepository.findById(member.getMemberId())
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
    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean postMemberCheck(Post post, Member member) {

        memberRepository.findById(member.getMemberId()).orElseThrow(IllegalArgumentException::new);

        if(post.getMember().equals(member)){
            return true;
        }
        return false;
    }
}
