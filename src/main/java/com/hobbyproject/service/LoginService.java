package com.hobbyproject.service;

import com.hobbyproject.dto.member.request.SignupDto;

public interface LoginService {

    void signup(SignupDto signupDto);

    boolean checkLoginIdDup(String loginId);
}
