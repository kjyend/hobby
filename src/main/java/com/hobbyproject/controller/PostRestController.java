package com.hobbyproject.controller;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.service.PostService;
import jakarta.servlet.http.HttpSession;
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
    public void postWrite(@Valid @ModelAttribute PostWriteDto postWriteDto, BindingResult bindingResult, Model model, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        postService.postCreate(postWriteDto,member);
    }

    @PostMapping("/post/edit/{postId}")
    public void postEdit(@Valid @ModelAttribute PostEditDto postEditDto, BindingResult bindingResult,@PathVariable Long postId , Model model, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        postService.postEdit(postEditDto,member);
    }

    @DeleteMapping("/post/{postId}")
    public void postDelete(@PathVariable Long postId, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        postService.postDelete(postId,member);
    }
}
