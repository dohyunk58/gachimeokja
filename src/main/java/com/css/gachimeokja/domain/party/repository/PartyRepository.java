package com.css.gachimeokja.domain.party.repository;

import com.css.gachimeokja.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
    // TODO: 최신순 정렬 메서드
}
