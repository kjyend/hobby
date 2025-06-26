package com.hobbyproject.service.file;

import com.hobbyproject.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadFileService {

    String getFullPath(String filename);
    void uploadFile(Post post, List<MultipartFile> images);
}
