package com.hobbyproject.controller;

import com.hobbyproject.entity.Member;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("session이 있을때 controller에서 true를 보내는가?")
    void homeTrueTest() throws Exception {
        Member member = Member.builder()
                .loginId("1")
                .password("1")
                .name("이름")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId",member);

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("memberId", Matchers.is(member)))
                .andExpect(MockMvcResultMatchers.model().attribute("isLoggedIn",true))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("HomeContoller가 제대로 된 home.html을 내보내는가?")
    void homeNameCheckTest() throws Exception {
        Member member = Member.builder()
                .loginId("1")
                .password("1")
                .name("이름")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId",member);

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("session이 null일때 controller에서 실행이 되는가")
    void homeFalseTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("isLoggedIn",false))
                .andDo(MockMvcResultHandlers.print());
    }
}