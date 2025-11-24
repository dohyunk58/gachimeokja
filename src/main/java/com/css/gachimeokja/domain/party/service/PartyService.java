package com.css.gachimeokja.domain.party.service;

import com.css.gachimeokja.domain.party.dto.PartyCreateRequest;
import com.css.gachimeokja.domain.party.dto.PartyResponse;
import com.css.gachimeokja.domain.party.entity.Party;
import com.css.gachimeokja.domain.party.entity.PartyMember;
import com.css.gachimeokja.domain.party.entity.PartyStatus;
import com.css.gachimeokja.domain.party.repository.PartyMemberRepository;
import com.css.gachimeokja.domain.party.repository.PartyRepository;
import com.css.gachimeokja.domain.user.entity.User;
import com.css.gachimeokja.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        savedParty.setChatRoomId(savedParty.getId()); // 공동구매 ID와 동일한 ID로 채팅방 ID 생성

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

    // 공동구매 참여
    @Transactional
    public Long joinParty(Long partyId, String socialId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("파티가 존재하지 않습니다."));

        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        // 모집 중인지 확인
        if (party.getStatus() == PartyStatus.CLOSED) {
            throw new IllegalStateException("이미 마감된 파티입니다.");
        }

        // 이미 참여했는지 확인
        if (partyMemberRepository.existsByPartyAndUser(party, user)) {
            return party.getChatRoomId();
        }

        // 참여 처리
        PartyMember member = PartyMember.builder()
                .party(party)
                .user(user)
                .orderAmount(0)
                .build();

        partyMemberRepository.save(member);

        return party.getChatRoomId(); // 채팅방 ID 반환
    }

    public List<PartyResponse> getPartyList() {
        return partyRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PartyResponse::from)
                .collect(Collectors.toList());
    }

    public PartyResponse getPartyDetail(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("파티가 없습니다."));
        return PartyResponse.from(party);
    }

    // 공동구매 탈퇴
    @Transactional
    public void quitParty(Long partyId, String socialId, Long targetUserId) {
        Party party = partyRepository.findById(partyId).orElseThrow();
        User requestUser = userRepository.findBySocialId(socialId).orElseThrow();

        // 삭제 대상 멤버 찾기
        PartyMember member = partyMemberRepository.findByPartyIdAndUserId(partyId, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("참여자가 아닙니다."));

        // 권한 체크
        boolean isSelf = requestUser.getId().equals(targetUserId);
        boolean isCreator = party.getCreator().getId().equals(requestUser.getId());

        // 권한 확인
        if (!isSelf && !isCreator) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        // 개설자 퇴장 불가 처리
        if (isSelf && isCreator) {
            throw new IllegalStateException("방장은 나갈 수 없습니다. 파티를 삭제(마감)해주세요.");
        }

        partyMemberRepository.delete(member);
    }
}