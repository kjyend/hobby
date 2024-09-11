package com.hobbyproject.service;

import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.UploadFile;
import com.hobbyproject.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl implements UploadFileService{

    private final UploadFileRepository uploadFileRepository;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public void uploadFile(Post post, List<MultipartFile> images) {
        try {
            for (MultipartFile image : images) {
                String storeFileName = saveImage(image);

                UploadFile file = UploadFile.builder()
                        .uploadFileName(image.getOriginalFilename())
                        .storeFileName(storeFileName)
                        .post(post)
                        .build();

                uploadFileRepository.save(file);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uuid = UUID.randomUUID().toString()+"."+image.getOriginalFilename();
        String dbPath=fileDir+uuid;
        image.transferTo(new File(fileDir+image.getOriginalFilename()));

        return dbPath;
    }
}
