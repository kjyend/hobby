package com.hobbyproject.controller;

import com.hobbyproject.entity.Member;
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
    public String postView(@PathVariable("postId") Long postId, HttpSession session, Model model){
        Post post = postService.findPost(postId);
        Member member = (Member) session.getAttribute("memberId");

        if(member!=null){
            if(postService.postMemberCheck(post, member)) {
                boolean isLoggedIn = (session.getAttribute("memberId") != null);
                model.addAttribute("isLoggedIn", isLoggedIn);

            }
        }

        model.addAttribute("post", post);
        return "post/postview";
    }

    @GetMapping("/post/edit/{postId}")
    public String postEdit(@PathVariable("postId") Long postId, Model model){
        model.addAttribute("postId", postId);
        return "post/postedit";
    }

    @GetMapping("/post/write")
    public String postWrite(){
        return "/post/postwrite";
    }
}
