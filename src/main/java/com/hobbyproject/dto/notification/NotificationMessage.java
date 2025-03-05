package com.hobbyproject.dto.notification;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotificationMessage {
    private String postOwner;
    private String content;

    public NotificationMessage(String postOwner, String content) {
        this.postOwner = postOwner;
        this.content = content;
    }

    public String getPostOwner() {
        return postOwner;
    }

    public String getContent() {
        return content;
    }
}
