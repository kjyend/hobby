package com.hobbyproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyproject.dto.member.request.LoginDto;
import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.repository.MemberRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccessTest() throws Exception {
        SignupDto signupDto = SignupDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .checkPassword("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(1L, memberRepository.count());

        Member member = memberRepository.findAll().get(0);

        assertEquals("asd123", member.getLoginId());
        assertEquals("qqq111", member.getPassword());

    }

    @Test
    @DisplayName("회원가입 더블 체크에서 실패")
    void signupPasswordCheckFailTest() throws Exception {
        SignupDto signupDto = SignupDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .checkPassword("ddd222")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원가입 중복된 아이디로 실패")
    void signupDuplicationFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password("ddd444")
                .name("김원회")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();

        memberRepository.save(member);

        SignupDto signupDto = SignupDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .checkPassword("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        LoginDto login = LoginDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId",member);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 회원정보가 없어서 실패")
    void loginNoInformationFailTest() throws Exception {
        LoginDto login = LoginDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId",true);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 아이디가 달라서 실패")
    void loginNoMatchIdFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        LoginDto login = LoginDto.builder()
                .loginId("a123")
                .password("qqq111")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId",member);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 아이디가 달라서 실패")
    void loginNoMatchPasswordFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        LoginDto login = LoginDto.builder()
                .loginId("asd123")
                .password("asd123")
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId",member);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccessTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", "asd123");

        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertTrue(session.isInvalid());
    }
}