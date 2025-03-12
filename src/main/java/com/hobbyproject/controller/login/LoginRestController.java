package com.hobbyproject.controller.login;

import com.hobbyproject.dto.member.request.LoginDto;
import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.service.login.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginRestController {

    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto, BindingResult bindingResult){

        if(loginService.checkLoginIdDup(signupDto.getLoginId())){
            bindingResult.addError(new FieldError("signupDto","loginId","로그인 아이디가 중복입니다."));
        }

        if(!signupDto.getPassword().equals(signupDto.getCheckPassword())){
            bindingResult.addError(new FieldError("signupDto","passwordCheck","비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        loginService.signup(signupDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult){
        String token = loginService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
