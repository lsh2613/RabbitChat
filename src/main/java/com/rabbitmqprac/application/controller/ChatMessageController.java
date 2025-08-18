package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.dto.chatmessage.req.ChatMessageReq;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageDetailRes;
import com.rabbitmqprac.domain.context.chatmessage.service.ChatMessageService;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.infra.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Destination Queue: /pub/chat.message.{chatRoomId}를 통해 호출 후 처리 되는 로직
     */
    @PreAuthorize("#chatRoomAccessChecker.hasPermission(#chatRoomId, principal)")
    @MessageMapping("chat.room.{chatRoomId}/message")
    public void sendMessage(UserPrincipal principal,
                            @DestinationVariable Long chatRoomId,
                            @Validated ChatMessageReq message) {
        chatMessageService.sendMessage(principal.getUserId(), chatRoomId, message);
    }

    @GetMapping("/chat-rooms/{chatRoomId}/messages/before")
    public List<ChatMessageDetailRes> readChatMessagesBefore(@PathVariable Long chatRoomId,
                                                             @RequestParam(value = "lastChatMessageId", defaultValue = "0") Long lastMessageId,
                                                             @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return chatMessageService.readChatMessagesBefore(chatRoomId, lastMessageId, size);
    }

    @GetMapping("/chat-rooms/{chatRoomId}/messages/between")
    public List<ChatMessageDetailRes> readChatMessagesBetween(@AuthenticationPrincipal SecurityUserDetails user,
                                                              @PathVariable Long chatRoomId,
                                                              @RequestParam Long from,
                                                              @RequestParam Long to
    ) {
        return chatMessageService.readChatMessagesBetween(user.getUserId(), chatRoomId, from, to);
    }
}
