package com.hobbyproject.service.file;

import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.UploadFile;
import com.hobbyproject.repository.file.UploadFileRepository;
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

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

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
        String ext =extractExt(image.getOriginalFilename());
        String store = UUID.randomUUID().toString()+"."+ext;
        String dbPath= getFullPath(store);
        image.transferTo(new File(dbPath));

        return store;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
