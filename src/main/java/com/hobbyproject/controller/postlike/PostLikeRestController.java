package com.hobbyproject.controller.postlike;

import com.hobbyproject.service.postlike.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostLikeRestController {

    private final PostLikeService postLikeService;

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<String> upLikeCount(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        postLikeService.likePost(postId, userDetails.getUsername());
        return ResponseEntity.ok("좋아요를 눌렀습니다.");
    }

    @PostMapping("/post/{postId}/like/cancel")
    public ResponseEntity<String> downLikeCount(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails){
        postLikeService.unlikePost(postId, userDetails.getUsername());
        return ResponseEntity.ok("좋아요를 취소 했습니다.");
    }

    @GetMapping("/post/{postId}/like/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        boolean liked = postLikeService.isUserLikedPost(postId, userDetails.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("liked", liked));
    }

    @GetMapping("/post/{postId}/like/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable("postId") Long postId) {
        Long count = postLikeService.getLikeCount(postId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
