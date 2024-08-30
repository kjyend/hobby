package com.hobbyproject.controller;

import com.hobbyproject.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model){

        boolean isLoggedIn = (session.getAttribute("memberId") != null);

        model.addAttribute("isLoggedIn",isLoggedIn);

        return "home";
    }


}
