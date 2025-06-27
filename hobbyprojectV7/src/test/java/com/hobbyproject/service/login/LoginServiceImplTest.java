package com.hobbyproject.service.login;

import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.repository.member.MemberRepository;
import com.hobbyproject.service.login.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceImplTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void clear(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccessTest(){
        SignupDto signupDto = SignupDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .checkPassword("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        loginService.signup(signupDto);

        assertEquals(1L, memberRepository.count());

        Member member = memberRepository.findAll().get(0);

        assertEquals("asd123", member.getLoginId());
        assertTrue(passwordEncoder.matches("qqq111", member.getPassword()));
    }



    @Test
    @DisplayName("아이디 중복 확인(중복이 없어서 성공)")
    void AlreadyLoginIdSuccessTest(){
        boolean result = loginService.checkLoginIdDup("asd123");

        assertFalse(result);
    }

    @Test
    @DisplayName("이미 있는 아이디 확인")
    void AlreadyLoginIdFailTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        boolean result = loginService.checkLoginIdDup("asd123");

        assertTrue(result);

    }


}