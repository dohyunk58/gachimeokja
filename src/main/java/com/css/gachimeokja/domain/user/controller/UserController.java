package com.css.gachimeokja.domain.user.controller;

import com.css.gachimeokja.domain.user.dto.request.UserSignUpRequest;
import com.css.gachimeokja.domain.user.entity.University;
import com.css.gachimeokja.domain.user.entity.User;
import com.css.gachimeokja.domain.user.service.UserService;
import com.css.gachimeokja.security.dto.LoginResponseDto;
import com.css.gachimeokja.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 OAuth 로그인 후 최초 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
        @AuthenticationPrincipal String socialId,
        @RequestBody @Valid UserSignUpRequest request) {

        try {
            // 회원 가입 후 User 객체 반환
            User savedUser = userService.signUp(socialId, request);

            // 성공 후 최종 JWT 토큰 발급
            String accessToken = jwtTokenProvider.createAccessToken(socialId);
            String refreshToken = jwtTokenProvider.createRefreshToken(socialId);

            // 응답 생성
            LoginResponseDto tokenResponse = LoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .nickname(savedUser.getNickname())
                    .build();

            return ResponseEntity.ok(tokenResponse);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 에러 발생 시 토큰을 반환하지 않음
        }
    }

    // 회원가입 시 대학 목록 반환
    @GetMapping("/universities")
    public ResponseEntity<List<Map<String, String>>> getUniversities() {
        // Enum을 순회하며 JSON 리스트로 변환
        List<Map<String, String>> universities = Arrays.stream(University.values())
                .map(univ -> Map.of(
                        "code", univ.name(),          // "DONGGUK" (서버로 보낼 값)
                        "name", univ.getKoreanName()  // "동국대학교" (화면에 보여줄 값)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(universities);
    }
}