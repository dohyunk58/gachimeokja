package com.css.gachimeokja.domain.chat.repository;

import com.css.gachimeokja.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 채팅 내역 최신순으로 불러오기
    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
}
