package com.rabbitmqprac.service;

import com.rabbitmqprac.application.dto.chatmessage.req.ChatMessageReq;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageRes;
import com.rabbitmqprac.common.fixture.ChatMessageFixture;
import com.rabbitmqprac.common.fixture.ChatRoomFixture;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.chatmessage.service.ChatMessageService;
import com.rabbitmqprac.domain.context.chatmessagestatus.service.ChatMessageStatusService;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.context.usersession.service.UserSessionService;
import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatmessage.repository.ChatMessageRepository;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.helper.RabbitPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatMessageService 테스트")
public class ChatMessageServiceTest {
    @Mock
    private EntityFacade entityFacade;
    @Mock
    private ChatRoomMemberService chatRoomMemberService;
    @Mock
    private ChatMessageStatusService chatMessageStatusService;
    @Mock
    private UserSessionService userSessionService;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private RabbitPublisher rabbitPublisher;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private static final UserFixture USER_FIXTURE = UserFixture.FIRST_USER;
    private static final ChatRoomFixture CHAT_ROOM_FIXTURE = ChatRoomFixture.FIRST_CHAT_ROOM;
    private static final ChatMessageFixture CHAT_MESSAGE_FIXTURE = ChatMessageFixture.FIRST_CHAT_MESSAGE;

    private User user = mock(User.class);
    private ChatRoom chatRoom = mock(ChatRoom.class);
    private ChatMessage chatMessage = mock(ChatMessage.class);

    @BeforeEach
    void setUp() {
        given(user.getId()).willReturn(USER_FIXTURE.getId());
        given(chatRoom.getId()).willReturn(CHAT_ROOM_FIXTURE.getId());

        given(chatMessage.getId()).willReturn(CHAT_MESSAGE_FIXTURE.getId());
        given(chatMessage.getUser()).willReturn(user);
        given(chatMessage.getChatRoom()).willReturn(chatRoom);
    }

    @Nested
    @DisplayName("채팅 메시지 전송 시나리오")
    class SendMessageScenario {
        @Nested
        @DisplayName("채팅 메시지 전송 성공 시나리오")
        class SendMessageSuccessScenario {
            @Test
            @DisplayName("성공")
            void sendMessageSuccess() {
                // given
                ChatMessageReq req = new ChatMessageReq("content");
                given(entityFacade.readUser(user.getId())).willReturn(user);
                given(entityFacade.readChatRoom(chatRoom.getId())).willReturn(chatRoom);
                given(chatMessageRepository.save(any(ChatMessage.class))).willReturn(chatMessage);

                // when
                chatMessageService.sendMessage(user.getId(), chatRoom.getId(), req);

                // then
                verify(rabbitPublisher).publish(any(Long.class), any(ChatMessageRes.class));
            }
        }
    }

    @Nested
    @DisplayName("채팅 메시지 범위 조회 시나리오")
    class ReadMessageBetweenScenario {
        @Nested
        @DisplayName("채팅 메시지 범위 조회 성공 시나리오")
        class ReadMessageBetweenSuccessScenario {
            @Test
            @DisplayName("안 읽은 메시지가 존재하는 경우")
            void readMessageBetweenSuccessWhenExistsUnReadMessage() {
                // given
                final Long from = 2L;
                final Long to = 10L;
                final Long chatRoomId = CHAT_ROOM_FIXTURE.getId(); // 미리 값을 저장

                given(chatMessageRepository.findByChatRoomIdAndIdBetween(user.getId(), from, to))
                        .willReturn(List.of(chatMessage));

                given(chatMessageStatusService.readLastReadMessageId(user.getId(), chatRoomId))
                        .willReturn(chatRoomId - 1); // 직접 값 사용

                // when
                chatMessageService.readChatMessagesBetween(user.getId(), chatRoomId, from, to);

                // then
                verify(chatMessageStatusService).saveLastReadMessageId(any(Long.class), any(Long.class), any(Long.class));
            }

            @Test
            @DisplayName("안 읽은 메시지가 존재하지 않는 경우")
            void readMessageBetweenSuccessWhenNotExistsUnReadMessage() {
                // given
                final Long from = 2L;
                final Long to = 10L;
                final Long chatRoomId = CHAT_ROOM_FIXTURE.getId(); // 미리 값을 저장

                given(chatMessageRepository.findByChatRoomIdAndIdBetween(user.getId(), from, to))
                        .willReturn(List.of(chatMessage));

                given(chatMessageStatusService.readLastReadMessageId(user.getId(), chatRoomId))
                        .willReturn(chatRoomId + 1); // 직접 값 사용

                // when
                chatMessageService.readChatMessagesBetween(user.getId(), chatRoomId, from, to);

                // then
                verify(chatMessageStatusService, never()).saveLastReadMessageId(any(Long.class), any(Long.class), any(Long.class));
            }
        }
    }
}
