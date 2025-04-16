package com.rabbitmqprac.chatmessage.dto;

import com.rabbitmqprac.chatmessage.ChatMessage;
import com.rabbitmqprac.common.constant.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ChatMessageRes extends MessageRes {
    static private final MessageType messageType = MessageType.CHAT_MESSAGE;
    private Long memberId;
    private String username;
    private String message;
    private LocalDateTime createdAt;
    private int unreadCnt;

    private ChatMessageRes(Long memberId, String username, String message, LocalDateTime createdAt, int unreadCnt) {
        super(messageType);
        this.memberId = memberId;
        this.username = username;
        this.message = message;
        this.createdAt = createdAt;
        this.unreadCnt = unreadCnt;
    }

    public static MessageRes createRes(String username, ChatMessage chatMessage, int unreadCnt) {
        return new ChatMessageRes(chatMessage.getMemberId(), username, chatMessage.getMessage(), chatMessage.getCreatedAt(), unreadCnt);
    }
}
