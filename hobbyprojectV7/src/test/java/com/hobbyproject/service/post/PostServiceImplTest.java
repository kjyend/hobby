package com.hobbyproject.service.post;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.SearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostListDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.member.MemberRepository;
import com.hobbyproject.repository.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void clear(){
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성하기 성공")
    void postWriteSuccessTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        PostWriteDto postWriteDto = PostWriteDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postService.postCreate(postWriteDto,member.getLoginId(),null);

        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);

        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }


    @Test
    @DisplayName("글 수정하기 성공")
    void postEditSuccessTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
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

        boolean result = postService.postEdit(postEditDto, member.getLoginId(),null);

        assertTrue(result);
    }

    @Test
    @DisplayName("다른 member가 글 수정하기 실패")
    void postEditOtherMemberFailTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
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
                .loginId("bbb111")
                .password("ddd444")
                .name("김원회")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();

        memberRepository.save(member2);

        PostEditDto postEditDto = PostEditDto.builder()
                .postId(post.getPostId())
                .title("변경된 제목입니다.")
                .content("변경된 내용입니다.")
                .build();

        boolean result = postService.postEdit(postEditDto, member2.getLoginId(),null);

        assertFalse(result);
    }

    @Test
    @DisplayName("작성된 글이 없어서 글 수정하기 실패")
    void nonPostEditFailTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);

        PostEditDto postEditDto = PostEditDto.builder()
                .postId(1L)
                .title("변경된 제목입니다.")
                .content("변경된 내용입니다.")
                .build();


        boolean result = postService.postEdit(postEditDto, member.getLoginId(),null);

        assertFalse(result);
    }

    @Test
    @DisplayName("글 삭제 성공")
    void postDeleteSuccessTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
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

        postService.postDelete(post.getPostId(), member.getLoginId());

        assertEquals(0L, postRepository.count());
    }

    @Test
    @DisplayName("작성자가 아닌 다른 member가 글 삭제 시도로 실패")
    void postOtherMemberDeleteFailTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
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
                .loginId("bbb111")
                .password("ddd444")
                .name("김원회")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();

        memberRepository.save(member2);

        postService.postDelete(post.getPostId(), member2.getLoginId());

        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("작성된 글이 없어서 글 삭제 실패")
    void nonPostDeleteFailTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);


        boolean result = postService.postDelete(1L, member.getLoginId());

        assertFalse(result);
    }

    @Test
    @DisplayName("내가 쓴 글 확인")
    void myPostCheckSuccessTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
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

        boolean result = postService.postMemberCheck(post.getPostId(), member.getLoginId());

        assertTrue(result);
    }

    @Test
    @DisplayName("내가 쓴 글이 아닌 경우 false")
    void myPostCheckFailTest(){
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
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
                .loginId("bbb111")
                .password("ddd444")
                .name("김원회")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();

        memberRepository.save(member2);

        boolean result = postService.postMemberCheck(post.getPostId(), member2.getLoginId());

        assertFalse(result);
    }

    @Test
    @DisplayName("N+1 문제 발생 여부 확인")
    void checkNPlusOneProblem() {
        Member member = Member.builder()
                .loginId("asd123")
                .password("qqq111")
                .name("김회원")
                .birthday(LocalDate.parse("2000-11-11"))
                .build();

        memberRepository.save(member);
        System.out.println("member = " + member);

        for (int i = 0; i < 10; i++) {
            Post post = Post.builder()
                    .title("Title " + i)
                    .content("Content " + i)
                    .member(member)
                    .build();
            postRepository.save(post);
            System.out.println("post"+i+"= " + post+"("+i+")");
        }

        SearchDto postSearchDto = new SearchDto(0, 10);
        PostPagingResponse response = postService.getList(postSearchDto);
        System.out.println("response = " + response);

        List<PostListDto> posts = response.getPosts();

        for (PostListDto post : posts) {
            System.out.println("post.getTitle() ="+post.getTitle());
        }
    }
}