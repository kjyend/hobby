package com.hobbyproject.controller;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping("/post/write")
    public void postWrite(@Valid @RequestBody PostWriteDto postWriteDto, BindingResult bindingResult, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        postService.postCreate(postWriteDto,member);
    }

    @PostMapping("/post/edit/{postId}")
    public ResponseEntity<String> postEdit(@PathVariable("postId") Long postId, @Valid @RequestBody PostEditDto postEditDto, BindingResult bindingResult, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        if(postService.postEdit(postEditDto,member)){
            return ResponseEntity.ok("Post 삭제에 성공했습니다.");
        }else{
            return ResponseEntity.badRequest().body("Post 삭제에 실패했습니다.");
        }
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> postDelete(@PathVariable("postId") Long postId, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        if (postService.postDelete(postId, member)) {
            return ResponseEntity.ok("Post 삭제에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("Post 삭제에 실패했습니다.");
        }
    }
}
