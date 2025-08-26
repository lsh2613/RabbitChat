package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.api.ChatRoomApi;
import com.rabbitmqprac.application.dto.chatroom.req.ChatRoomCreateReq;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomInfoRes;
import com.rabbitmqprac.domain.context.chatroom.service.ChatRoomService;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController implements ChatRoomApi {

    private final ChatRoomService chatRoomService;

    @Override
    @PostMapping("/chat-rooms")
    public ChatRoomDetailRes create(@AuthenticationPrincipal SecurityUserDetails user,
                                    @RequestBody @Validated ChatRoomCreateReq chatRoomCreateReq) {
        return chatRoomService.create(user.getUserId(), chatRoomCreateReq);
    }

    @Override
    @GetMapping("/chat-rooms/me")
    public List<ChatRoomDetailRes> getMyChatRooms(@AuthenticationPrincipal SecurityUserDetails user) {
        return chatRoomService.getMyChatRooms(user.getUserId());
    }

    @Override
    @GetMapping("/chat-rooms")
    public List<ChatRoomInfoRes> getChatRooms(@AuthenticationPrincipal SecurityUserDetails user) {
        return chatRoomService.getChatRooms(Optional.ofNullable(Objects.isNull(user) ? null : user.getUserId())
        );
    }
}
