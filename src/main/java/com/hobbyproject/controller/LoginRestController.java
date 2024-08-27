package com.hobbyproject.controller;

import com.hobbyproject.dto.member.request.LoginDto;
import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginRestController {

    private final LoginService loginService;

    @PostMapping("/login")
    public void login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            return ;
        }

        Member loginMember=loginService.login(loginDto.getLoginId(),loginDto.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail","아이디 똔느 비밀번호가 맞지 않습니다.");
            return ;
        }

        HttpSession session = request.getSession();
        session.setAttribute("memberId",loginMember);

    }

    @PostMapping("/signup")
    public void signup(@Valid @ModelAttribute SignupDto signupDto,BindingResult bindingResult){

        if(loginService.checkLoginIdDup(signupDto.getLoginId())){
            bindingResult.addError(new FieldError("signupDto","loginId","로그인 아이디가 중복입니다."));
        }

        if(!signupDto.getPassword().equals(signupDto.getCheckPassword())){
            bindingResult.addError(new FieldError("signupDto","passwordCheck","비밀번호가 일치하지 않습니다."));
        }

        loginService.signup(signupDto);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

}
