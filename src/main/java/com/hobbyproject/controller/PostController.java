package com.hobbyproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping("/post/list")
    public String postList(){
        return "post/postlist";
    }

    @GetMapping("/post/edit")
    public String postEdit(){
        return "post/postedit";
    }

    @GetMapping("/post/write")
    public String postWrite(){
        return "/post/postwrite";
    }
}
