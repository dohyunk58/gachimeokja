package com.css.gachimeokja.domain.party.repository;

import com.css.gachimeokja.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findAllByOrderByCreatedAtDesc(); // 공동구매 최신순 조회
}
