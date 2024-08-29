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
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void postCreate(PostWriteDto postWriteDto,Long id) {

        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        Post post=Post.builder()
                .title(postWriteDto.getTitle())
                .content(postWriteDto.getContent())
                .member(member)
                .build();

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void postEdit(PostEditDto postEditDto,Long id) {

        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        Post post = postRepository.findById(postEditDto.getPostId()).orElseThrow(IllegalArgumentException::new);

        if (!post.getMember().equals(member)) {
            throw new IllegalArgumentException("회원이 일치하지 않습니다.");
        }

        post.edit(postEditDto);

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void postDelete(Long postId,Long id) {

        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);

        if (!post.getMember().equals(member)) {
            throw new IllegalArgumentException("회원이 일치하지 않습니다.");
        }

        postRepository.delete(post);
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
    public boolean postMemberCheck(Post post, Long id) {

        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        if(post.getMember().equals(member)){
            return true;
        }
        return false;
    }
}
