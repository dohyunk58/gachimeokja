package com.css.gachimeokja.domain.party.entity;

import com.css.gachimeokja.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parties")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    private String restaurant; // 가게 이름

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "naver_map_url") // 네이버 지도 URL
    private String naverMapUrl;

    @Column(name = "target_amount", nullable = false)
    private Integer targetAmount; // 목표 금액

    @Column(name = "current_amount", nullable = false)
    @ColumnDefault("0")
    private Integer currentAmount; // 현재 금액

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt; // 마감 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyStatus status; // OPEN, CLOSED (모집중, 모집마감)

    // 참여자 목록
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyMember> members = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Builder
    public Party(User creator, String restaurant, String pickupLocation,
                 Integer targetAmount, LocalDateTime endAt, String naverMapUrl) {
        this.creator = creator;
        this.restaurant = restaurant;
        this.pickupLocation = pickupLocation;
        this.targetAmount = targetAmount;
        this.endAt = endAt;
        this.naverMapUrl = naverMapUrl;
        this.currentAmount = 0;
        this.status = PartyStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String restaurant, String pickupLocation, String naverMapUrl,
                       Integer targetAmount, LocalDateTime endAt) {
        this.restaurant = restaurant;
        this.pickupLocation = pickupLocation;
        this.naverMapUrl = naverMapUrl;
        this.targetAmount = targetAmount;
        this.endAt = endAt;
    }

    public void increaseCurrentAmount(Integer amount) {
        this.currentAmount += amount;
    }

    public void decreaseCurrentAmount(Integer amount) {
        this.currentAmount -= amount;
        if (this.currentAmount < 0) this.currentAmount = 0;
    }

    public void closeParty() {
        this.status = PartyStatus.CLOSED;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public enum PartyStatus {
        OPEN,
        CLOSED
    }
}