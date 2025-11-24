package com.css.gachimeokja.domain.user.service;

import com.css.gachimeokja.domain.user.dto.request.UserSignUpRequest;
import com.css.gachimeokja.domain.user.entity.User;
import com.css.gachimeokja.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 사용자 정보 저장
    @Transactional
    public User signUp(String socialId, UserSignUpRequest request) {
        // socialId 중복 검사
        if (userRepository.findBySocialId(socialId).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // User 엔티티 생성
        User newUser = User.builder()
                .socialId(socialId)
                .fullName(request.getFullName())
                .nickname(request.getNickname())
                .university(request.getUniversity())
                .build();

        // DB 저장 후 ID 반환
        return userRepository.save(newUser);
    }
}
