package com.hobbyproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.MemberRepository;
import com.hobbyproject.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
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
class PostRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("post 글쓰기 성공")
    void postWriteSuccessTest() throws Exception {
        // given
        PostWriteDto postWrite = PostWriteDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))  // 비밀번호는 암호화
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        MockMultipartFile mockImage = new MockMultipartFile(
                "images", "test.png", "image/png", "test image content".getBytes());

        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/write")
                        .file(mockImage)
                        .param("title", postWrite.getTitle())
                        .param("content", postWrite.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.user(member.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("post 글 수정 성공")
    void postEditSuccessTest() throws Exception {
        // given
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(member)
                .build();

        postRepository.save(post);

        PostEditDto postEditDto = PostEditDto.builder()
                .postId(post.getPostId())
                .title("변경된 제목입니다.")
                .content("변경된 내용입니다.")
                .build();

        MockMultipartFile mockImage = new MockMultipartFile(
                "images", "test.png", "image/png", "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/edit/{postId}", post.getPostId())
                        .file(mockImage)  // 이미지 파일 추가
                        .param("title", postEditDto.getTitle())
                        .param("content", postEditDto.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.user(member.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(1L, postRepository.count());

        Post postCheck = postRepository.findAll().get(0);
        assertEquals("변경된 제목입니다.", postCheck.getTitle());
        assertEquals("변경된 내용입니다.", postCheck.getContent());
    }

    @Test
    @DisplayName("다른 사람이 post 수정 시도 시 post 글 수정 실패")
    void postOtherMemberEditFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(member)
                .build();
        postRepository.save(post);

        Member member2 = Member.builder()
                .loginId("aaa111")
                .password(passwordEncoder.encode("bbb222"))
                .name("김원해")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();
        memberRepository.save(member2);

        PostEditDto postEditDto = PostEditDto.builder()
                .postId(post.getPostId())
                .title("변경된 제목입니다.")
                .content("변경된 내용입니다.")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/edit/{postId}", post.getPostId())
                        .param("title", postEditDto.getTitle())
                        .param("content", postEditDto.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.user(member2.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(1L, postRepository.count());

        Post postCheck = postRepository.findAll().get(0);
        assertEquals("제목입니다.", postCheck.getTitle());
        assertEquals("내용입니다.", postCheck.getContent());
    }

    @Test
    @DisplayName("없는 post 수정 시도 post 글 수정 실패")
    void postEditFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();
        memberRepository.save(member);

        PostEditDto postEditDto = PostEditDto.builder()
                .postId(1L)
                .title("변경된 제목입니다.")
                .content("변경된 내용입니다.")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/edit/{postId}", postEditDto.getPostId())
                        .param("title", postEditDto.getTitle())
                        .param("content", postEditDto.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.user(member.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("post 글 삭제 성공")
    void postDeleteSuccessTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(member)
                .build();

        postRepository.save(post);

        assertEquals(1L, postRepository.count());

        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}",post.getPostId())
                        .with(SecurityMockMvcRequestPostProcessors.user(member.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(0L, postRepository.count());
    }



    @Test
    @DisplayName("다른 사람이 post 삭제 시도 post 글 삭제 실패")
    void postOtherMemberDeleteFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(member)
                .build();

        postRepository.save(post);

        Member member2 = Member.builder()
                .loginId("aaa111")
                .password(passwordEncoder.encode("bbb222"))
                .name("김원해")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member2);

        assertEquals(1L, postRepository.count());

        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", post.getPostId())
                        .with(SecurityMockMvcRequestPostProcessors.user(member2.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Post 삭제에 실패했습니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("없는 post 삭제 시도 post 글 삭제 실패")
    void noPostDeleteFailTest() throws Exception {
        Member member = Member.builder()
                .loginId("asd123")
                .password(passwordEncoder.encode("qqq111"))
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);


        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(member.getLoginId()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Post 삭제에 실패했습니다."))
                .andDo(MockMvcResultHandlers.print());
    }



}