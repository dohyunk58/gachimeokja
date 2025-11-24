package com.css.gachimeokja.domain.party.controller;

import com.css.gachimeokja.domain.party.dto.PartyCreateRequest;
import com.css.gachimeokja.domain.party.dto.PartyResponse;
import com.css.gachimeokja.domain.party.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "공동구매 API")
@RestController
@RequestMapping("/api/v1/parties")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @Operation(summary = "공동구매 등록", description = "새로운 공동구매를 생성합니다.")
    @PostMapping
    public ResponseEntity<PartyResponse> createParty(
            @Parameter(hidden = true)
            @AuthenticationPrincipal String socialId, // 토큰에서 ID 추출

            @RequestBody @Valid PartyCreateRequest request
    ) {
        PartyResponse response = partyService.createParty(socialId, request);
        return ResponseEntity.ok(response); // 200 OK와 함께 생성된 정보 반환
    }

    @Operation(summary = "공동구매 정보 수정", description = "개설자만 파티 정보를 수정할 수 있습니다.")
    @PatchMapping("/{partyId}")
    public ResponseEntity<PartyResponse> updateParty(
            @PathVariable Long partyId,
            @AuthenticationPrincipal String socialId,
            @RequestBody @Valid PartyCreateRequest request
    ) {
        PartyResponse response = partyService.updateParty(partyId, socialId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공동구매 모집 마감", description = "개설자가 모집을 강제로 마감합니다. (상태 변경: CLOSED)")
    @PostMapping("/{partyId}/close")
    public ResponseEntity<Void> closeParty(
            @PathVariable Long partyId,
            @AuthenticationPrincipal String socialId
    ) {
        partyService.closeParty(partyId, socialId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공동구매 목록 조회", description = "생성된 모든 공동구매 목록을 최신순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PartyResponse>> getAllParties() {
        return ResponseEntity.ok(partyService.getPartyList());
    }

    @Operation(summary = "공동구매 상세 조회", description = "특정 파티의 상세 정보를 조회합니다.")
    @GetMapping("/{partyId}")
    public ResponseEntity<PartyResponse> getPartyDetail(
            @Parameter(description = "파티 ID", example = "1")
            @PathVariable Long partyId
    ) {
        return ResponseEntity.ok(partyService.getPartyDetail(partyId));
    }

    @Operation(summary = "공동구매 참여", description = "해당 공동구먀에 참여하고 채팅방 ID를 반환받습니다.")
    @PostMapping("/{partyId}/join")
    public ResponseEntity<Long> joinParty(
            @Parameter(description = "공동구매 ID", example = "1")
            @PathVariable Long partyId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String socialId
    ) {
        Long chatRoomId = partyService.joinParty(partyId, socialId);

        return ResponseEntity.ok(chatRoomId);
    }

    @Operation(summary = "참여자 내보내기 (강퇴/탈퇴)", description = "본인이 나가거나, 방장이 특정 참여자를 내보냅니다.")
    @DeleteMapping("/{partyId}/members/{userId}")
    public ResponseEntity<Void> quitParty(
            @Parameter(description = "파티 ID", example = "1")
            @PathVariable Long partyId,

            @Parameter(description = "내보낼 유저 ID", example = "10")
            @PathVariable Long userId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String socialId // 요청자 ID
    ) {
        partyService.quitParty(partyId, socialId, userId);
        return ResponseEntity.ok().build();
    }
}
