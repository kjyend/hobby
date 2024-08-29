package com.hobbyproject.controller;

import com.hobbyproject.entity.Post;
import com.hobbyproject.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post")
    public String postList(Model model, HttpSession session){
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts",posts);

        boolean isLoggedIn = (session.getAttribute("memberId") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "post/postlist";
    }

    @GetMapping("/post/{postId}")
    public String postView(@PathVariable Long postId, HttpSession session, Model model){
        Post post = postService.findPost(postId);
        Long memberId = (Long) session.getAttribute("memberId");

        boolean check = postService.postMemberCheck(post, memberId);

        boolean isLoggedIn = (session.getAttribute("memberId") != null);

        if(isLoggedIn&&check) {
            model.addAttribute("isLoggedIn");
        }

        model.addAttribute("post", post);
        return "post/postview";
    }

    @GetMapping("/post/edit/{postId}")
    public String postEdit(@PathVariable Long postId, Model model){
        model.addAttribute("postId", postId);
        return "post/postedit";
    }

    @GetMapping("/post/write")
    public String postWrite(){
        return "/post/postwrite";
    }
}
