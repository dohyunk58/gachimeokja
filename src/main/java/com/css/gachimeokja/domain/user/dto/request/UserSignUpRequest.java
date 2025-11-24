package com.css.gachimeokja.domain.user.dto.request;

import com.css.gachimeokja.domain.user.entity.University;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 요청 DTO")
public class UserSignUpRequest {

    @Schema(description = "실명", example = "홍길동")
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String fullName;

    @Schema(description = "닉네임 (중복 불가)", example = "동국대맛집러")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Schema(description = "대학교 이름", example = "DONGGUK")
    @NotBlank(message = "학교는 필수 입력 값입니다.")
    private University university;
}
