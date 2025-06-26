package com.hobbyproject.jwt;

import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoDto {

    private String loginId;

    private String password;

    private String name;

    private RoleType role;

    @Builder
    public UserInfoDto(Member member) {
        this.loginId = member.getLoginId();
        this.password = member.getPassword();
        this.name = member.getName();
        this.role = member.getRole();
    }
}
