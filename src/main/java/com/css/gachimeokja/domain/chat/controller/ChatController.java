package com.css.gachimeokja.domain.chat.controller;

import com.css.gachimeokja.domain.chat.dto.ChatMessageDto;
import com.css.gachimeokja.domain.chat.service.ChatService;
import com.css.gachimeokja.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    // /pub/chat/message
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, @Header("Authorization") String token) {

        // 토큰에서 유저 ID 추출 후 검증
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = Long.parseLong(jwtTokenProvider.getUserId(token));

        message.setSenderId(userId);

        // 메시지 저장
        chatService.saveMessage(message);

        // 발송 (/sub/chat/room/{roomId})
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
