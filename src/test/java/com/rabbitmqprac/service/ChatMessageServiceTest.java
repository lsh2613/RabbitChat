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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

    private static final User USER = mock(User.class);
    private static final ChatRoom CHAT_ROOM = mock(ChatRoom.class);
    private static final ChatMessage CHAT_MESSAGE = mock(ChatMessage.class);

    @BeforeEach
    void setUp() {
        given(USER.getId()).willReturn(USER_FIXTURE.getId());
        given(CHAT_ROOM.getId()).willReturn(CHAT_ROOM_FIXTURE.getId());
        given(CHAT_MESSAGE.getId()).willReturn(CHAT_MESSAGE_FIXTURE.getId());
        given(CHAT_MESSAGE.getUser()).willReturn(USER);
        given(CHAT_MESSAGE.getChatRoom()).willReturn(CHAT_ROOM);
    }

    @Nested
    @DisplayName("채팅 메시지 전송 시나리오")
    class SendMessageScenario {
        private final ChatMessageReq req = new ChatMessageReq("content");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("채팅 메시지 전송 성공")
            void sendMessageSuccess() {
                // given
                given(entityFacade.readUser(USER.getId())).willReturn(USER);
                given(entityFacade.readChatRoom(CHAT_ROOM.getId())).willReturn(CHAT_ROOM);
                given(chatMessageRepository.save(any(ChatMessage.class))).willReturn(CHAT_MESSAGE);

                // when
                chatMessageService.sendMessage(USER.getId(), CHAT_ROOM.getId(), req);

                // then
                verify(rabbitPublisher).publish(any(Long.class), any(ChatMessageRes.class));
            }
        }
    }

    @Nested
    @DisplayName("채팅 메시지 범위 조회 시나리오")
    class ReadMessageBetweenScenario {
        private final Long from = 2L;
        private final Long to = 10L;

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("안 읽은 메시지가 존재하는 경우")
            void readMessageBetweenSuccessWhenExistsUnReadMessage() {
                // given
                given(chatMessageRepository.findByChatRoomIdAndIdBetween(CHAT_ROOM.getId(), from, to))
                        .willReturn(List.of(CHAT_MESSAGE));
                long lastMessageId = CHAT_ROOM.getId() - 1;
                given(chatMessageStatusService.readLastReadMessageId(USER.getId(), CHAT_ROOM.getId()))
                        .willReturn(lastMessageId);

                // when
                chatMessageService.readChatMessagesBetween(USER.getId(), CHAT_ROOM.getId(), from, to);

                // then
                verify(chatMessageStatusService).saveLastReadMessageId(any(Long.class), any(Long.class), any(Long.class));
            }

            @Test
            @DisplayName("안 읽은 메시지가 존재하지 않는 경우")
            void readMessageBetweenSuccessWhenNotExistsUnReadMessage() {
                // given
                given(chatMessageRepository.findByChatRoomIdAndIdBetween(CHAT_ROOM.getId(), from, to))
                        .willReturn(List.of(CHAT_MESSAGE));
                long lastMessageId = CHAT_ROOM.getId() + 1;
                given(chatMessageStatusService.readLastReadMessageId(USER.getId(), CHAT_ROOM.getId()))
                        .willReturn(lastMessageId);

                // when
                chatMessageService.readChatMessagesBetween(USER.getId(), CHAT_ROOM.getId(), from, to);

                // then
                verify(chatMessageStatusService, never()).saveLastReadMessageId(any(Long.class), any(Long.class), any(Long.class));
            }
        }
    }
}
