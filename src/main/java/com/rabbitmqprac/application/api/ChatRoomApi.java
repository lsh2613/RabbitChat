package com.rabbitmqprac.application.api;

import com.rabbitmqprac.application.dto.chatroom.req.ChatRoomCreateReq;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomSummaryRes;
import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.global.annotation.ApiExceptionExplanation;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "[채팅방 API]")
public interface ChatRoomApi {

    @Operation(summary = "채팅방 생성")
    @ApiExceptionExplanation(
            errorCode = UserErrorCode.class,
            constants = "NOT_FOUND"
    )
    ChatRoomDetailRes create(
            @AuthenticationPrincipal SecurityUserDetails user,
            @RequestBody @Validated ChatRoomCreateReq chatRoomCreateReq
    );

    @Operation(summary = "가입한 채팅방 목록 조회", description = "로그인된 유저의 채팅방 목록을 조회한다.")
    List<ChatRoomDetailRes> getMyChatRooms(@AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "채팅방 목록 조회")
    List<ChatRoomSummaryRes> getChatRooms(@AuthenticationPrincipal SecurityUserDetails user);
}
