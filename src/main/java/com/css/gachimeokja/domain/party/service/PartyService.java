package com.css.gachimeokja.domain.party.service;

import com.css.gachimeokja.domain.party.dto.PartyCreateRequest;
import com.css.gachimeokja.domain.party.dto.PartyResponse;
import com.css.gachimeokja.domain.party.entity.Party;
import com.css.gachimeokja.domain.party.entity.PartyMember;
import com.css.gachimeokja.domain.party.repository.PartyMemberRepository;
import com.css.gachimeokja.domain.party.repository.PartyRepository;
import com.css.gachimeokja.domain.user.entity.User;
import com.css.gachimeokja.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final UserRepository userRepository;

    // 파티 생성
    @Transactional
    public PartyResponse createParty(String socialId, PartyCreateRequest request) {
        // 개설자 조회
        User creator = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 파티 엔티티 생성
        Party party = Party.builder()
                .creator(creator)
                .restaurant(request.getRestaurant())
                .pickupLocation(request.getPickupLocation())
                .targetAmount(request.getTargetAmount())
                .endAt(request.getEndAt())
                .naverMapUrl(request.getNaverMapUrl())
                .build();

        Party savedParty = partyRepository.save(party);

        // 개설자를 참여자로 등록
        PartyMember creatorMember = PartyMember.builder()
                .party(savedParty)
                .user(creator)
                .orderAmount(request.getMyOrderAmount())
                .build();

        partyMemberRepository.save(creatorMember);

        // 공동구매 현재 금액 업데이트
        savedParty.increaseCurrentAmount(request.getMyOrderAmount());

        return PartyResponse.from(savedParty);
    }

    // 공동구매 수정
    @Transactional
    public PartyResponse updateParty(Long partyId, String socialId, PartyCreateRequest request) {
        // 공동구매 조회
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공동구매가 존재하지 않습니다."));

        // 요청자(User) 조회
        User requestUser = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 권한 확인(파티 생성자와 요청자가 같은지 비교)
        if (!party.getCreator().getId().equals(requestUser.getId())) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        // 엔티티 업데이트
        party.update(
                request.getRestaurant(),
                request.getPickupLocation(),
                request.getNaverMapUrl(),
                request.getTargetAmount(),
                request.getEndAt()
        );

        return PartyResponse.from(party);
    }

    // 공동구매 모집마감 처리
    @Transactional
    public void closeParty(Long partyId, String socialId) {
        Party party = partyRepository.findById(partyId).orElseThrow();
        User requestUser = userRepository.findBySocialId(socialId).orElseThrow();

        if (!party.getCreator().getId().equals(requestUser.getId())) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        party.closeParty();
    }
}