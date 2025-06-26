package com.hobbyproject.service.login;

import com.hobbyproject.dto.member.request.SignupDto;

public interface LoginService {

    void signup(SignupDto signupDto);

    boolean checkLoginIdDup(String loginId);
}
