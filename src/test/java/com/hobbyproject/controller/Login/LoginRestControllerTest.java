package com.hobbyproject.controller.Login;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyproject.dto.member.request.LoginDto;
import com.hobbyproject.dto.member.request.SignupDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        assertTrue(passwordEncoder.matches("qqq111", member.getPassword()));

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
                .password(passwordEncoder.encode("ddd444"))
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
        // 회원 생성 및 비밀번호 암호화
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member); // 회원 저장

        // 로그인 요청 - form 데이터로 전송
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "asd123")
                        .param("password", "qqq111")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 회원정보가 없어서 실패")
    void loginNoInformationFailTest() throws Exception {
        LoginDto login = LoginDto.builder()
                .loginId("asd123")
                .password("qqq111")
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error=true"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 아이디가 달라서 실패")
    void loginNoMatchIdFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        LoginDto login = LoginDto.builder()
                .loginId("a123")
                .password("qqq111")
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error=true"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 아이디가 달라서 실패")
    void loginNoMatchPasswordFailTest() throws Exception {
        // 회원 생성 및 비밀번호 암호화
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "a123")
                        .param("password", "qqq111")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error=true"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccessTest() throws Exception {
        UserDetails userDetails = User.withUsername("asd123")
                .password("password")
                .roles("USER")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?logout=true"))
                .andDo(MockMvcResultHandlers.print());
    }
}