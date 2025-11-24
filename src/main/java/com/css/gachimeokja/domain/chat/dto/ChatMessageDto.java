package com.css.gachimeokja.domain.chat.dto;

import com.css.gachimeokja.domain.chat.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채팅 메시지 전송/수신 DTO")
public class ChatMessageDto {
    @Schema(description = "채팅방 ID (공동구매 ID와 동일)", example = "1")
    private Long roomId;

    @Schema(description = "보낸 사람 ID (전송 시엔 안 보내도 됨 - 토큰 사용)", example = "10")
    private Long senderId;

    @Schema(description = "보낸 사람 닉네임", example = "같이먹자")
    private String senderNickname;

    @Schema(description = "메시지 내용", example = "안녕하세요! 공구 참여하고 싶습니다!")
    private String content;

    @Schema(description = "메시지 타입 (ENTER:입장, TALK:대화, LEAVE:퇴장)", example = "TALK")
    private ChatMessage.MessageType type;
}
