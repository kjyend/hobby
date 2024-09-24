package com.hobbyproject.service;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.entity.Comment;
import com.hobbyproject.entity.DeleteStatus;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.CommentRepository;
import com.hobbyproject.repository.MemberRepository;
import com.hobbyproject.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceImplTest {


    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clear(){
        commentRepository.deleteAll();
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void commentCreateSuccessTest() {
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        CreatedComment createdComment = CreatedComment.builder()
                .content("댓글 내용")
                .build();

        commentService.commentCreate(createdComment, post.getPostId(), member.getLoginId());

        List<Comment> comments = commentRepository.findAll();
        assertEquals(1, comments.size());
        assertEquals("댓글 내용", comments.get(0).getContent());
        assertEquals(post.getPostId(), comments.get(0).getPost().getPostId());
    }
    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentSuccessTest() {
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("댓글 내용")
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment);

        commentService.deleteComment(comment.getCommentId());

        assertFalse(commentRepository.findById(comment.getCommentId()).isPresent());
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void getListSuccessTest() {
        // given
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        Comment comment1 = Comment.builder()
                .content("댓글1")
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment1);

        Comment comment2 = Comment.builder()
                .content("댓글2")
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment2);

        List<CommentResponseDto> commentList = commentService.getList(post.getPostId());

        // then: 댓글 목록이 제대로 조회되었는지 확인
        assertEquals(2, commentList.size());
        assertEquals("댓글1", commentList.get(0).getContent());
        assertEquals("댓글2", commentList.get(1).getContent());
    }

    @Test
    @DisplayName("댓글 작성자 확인 성공")
    void isCommentOwnerSuccessTest() {
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("댓글 내용")
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment);

        boolean isOwner = commentService.isCommentOwner(comment.getCommentId(), member.getLoginId());

        assertTrue(isOwner);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 생성 시도 실패")
    void createCommentOnNonexistentPostTest() {
        // given
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        CreatedComment createdComment = CreatedComment.builder()
                .content("댓글 내용")
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.commentCreate(createdComment, 9999L, member.getLoginId()); // 존재하지 않는 게시글 ID
        }, "게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 댓글 생성 시도 실패")
    void createCommentWithNonexistentUserTest() {
        // given: 게시글 생성
        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .build();
        postRepository.save(post);

        CreatedComment createdComment = CreatedComment.builder()
                .content("댓글 내용")
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.commentCreate(createdComment, post.getPostId(), "unknownUser"); // 존재하지 않는 사용자
        }, "멤버가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 부모 댓글에 대한 답글 생성 시도 실패")
    void createReplyToNonexistentParentCommentTest() {
        // given: 게시글 및 사용자 생성
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        CreatedComment createdComment = CreatedComment.builder()
                .content("답글 내용")
                .parentId(9999L)  // 존재하지 않는 부모 댓글 ID
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.commentCreate(createdComment, post.getPostId(), member.getLoginId());
        }, "부모 댓글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 시도 실패")
    void deleteNonexistentCommentTest() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(9999L);  // 존재하지 않는 댓글 ID
        }, "댓글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("자식 댓글이 있는 경우 부모 댓글 삭제 처리 성공")
    void deleteParentCommentWithChildrenSuccessTest() {
        // given: 부모 댓글과 자식 댓글 생성
        Member member = Member.builder()
                .loginId("user123")
                .password(passwordEncoder.encode("password"))
                .name("사용자")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        Comment parentComment = Comment.builder()
                .content("부모 댓글")
                .member(member)
                .post(post)
                .build();
        commentRepository.save(parentComment);

        Comment childComment = Comment.builder()
                .content("자식 댓글")
                .member(member)
                .post(post)
                .parent(parentComment)
                .build();
        commentRepository.save(childComment);

        // when: 부모 댓글 삭제 시도
        commentService.deleteComment(parentComment.getCommentId());

        // then: 부모 댓글이 삭제되지 않고 상태만 변경되었는지 확인
        Comment deletedParent = commentRepository.findById(parentComment.getCommentId()).orElseThrow();
        assertEquals(DeleteStatus.YES, deletedParent.getIsDeleted());
    }
}