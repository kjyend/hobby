package com.hobbyproject.dto.post.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchDto {

    private int page = 1;
    private int size = 10;

    @Builder
    public SearchDto(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public long getOffset(){
        return (long) (Math.max(1,page)-1)*Math.min(size,100);
    }
}
