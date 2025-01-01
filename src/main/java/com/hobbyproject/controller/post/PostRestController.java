package com.hobbyproject.controller.post;

import com.hobbyproject.dto.post.request.PostEditDto;
import com.hobbyproject.dto.post.request.PostSearchDto;
import com.hobbyproject.dto.post.request.PostWriteDto;
import com.hobbyproject.dto.post.response.PostPagingResponse;
import com.hobbyproject.service.post.PostService;
import com.hobbyproject.service.file.UploadFileService;
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
        PostSearchDto postSearch = new PostSearchDto(page, size);
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

}
