package com.rabbitmqprac.common.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import com.rabbitmqprac.common.constant.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageRes extends MessageRes {
    private Long memberId;
    private String message;
    private LocalDateTime createdAt;
    private int unreadCnt;

    private ChatMessageRes(MessageType messageType, Long memberId, String message, LocalDateTime createdAt, int unreadCnt) {
        super(messageType);
        this.memberId = memberId;
        this.message = message;
        this.createdAt = createdAt;
        this.unreadCnt = unreadCnt;
    }

    public static ChatMessageRes createRes(MessageType messageType, ChatMessage chatMessage, int unreadCnt) {
        return new ChatMessageRes(messageType, chatMessage.getMemberId(), chatMessage.getMessage(), chatMessage.getCreatedAt(), unreadCnt);
    }
}
