package com.hobbyproject.service;

import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;

public interface LoginService {
    Member login(String loginId, String password);

    void signup(SignupDto signupDto);

    boolean checkLoginIdDup(String loginId);
}
