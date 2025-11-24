package com.css.gachimeokja.domain.user.dto.request;

import com.css.gachimeokja.domain.user.entity.University;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequest {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String fullName;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "학교는 필수 입력 값입니다.")
    private University university;
}
