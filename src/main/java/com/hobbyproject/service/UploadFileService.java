package com.hobbyproject.service;

import com.hobbyproject.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadFileService {
    void uploadFile(Post post, List<MultipartFile> images);
}
