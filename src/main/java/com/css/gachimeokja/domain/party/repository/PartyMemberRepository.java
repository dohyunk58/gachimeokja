package com.css.gachimeokja.domain.party.repository;

import com.css.gachimeokja.domain.party.entity.Party;
import com.css.gachimeokja.domain.party.entity.PartyMember;
import com.css.gachimeokja.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    // 이미 참여한 사람인지 조회
    boolean existsByPartyAndUser(Party party, User user);

    // 탈퇴 및 강퇴 처리를 위한 멤버 엔티티 조회
    Optional<PartyMember> findByPartyIdAndUserId(Long partyId, Long userId);
}
