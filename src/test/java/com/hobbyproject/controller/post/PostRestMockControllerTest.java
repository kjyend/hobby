package com.hobbyproject.controller.post;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.dto.post.response.PostResponseDto;
import com.hobbyproject.service.post.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureMockMvc
public class PostRestMockControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PostServiceImpl postService;

    @Test
    @DisplayName("Post 삭제 성공 테스트")
    @WithMockUser(username = "asd123")
    void postWhenDeleteSuccessTest() throws Exception {
        // when
        when(postService.postDelete(anyLong(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // then
        verify(postService).postDelete(1L, "asd123");
    }

    @Test
    @DisplayName("Mock을 사용한 포스트 작성 테스트")
    void postWriteWithMockSuccessTest() throws Exception {
        // given
        PostWriteDto postWriteDto = PostWriteDto.builder()
                .title("Mock 제목")
                .content("Mock 내용")
                .build();

        MockMultipartFile mockImage = new MockMultipartFile(
                "images", "test.png", "image/png", "test image content".getBytes());

        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/write")
                        .file(mockImage)
                        .param("title", postWriteDto.getTitle())
                        .param("content", postWriteDto.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.user("mockUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        verify(postService, times(1)).postCreate(any(PostWriteDto.class), eq("mockUser"), anyList());
    }

    @Test
    @DisplayName("Mock을 사용한 포스트 수정 테스트")
    void postEditWithMockSuccessTest() throws Exception {
        // given
        PostEditDto postEditDto = PostEditDto.builder()
                .postId(1L)
                .title("수정된 Mock 제목")
                .content("수정된 Mock 내용")
                .build();

        MockMultipartFile mockImage = new MockMultipartFile(
                "images", "test.png", "image/png", "test image content".getBytes());

        given(postService.postEdit(any(PostEditDto.class), eq("mockUser"), anyList())).willReturn(true);

        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/edit/{postId}", postEditDto.getPostId())
                        .file(mockImage)
                        .param("title", postEditDto.getTitle())
                        .param("content", postEditDto.getContent())
                        .with(SecurityMockMvcRequestPostProcessors.user("mockUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Post 수정에 성공했습니다."))
                .andDo(MockMvcResultHandlers.print());

        // then
        verify(postService, times(1)).postEdit(any(PostEditDto.class), eq("mockUser"), anyList());
    }

    @Test
    @DisplayName("Mock을 사용한 포스트 삭제 테스트")
    void postDeleteWithMockSuccessTest() throws Exception {
        // given
        given(postService.postDelete(eq(1L), eq("mockUser"))).willReturn(true);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("mockUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Post 삭제에 성공했습니다."))
                .andDo(MockMvcResultHandlers.print());

        // then
        verify(postService, times(1)).postDelete(eq(1L), eq("mockUser"));
    }



    @Test
    @DisplayName("Mock을 사용한 포스트 목록 조회 테스트")
    void getPostListWithMockSuccessTest() throws Exception {
        // given
        PostPagingResponse mockResponse = new PostPagingResponse(
                List.of(
                        new PostResponseDto(1L, "Mock 제목 1", "Mock 내용 1", List.of("image1.png")),
                        new PostResponseDto(2L, "Mock 제목 2", "Mock 내용 2", List.of("image2.png"))
                ),
                2L
        );

        given(postService.getList(any(PostSearchDto.class))).willReturn(mockResponse);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("page", "1")
                        .param("size", "10")
                        .with(SecurityMockMvcRequestPostProcessors.user("mockUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.posts.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].title").value("Mock 제목 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.posts[1].title").value("Mock 제목 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPostCount").value(2))
                .andDo(MockMvcResultHandlers.print());

        // then
        verify(postService, times(1)).getList(any(PostSearchDto.class));
    }
}
