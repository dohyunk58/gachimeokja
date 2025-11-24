package com.css.gachimeokja.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인/회원가입 응답 DTO")
public class LoginResponseDto {

    @Schema(description = "액세스 토큰 (Bearer)", example = "ey...")
    private String accessToken;

    @Schema(description = "리프레시 토큰", example = "ey...")
    private String refreshToken;

    @Schema(description = "닉네임", example = "같이먹자")
    private String nickname;

    @Schema(description = "신규 회원 여부 (true면 회원가입 페이지로 이동)", example = "false")
    private boolean isNewMember;
}
