package com.hobbyproject.controller.postlike;

import com.hobbyproject.service.postlike.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostLikeRestController {

    private final PostLikeService postLikeService;

    @PostMapping("/post/{postId}/like")
    public void upLikeCount(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        postLikeService.addPostLikeAndIncrementCount(postId, userDetails.getUsername());
    }

    @PostMapping("/post/{postId}/like/cancel")
    public void downLikeCount(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails){
        postLikeService.cancelPostLikeAndDecrementCount(postId, userDetails.getUsername());
    }
}
