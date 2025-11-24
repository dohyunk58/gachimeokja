package com.css.gachimeokja.domain.party.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "공동구매 생성 요청 DTO")
public class PartyCreateRequest {

    @Schema(description = "가게 이름", example = "동대문 엽기떡볶이")
    @NotBlank(message = "가게 이름은 필수입니다.")
    private String restaurant;

    @Schema(description = "수령 위치", example = "신공학관 1층")
    @NotBlank(message = "수령 위치는 필수입니다.")
    private String pickupLocation;

    @Schema(description = "목표 금액 (최소 주문 금액)", example = "20000")
    @NotNull(message = "목표 금액은 필수입니다.")
    private Integer targetAmount;

    @Schema(description = "내 주문 금액 (개설자)", example = "5000")
    @NotNull(message = "주문 금액은 필수입니다.")
    private Integer myOrderAmount;

    @Schema(description = "네이버 지도 URL", example = "https://naver.me/5xyz123")
    private String naverMapUrl;

    @Schema(description = "모집 마감 시간 (yyyy-MM-dd HH:mm)", example = "2025-12-31 18:30", type = "string")
    @NotNull(message = "모집 마감 시간은 필수입니다.")
    @Future(message = "마감 시간은 미래여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endAt;
}
