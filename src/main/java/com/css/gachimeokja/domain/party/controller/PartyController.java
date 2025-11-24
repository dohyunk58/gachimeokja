package com.css.gachimeokja.domain.party.controller;

import com.css.gachimeokja.domain.party.dto.PartyCreateRequest;
import com.css.gachimeokja.domain.party.dto.PartyResponse;
import com.css.gachimeokja.domain.party.service.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
