package com.hobbyproject.service;

import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.UploadFile;
import com.hobbyproject.repository.PostRepository;
import com.hobbyproject.repository.UploadFileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UploadFileServiceImplTest {
    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Value("${file.dir}")
    private String fileDir;

    @AfterEach
    void clear(){
        uploadFileRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("파일 업로드 성공")
    void uploadFileSuccessTest() throws Exception {
        Post post = Post.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .build();
        postRepository.save(post);

        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "test.png", "image/png", "test image content".getBytes());
        List<MultipartFile> images = List.of(mockImage);

        uploadFileService.uploadFile(post, images);

        List<UploadFile> uploadFiles = uploadFileRepository.findAll();
        assertEquals(1, uploadFiles.size());
        assertEquals("test.png", uploadFiles.get(0).getUploadFileName());

        File savedFile = new File(fileDir + uploadFiles.get(0).getStoreFileName());
        assertTrue(savedFile.exists());
    }
}