package com.hobbyproject.service;

import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.repository.MemberRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceImplTest {

    @Autowired
    private LoginService loginService;

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
        assertEquals("qqq111", member.getPassword());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Member result = loginService.login("asd123", "qqq111");

        assertNotNull(result);
        assertEquals("asd123", result.getLoginId());
        assertEquals("qqq111", result.getPassword());
        assertEquals("김회원", result.getName());
        assertEquals(LocalDate.parse("2000-11-11"), result.getBirthday());
    }


    @Test
    @DisplayName("아이디가 null로 로그인 실패")
    void loginNullLoginIdFailTest() {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Member result = loginService.login(null, "qqq111");

        assertNull(result);
    }

    @Test
    @DisplayName("비밀번호가 null로 로그인 실패")
    void loginNullPasswordFailTest() {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Member result = loginService.login("asd123", null);

        assertNull(result);
    }

    @Test
    @DisplayName("다른 아이디로 로그인 실패")
    void loginWrongLoginIdFailTest() {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Member result = loginService.login("aaa111", "qqq111");

        assertNull(result);
    }

    @Test
    @DisplayName("다른 비밀번호로 로그인 실패")
    void loginWrongPasswordFailTest() {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Member result = loginService.login("asd123", "ddd222");

        assertNull(result);
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