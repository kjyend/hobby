package com.hobbyproject.controller;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;


    @GetMapping("/post/{postId}/comment")
    public List<CommentResponseDto> getCommentList(@PathVariable("postId") Long postId){
        return commentService.getList(postId);
    }

    @PostMapping("/post/{postId}/comment")
    public void addComment(@PathVariable("postId") Long postId, @RequestBody CreatedComment createdComment, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        commentService.commentCreate(createdComment, postId, member);
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public void deleteComment(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId){
        commentService.deleteComment(commentId);
    }

}
