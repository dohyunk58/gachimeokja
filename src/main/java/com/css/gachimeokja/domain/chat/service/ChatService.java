package com.css.gachimeokja.domain.chat.service;

import com.css.gachimeokja.domain.chat.dto.ChatMessageDto;
import com.css.gachimeokja.domain.chat.entity.ChatMessage;
import com.css.gachimeokja.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void saveMessage(ChatMessageDto messageDto) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(messageDto.getRoomId())
                .senderId(messageDto.getSenderId())
                .content(messageDto.getContent())
                .type(messageDto.getType()) // ENTER, TALK, LEAVE
                .build();

        chatMessageRepository.save(chatMessage);
    }
}
