package com.css.gachimeokja.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String socialId;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private University university;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public User(String socialId, String fullName, String nickname, University university) {
        this.socialId = socialId;
        this.fullName = fullName;
        this.nickname = nickname;
        this.university = university;
    }

    // 메서드
    // 닉네임 변경
    public void updateNickname(String newNickname) {
        if (newNickname == null || newNickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
        this.nickname = newNickname;
    }
}
