package com.hobbyproject.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.entity.DeleteStatus;
import com.hobbyproject.service.comment.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentRestMockControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Mock을 사용한 댓글 추가 테스트")
    void addCommentWithMockSuccessTest() throws Exception {
        // given
        CreatedComment createdComment = CreatedComment.builder()
                .content("새로운 Mock 댓글")
                .build();

        // when
        willDoNothing().given(commentService).commentCreate(any(CreatedComment.class), anyLong(), anyString());

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/post/{postId}/comment", 1L)
                        .content(new ObjectMapper().writeValueAsString(createdComment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // verify
        verify(commentService, times(1)).commentCreate(any(CreatedComment.class), eq(1L), eq("testUser"));
    }

    @Test
    @DisplayName("Mock을 사용한 댓글 소유자 확인 테스트")
    void isCommentOwnerWithMockTest() throws Exception {
        // given
        given(commentService.isCommentOwner(anyLong(), eq("testUser"))).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}/comment/{commentId}", 1L, 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // verify
        verify(commentService, times(1)).isCommentOwner(eq(1L), eq("testUser"));
    }

    @Test
    @DisplayName("삭제된 댓글 조회 시 처리")
    void getDeletedCommentTest() throws Exception {
        // given
        List<CommentResponseDto> comments = List.of(
                CommentResponseDto.builder()
                        .id(1L)
                        .content("삭제된 댓글입니다.")
                        .isDeleted(DeleteStatus.YES)
                        .name("사용자")
                        .build()
        );

        given(commentService.getList(anyLong())).willReturn(comments);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/post/{postId}/comment", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("삭제된 댓글입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("댓글 목록 조회 - 서비스 Mock")
    void getCommentListTest() throws Exception {
        // given
        List<CommentResponseDto> mockComments = List.of(
                CommentResponseDto.builder().content("Mock 댓글 1").build(),
                CommentResponseDto.builder().content("Mock 댓글 2").build()
        );
        given(commentService.getList(anyLong())).willReturn(mockComments);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/post/{postId}/comment", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Mock 댓글 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("Mock 댓글 2"))
                .andDo(MockMvcResultHandlers.print());

        // then
        verify(commentService, times(1)).getList(anyLong());
    }

    @Test
    @DisplayName("댓글 삭제 시 서비스의 deleteComment가 호출되었는지 검증")
    void deleteCommentTest() throws Exception {
        // given
        given(commentService.isCommentOwner(anyLong(), anyString())).willReturn(true);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}/comment/{commentId}", 1L, 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        verify(commentService, times(1)).deleteComment(1L);
    }
}
