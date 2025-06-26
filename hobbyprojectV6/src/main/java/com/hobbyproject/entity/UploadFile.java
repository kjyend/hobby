package com.hobbyproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    @Id
    @GeneratedValue
    private Long uploadFileId;
    private String storeFileName;
    private String uploadFileName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public UploadFile(Post post,String uploadFileName, String storeFileName) {
        this.post=post;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}
