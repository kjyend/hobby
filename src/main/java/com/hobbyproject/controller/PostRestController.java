package com.hobbyproject.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostRestController {

    @PostMapping("/post/write")
    public void postWrite(){

    }

    @PatchMapping("/post/{postId}")
    public void postEdit(){

    }

    @DeleteMapping("/post/{postId}")
    public void postDelete(){

    }
}
