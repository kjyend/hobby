package com.hobbyproject.controller.post;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.SearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.service.post.PostService;
import com.hobbyproject.service.file.UploadFileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;
    private final UploadFileService uploadFileService;

    @GetMapping("/posts")
    public PostPagingResponse getList(@RequestParam(name = "page",defaultValue = "1") int page,
                                      @RequestParam(name = "size",defaultValue = "10") int size) {
        SearchDto postSearch = new SearchDto(page, size);
        return postService.getList(postSearch);
    }

    @PostMapping("/post/write")
    public void postWrite(@Valid @RequestParam("images") List<MultipartFile> images,@Valid @ModelAttribute PostWriteDto postWriteDto, BindingResult bindingResult,@AuthenticationPrincipal UserDetails userDetails){
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("유효성 검사 실패");
        }

        postService.postCreate(postWriteDto,userDetails.getUsername(),images);
    }

    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + uploadFileService.getFullPath(filename));
    }

    @PostMapping("/post/edit/{postId}")
    public ResponseEntity<String> postEdit(@PathVariable("postId") Long postId, @RequestParam("images") List<MultipartFile> images, @Valid @ModelAttribute PostEditDto postEditDto, BindingResult bindingResult,@AuthenticationPrincipal UserDetails userDetails){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("유효성 검사에 실패했습니다.");
        }

        if(postService.postEdit(postEditDto, userDetails.getUsername(), images)){
            return ResponseEntity.ok("Post 수정에 성공했습니다.");
        }else{
            return ResponseEntity.badRequest().body("Post 수정에 실패했습니다.");
        }
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> postDelete(@PathVariable("postId") Long postId,@AuthenticationPrincipal UserDetails userDetails){
        if (postService.postDelete(postId, userDetails.getUsername())) {
            return ResponseEntity.ok("Post 삭제에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("Post 삭제에 실패했습니다.");
        }
    }

    @PostMapping("/post/{postId}/count")
    public void postViewCount(@PathVariable("postId") Long postId, HttpServletRequest req, HttpServletResponse res){
        viewCountUp(postId, req, res);
    }

    private void viewCountUp(Long id, HttpServletRequest req, HttpServletResponse res) {
        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("count")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[ " + id.toString() + " ]")) {
                postService.incrementViewCount(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            postService.incrementViewCount(id);
            Cookie newCookie = new Cookie("count","[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }
    }
}
