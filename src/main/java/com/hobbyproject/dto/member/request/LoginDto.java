package com.hobbyproject.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginDto {

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    public LoginDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
