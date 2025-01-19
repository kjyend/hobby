package com.hobbyproject.repository.commnet;

import com.hobbyproject.config.QueryDslConfig;
import com.hobbyproject.dto.comment.response.CommentResponseDto;
import com.hobbyproject.entity.Comment;
import com.hobbyproject.entity.DeleteStatus;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.member.MemberRepository;
import com.hobbyproject.repository.post.PostRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Import(QueryDslConfig.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentRepositoryImplTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;


    @BeforeAll
    void setUp() {
        Member member = Member.builder()
                .loginId("user1")
                .name("성능 테스트 사용자")
                .birthday(LocalDate.parse("1990-01-01"))
                .build();
        memberRepository.save(member);

        Post post = Post.builder()
                .title("성능 테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
        postRepository.save(post);

        List<Comment> comments = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            comments.add(Comment.builder()
                    .content("댓글 " + i)
                    .post(post)
                    .member(member)
                    .isDeleted(DeleteStatus.NO)
                    .build());
        }
        commentRepository.saveAll(comments);
    }

    @AfterAll
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("1000개의 댓글 조회 성능 테스트")
    void getCommentListPerformanceTest() {
        Long postId = postRepository.findAll().getFirst().getPostId();

        long startTime = System.currentTimeMillis();
        List<CommentResponseDto> comments = commentRepository.getComment(postId);
        long endTime = System.currentTimeMillis();

        Assertions.assertEquals(1000, comments.size()); // 데이터가 정확히 조회되는지 확인
        System.out.println("조회 시간(ms): " + (endTime - startTime)+"ms");
    }

}