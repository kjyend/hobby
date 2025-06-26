package com.hobbyproject.dto.post.response;

import com.hobbyproject.entity.Post;
import com.hobbyproject.entity.UploadFile;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long count;
    private Long likeCount;
    private List<String> imageUrls;
    private String createTime;
    private String modifyTime;


    public PostResponseDto(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.count = post.getCount();
        this.likeCount = post.getLikeCount();
        this.imageUrls = post.getUploadFiles().stream()
                .map(UploadFile::getStoreFileName)
                .collect(Collectors.toList());
        this.createTime = post.getCreatedDate();
        this.modifyTime = post.getModifiedDate();
    }

    @Builder
    public PostResponseDto(Long id, String title, String content, List<String> imageUrls) {
        this.id = id;
        this.title = title.substring(0,Math.min(title.length(),10));
        this.content = content;
        this.imageUrls = imageUrls;
    }
}
