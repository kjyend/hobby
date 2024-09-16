package com.hobbyproject.controller;

import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post")
    public String postList(@RequestParam(name = "page",defaultValue = "1") int page, Model model,  HttpSession session) {
        PostSearchDto postSearch = new PostSearchDto(page, 10);
        PostPagingResponse response = postService.getList(postSearch);

        model.addAttribute("posts", response.getPosts());
        model.addAttribute("totalPostCount", response.getTotalPostCount());
        model.addAttribute("currentPage", page);

        boolean isLoggedIn = (session.getAttribute("memberId") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "post/postlist"; // Thymeleaf 템플릿 파일명
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
