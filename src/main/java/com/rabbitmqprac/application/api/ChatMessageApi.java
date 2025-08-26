package com.rabbitmqprac.application.api;

import com.rabbitmqprac.application.dto.chatmessage.req.ChatMessageReq;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageDetailRes;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.infra.security.principal.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "[채팅 메시지 API]")
public interface ChatMessageApi {

    @RequestMapping(method = RequestMethod.OPTIONS)
    @Operation(
            summary = "채팅 메시지 전송 (STOMP 설명용)",
            description = """
                    STOMP를 통해 채팅 메시지를 전송하는 방법을 설명합니다. ※ 이 API는 HTTP가 아닌 WebSocket(STOMP) 기반으로 실제로 동작하지 않습니다.
                    - STOMP Destination: /pub/chat.room.{chatRoomId}/message
                    - Payload: ChatMessageReq {"content": "메시지 내용"}
                    - 인증: Header에 JWT 토큰 필요
                    """
    )
    void sendMessage(UserPrincipal principal,
                     @DestinationVariable Long chatRoomId,
                     @Validated ChatMessageReq message
    );

    @Operation(summary = "채팅 메시지 목록 조회 (이전)", description = "특정 채팅 메시지 ID 이전의 채팅 메시지 목록을 조회한다.")
    @Parameters({
            @Parameter(
                    name = "lastChatMessageId",
                    description = """
                            조회 시작 기준이 되는 채팅 메시지 ID. 이 ID 이전의 메시지들을 조회한다. 
                            기본값은 0이며, 이 경우 가장 최근 메시지부터 조회를 시작한다.
                            """,
                    example = "0"
            ),
            @Parameter(
                    name = "size",
                    description = "한 번에 조회할 채팅 메시지의 최대 개수. 기본값은 30이다.",
                    example = "30"
            )
    })
    List<ChatMessageDetailRes> readChatMessagesBefore(
            @PathVariable Long chatRoomId,
            @RequestParam(value = "lastChatMessageId", defaultValue = "0") Long lastMessageId,
            @RequestParam(value = "size", defaultValue = "30") int size
    );

    @Operation(summary = "채팅 메시지 목록 조회 (범위)")
    @Parameters({
            @Parameter(
                    name = "from",
                    description = "조회할 채팅 메시지 ID 범위의 시작. 이 ID를 포함한다.",
                    example = "100",
                    required = true
            ),
            @Parameter(
                    name = "to",
                    description = "조회할 채팅 메시지 ID 범위의 끝. 이 ID를 포함한다.",
                    example = "200",
                    required = true
            )
    })
    List<ChatMessageDetailRes> readChatMessagesBetween(@AuthenticationPrincipal SecurityUserDetails user,
                                                       @PathVariable Long chatRoomId,
                                                       @RequestParam Long from,
                                                       @RequestParam Long to
    );
}
