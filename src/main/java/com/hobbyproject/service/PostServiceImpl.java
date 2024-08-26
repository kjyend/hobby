package com.hobbyproject.service;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void postCreate(PostWriteDto postWriteDto) {
        Post post=Post.builder()
                .title(postWriteDto.getTitle())
                .content(postWriteDto.getContent())
                .build();

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void postEdit(PostEditDto postEditDto) {
        Post post = postRepository.findById(postEditDto.getPostId()).orElseThrow(IllegalArgumentException::new);

        post.edit(postEditDto);

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void postDelete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);

        postRepository.delete(post);
    }
}
