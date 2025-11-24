package com.css.gachimeokja.domain.party.repository;

import com.css.gachimeokja.domain.party.entity.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    // TODO: 특정 파티의 참여자 목록 조회 메서드
}
