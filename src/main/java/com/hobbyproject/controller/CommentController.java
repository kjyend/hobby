package com.hobbyproject.controller;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.entity.Member;
import com.hobbyproject.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public void addComment(@PathVariable("postId") Long postId, @RequestBody CreatedComment createdComment, HttpSession session){
        Member member = (Member) session.getAttribute("memberId");
        commentService.commentCreate(createdComment, postId, member);
    }

    @PostMapping("/post/{postId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long postId,@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }


}
