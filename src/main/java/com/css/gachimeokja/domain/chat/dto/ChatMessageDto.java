package com.css.gachimeokja.domain.chat.dto;

import com.css.gachimeokja.domain.chat.entity.ChatMessage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long roomId; // 채팅방 번호 (공동구매 ID)
    private Long senderId;
    private String senderNickname;
    private String content;
    private ChatMessage.MessageType type;
}
