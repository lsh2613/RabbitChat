package com.rabbitmqprac.application.api;

import com.rabbitmqprac.application.dto.chatroommember.res.ChatRoomMemberDetailRes;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "[채팅방 멤버 API]")
public interface ChatRoomMemberApi {

    @Operation(summary = "채팅방 참여")
    void joinChatRoom(
            @AuthenticationPrincipal SecurityUserDetails user,
            @PathVariable Long chatRoomId
    );

    @Operation(summary = "채팅방 멤버 목록 조회")
    List<ChatRoomMemberDetailRes> getChatRoomMembers(@PathVariable Long chatRoomId);
}
