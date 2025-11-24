package com.css.gachimeokja.domain.party.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PartyCreateRequest {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String restaurant;

    @NotBlank(message = "수령 위치는 필수입니다.")
    private String pickupLocation;

    @NotNull(message = "목표 금액은 필수입니다.")
    private Integer targetAmount;

    @NotNull(message = "주문 금액은 필수입니다.")
    private Integer myOrderAmount;

    private String naverMapUrl;

    @NotNull(message = "모집 마감 시간은 필수입니다.")
    @Future(message = "마감 시간은 미래여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endAt;
}
