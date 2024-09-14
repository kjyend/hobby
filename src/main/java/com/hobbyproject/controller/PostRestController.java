package com.hobbyproject.controller;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostResponseDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @GetMapping("/posts")
    public List<PostResponseDto> getList(@RequestBody PostSearchDto postSearch){
        return postService.getList(postSearch);
    }

    @PostMapping("/post/write")
    public void postWrite(@Valid @RequestParam("images") List<MultipartFile> images,@Valid @ModelAttribute PostWriteDto postWriteDto, BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("유효성 검사 실패");
        }

        Member member = (Member) session.getAttribute("memberId");
        postService.postCreate(postWriteDto,member,images);
    }

    @PostMapping("/post/edit/{postId}")
    public ResponseEntity<String> postEdit(@PathVariable("postId") Long postId, @RequestParam("images") List<MultipartFile> images, @Valid @ModelAttribute PostEditDto postEditDto, BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("유효성 검사에 실패했습니다.");
        }

    Member member = (Member) session.getAttribute("memberId");
        if(postService.postEdit(postEditDto, member, images)){
        return ResponseEntity.ok("Post 수정에 성공했습니다.");
    }else{
        return ResponseEntity.badRequest().body("Post 수정에 실패했습니다.");
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
