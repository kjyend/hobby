package com.hobbyproject.controller;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping("/post/write")
    public void postWrite(@Valid @ModelAttribute PostWriteDto postWriteDto, BindingResult bindingResult, Model model){
        postService.postCreate(postWriteDto);
    }

    @PostMapping("/post/edit/{postId}")
    public void postEdit(@PathVariable Long postId ,@Valid @ModelAttribute PostEditDto postEditDto, BindingResult bindingResult, Model model){
        postService.postEdit(postEditDto);
    }

    @DeleteMapping("/post/{postId}")
    public void postDelete(@PathVariable Long postId){
        postService.postDelete(postId);
    }
}
