package com.rabbitmqprac.application.dto.chatmessage.res;

import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.context.chatmessage.constant.MessageType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageRes extends MessageRes {
    static private final MessageType messageType = MessageType.CHAT_MESSAGE;
    private Long memberId;
    private String nickname;
    private String message;
    private LocalDateTime createdAt;
    private int unreadCnt;

    private ChatMessageRes(Long memberId, String nickname, String message, LocalDateTime createdAt, int unreadCnt) {
        super(messageType);
        this.memberId = memberId;
        this.nickname = nickname;
        this.message = message;
        this.createdAt = createdAt;
        this.unreadCnt = unreadCnt;
    }

    public static MessageRes createRes(String nickname, ChatMessage chatMessage, int unreadCnt) {
        return new ChatMessageRes(chatMessage.getUserId(), nickname, chatMessage.getMessage(), chatMessage.getCreatedAt(), unreadCnt);
    }
}
