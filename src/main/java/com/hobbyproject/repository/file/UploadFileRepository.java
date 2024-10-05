package com.hobbyproject.repository.file;

import com.hobbyproject.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile,Long> {

}
