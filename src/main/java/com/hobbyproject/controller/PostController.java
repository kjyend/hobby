package com.hobbyproject.controller;

import com.hobbyproject.entity.Post;
import com.hobbyproject.service.PostService;
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
    public String postList(Model model){
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts",posts);
        return "post/postlist";
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
