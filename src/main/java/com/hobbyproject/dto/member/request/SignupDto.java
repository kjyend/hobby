package com.hobbyproject.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignupDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    @NotBlank(message = "확인 비밀번호는 필수입니다.")
    private String checkPassword;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthday;

    @Builder
    public SignupDto(String loginId, String password, String checkPassword, String name, LocalDate birthday) {
        this.loginId = loginId;
        this.password = password;
        this.checkPassword = checkPassword;
        this.name = name;
        this.birthday = birthday;
    }
}
