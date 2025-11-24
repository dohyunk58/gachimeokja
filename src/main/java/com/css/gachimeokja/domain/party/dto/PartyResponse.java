package com.css.gachimeokja.domain.party.dto;

import com.css.gachimeokja.domain.party.entity.Party;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "공동구매 상세 정보 응답 DTO")
public class PartyResponse {
    @Schema(description = "공동구매 ID (채팅방 ID 겸용)", example = "1")
    private Long id;

    @Schema(description = "가게 이름", example = "엽기떡볶이")
    private String restaurant;

    @Schema(description = "수령 위치", example = "신공학관 1층")
    private String pickupLocation;

    @Schema(description = "네이버 지도 URL", example = "https://naver.me/...")
    private String naverMapUrl;

    @Schema(description = "현재 모인 금액", example = "15000")
    private Integer currentAmount;

    @Schema(description = "목표 금액", example = "20000")
    private Integer targetAmount;

    @Schema(description = "달성률 (%)", example = "75")
    private int achievementRate;

    @Schema(description = "현재 참여자 수", example = "3")
    private int currentMembers;

    @Schema(description = "모집 상태 (모집중/모집완료)", example = "모집중")
    private String status;

    @Schema(description = "마감 시간", example = "2025-12-31 18:00:00")
    private LocalDateTime endAt;

    @Schema(description = "생성 시간", example = "2025-11-24 10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "개설자 ID", example = "10")
    private Long creatorId;

    @Schema(description = "개설자 닉네임", example = "먹짱123")
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