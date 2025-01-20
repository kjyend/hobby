package com.hobbyproject.repository.post;


import com.hobbyproject.config.QueryDslConfig;
import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.response.PostListDto;
import com.hobbyproject.entity.Post;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(QueryDslConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeAll
    void setUp() {
        int batchSize = 1000; // 한 번에 처리할 데이터 크기
        List<Post> posts = new ArrayList<>();

        for (long i = 1L; i <= 10_000L; i++) {
            Post post = Post.builder()
                    .postId(i)
                    .content(i + "내용입니다.")
                    .title(i + "제목입니다.")
                    .build();
            posts.add(post);

            if (posts.size() == batchSize) {
                postRepository.saveAll(posts);
                posts.clear();
            }
        }

        if (!posts.isEmpty()) {
            postRepository.saveAll(posts);
        }
    }

    @AfterAll
    void tearDown() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 조회")
    void testFindById() {
        Post post = postRepository.findById(1L).orElseThrow();
        assertEquals("1제목입니다.", post.getTitle());
    }

    @Test
    @DisplayName("게시글을 만들고 방문 횟수 0회 확인")
    void testFindCountByPostId() {
        Long count = postRepository.findCountByPostId(10L);
        assertEquals(0L, count, "Initial count should be 0");
    }

    @Test
    @DisplayName("게시글을 만들고 좋아요 횟수 0회 확인")
    void testFindLikeCountByPostId() {
        Long likeCount = postRepository.findLikeCountByPostId(1L);
        assertEquals(0L, likeCount, "Initial like count should be 0");
    }

    @Test
    @DisplayName("게시글 조회수 500회 설정하고 조회수 확인")
    void testUpdateViewCount() {
        Long postId = 1L;
        Long updatedCount = 500L;

        postRepository.updateViewCount(postId, updatedCount);

        Post updatedPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        assertEquals(updatedCount, updatedPost.getCount(), "Updated count should match");
    }

    @Test
    @DisplayName("게시글 페이징 조회")
    void testGetList() {
        PostSearchDto searchDto = new PostSearchDto(9, 500);

        List<PostListDto> posts = postRepository.getList(searchDto);

        assertEquals(500, posts.size(), "Should return 10 posts");
    }
}
