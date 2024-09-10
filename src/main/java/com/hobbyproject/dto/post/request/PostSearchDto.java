package com.hobbyproject.dto.post.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchDto {

    private int page = 1;
    private int size = 10;

    public long getOffset(){
        return (long) (Math.max(1,page)-1)*Math.min(size,100);
    }
}
