package com.css.gachimeokja.domain.party.controller;

import com.css.gachimeokja.domain.party.dto.PartyCreateRequest;
import com.css.gachimeokja.domain.party.dto.PartyResponse;
import com.css.gachimeokja.domain.party.service.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parties")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    // 공동구매 등록
    @PostMapping
    public ResponseEntity<PartyResponse> createParty(
            @AuthenticationPrincipal String socialId, // 토큰에서 ID 추출
            @RequestBody @Valid PartyCreateRequest request
    ) {
        PartyResponse response = partyService.createParty(socialId, request);
        return ResponseEntity.ok(response); // 200 OK와 함께 생성된 정보 반환
    }

    // 공동구매 수정
    @PatchMapping("/{partyId}")
    public ResponseEntity<PartyResponse> updateParty(
            @PathVariable Long partyId,
            @AuthenticationPrincipal String socialId,
            @RequestBody @Valid PartyCreateRequest request
    ) {
        PartyResponse response = partyService.updateParty(partyId, socialId, request);
        return ResponseEntity.ok(response);
    }

    // 공동구매 마감
    @PostMapping("/{partyId}/close")
    public ResponseEntity<Void> closeParty(
            @PathVariable Long partyId,
            @AuthenticationPrincipal String socialId
    ) {
        partyService.closeParty(partyId, socialId);
        return ResponseEntity.ok().build();
    }

    // 공동구매 목록 조회
    @GetMapping
    public ResponseEntity<List<PartyResponse>> getAllParties() {
        return ResponseEntity.ok(partyService.getPartyList());
    }

    // 공동구매 상세 조회
    @GetMapping("/{partyId}")
    public ResponseEntity<PartyResponse> getPartyDetail(@PathVariable Long partyId) {
        return ResponseEntity.ok(partyService.getPartyDetail(partyId));
    }

    // 공동구매 참여
    @PostMapping("/{partyId}/join")
    public ResponseEntity<Long> joinParty(
            @PathVariable Long partyId,
            @AuthenticationPrincipal String socialId
    ) {
        Long chatRoomId = partyService.joinParty(partyId, socialId);

        return ResponseEntity.ok(chatRoomId);
    }

    // 공동구매에서 나가기
    @DeleteMapping("/{partyId}/members/{userId}")
    public ResponseEntity<Void> quitParty(
            @PathVariable Long partyId,
            @PathVariable Long userId, // 내보낼 대상 ID
            @AuthenticationPrincipal String socialId // 요청자 ID
    ) {
        partyService.quitParty(partyId, socialId, userId);
        return ResponseEntity.ok().build();
    }
}
