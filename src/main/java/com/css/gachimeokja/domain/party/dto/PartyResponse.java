package com.css.gachimeokja.domain.party.dto;

import com.css.gachimeokja.domain.party.entity.Party;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PartyResponse {
    private Long id;
    private String restaurant;
    private String pickupLocation;
    private String naverMapUrl;

    private Integer currentAmount;
    private Integer targetAmount;
    private int achievementRate; // 달성률 (%)

    private int currentMembers; // 참여자 수
    private String status;      // "모집중", "모집마감"

    private LocalDateTime endAt;
    private LocalDateTime createdAt;

    private Long creatorId;
    private String creatorNickname;
    private Long chatRoomId;

    public static PartyResponse from(Party party) {
        // 달성률 계산 (0원으로 나누기 방지)
        int rate = 0;
        if (party.getTargetAmount() > 0 && party.getCurrentAmount() > 0) {
            rate = (int) ((double) party.getCurrentAmount() / party.getTargetAmount() * 100);
            if (rate > 100) rate = 100;
        }

        return PartyResponse.builder()
                .id(party.getId())
                .restaurant(party.getRestaurant())
                .pickupLocation(party.getPickupLocation())
                .naverMapUrl(party.getNaverMapUrl())
                .currentAmount(party.getCurrentAmount())
                .targetAmount(party.getTargetAmount())
                .achievementRate(rate)
                .currentMembers(party.getMembers().size()) // 리스트 크기로 참여자 수 확인
                .status(party.getStatus() == Party.PartyStatus.OPEN ? "모집중" : "모집마감")
                .endAt(party.getEndAt())
                .createdAt(party.getCreatedAt())
                .creatorId(party.getCreator().getId())
                .creatorNickname(party.getCreator().getNickname())
                // .chatRoomId(party.getChatRoomId()) // 채팅 기능 붙인 후 주석 해제
                .build();
    }
}