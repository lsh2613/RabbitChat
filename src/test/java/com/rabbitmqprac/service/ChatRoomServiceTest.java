package com.rabbitmqprac.service;

import com.rabbitmqprac.application.dto.chatroom.req.ChatRoomCreateReq;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomSummaryRes;
import com.rabbitmqprac.common.fixture.ChatRoomFixture;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.chatmessage.service.ChatMessageService;
import com.rabbitmqprac.domain.context.chatmessagestatus.service.ChatMessageStatusService;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorCode;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorException;
import com.rabbitmqprac.domain.context.chatroom.service.ChatRoomService;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRepository;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatRoomService 테스트")
class ChatRoomServiceTest {
    @Mock
    private EntityFacade entityFacade;
    @Mock
    private ChatMessageService chatMessageService;
    @Mock
    private ChatRoomMemberService chatRoomMemberService;
    @Mock
    private ChatMessageStatusService chatMessageStatusService;
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private static final UserFixture USER_FIXTURE = UserFixture.FIRST_USER;
    private static final ChatRoomFixture CHAT_ROOM_FIXTURE = ChatRoomFixture.FIRST_CHAT_ROOM;
    private static final User USER = mock(User.class);
    private static final ChatRoom CHAT_ROOM = mock(ChatRoom.class);

    @BeforeEach
    void setUp() {
        given(USER.getId()).willReturn(USER_FIXTURE.getId());
        given(CHAT_ROOM.getId()).willReturn(CHAT_ROOM_FIXTURE.getId());
        given(CHAT_ROOM.getTitle()).willReturn(CHAT_ROOM_FIXTURE.getTitle());
        given(CHAT_ROOM.getMaxCapacity()).willReturn(CHAT_ROOM_FIXTURE.getMaxCapacity());
    }

    @Nested
    @DisplayName("채팅방 생성 시나리오")
    class CreateChatRoomScenario {
        private final ChatRoomCreateReq req = new ChatRoomCreateReq(CHAT_ROOM.getTitle(), CHAT_ROOM.getMaxCapacity());

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("채팅방 생성 성공")
            void createChatRoomSuccess() {
                // given
                given(entityFacade.readUser(USER.getId())).willReturn(USER);
                given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(CHAT_ROOM);

                // when
                ChatRoomDetailRes result = chatRoomService.create(USER.getId(), req);

                // then
                assertThat(result.title()).isEqualTo(CHAT_ROOM.getTitle());
                assertThat(result.maxCapacity()).isEqualTo(CHAT_ROOM.getMaxCapacity());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 유저로 채팅방 생성 시 실패")
            void createChatRoomFailWhenUserNotFound() {
                // given
                given(entityFacade.readUser(USER.getId())).willThrow(new ChatRoomErrorException(ChatRoomErrorCode.NOT_FOUND));

                // when
                ChatRoomErrorException ex = assertThrows(ChatRoomErrorException.class, () -> chatRoomService.create(USER.getId(), req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ChatRoomErrorCode.NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("내 채팅방 목록 조회 시나리오")
    class GetMyChatRoomsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("내 채팅방 목록 조회 성공")
            void getMyChatRoomsSuccess() {
                // given
                given(entityFacade.readUser(USER.getId())).willReturn(USER);
                given(chatRoomMemberService.readChatRoomMembersByUserId(USER.getId())).willReturn(List.of());

                // when
                List<ChatRoomDetailRes> result = chatRoomService.getMyChatRooms(USER.getId());

                // then
                assertThat(result).isNotNull();
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 유저의 채팅방 목록 조회 시 실패")
            void getMyChatRoomsFailWhenUserNotFound() {
                // given
                given(entityFacade.readUser(USER.getId())).willThrow(new ChatRoomErrorException(ChatRoomErrorCode.NOT_FOUND));

                // when
                ChatRoomErrorException ex = assertThrows(ChatRoomErrorException.class, () -> chatRoomService.getMyChatRooms(USER.getId()));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ChatRoomErrorCode.NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("전체 채팅방 목록 조회 시나리오")
    class GetChatRoomsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("비로그인 유저의 전체 채팅방 목록 조회 성공")
            void getChatRoomsSuccessWhenNotLoggedIn() {
                // given
                given(chatRoomRepository.findAll()).willReturn(List.of(CHAT_ROOM));

                // when
                List<ChatRoomSummaryRes> result = chatRoomService.getChatRooms(Optional.ofNullable(null));

                // then
                assertThat(result).isNotNull();
                assertThat(result.size()).isEqualTo(1);
                ChatRoomSummaryRes res = result.getFirst();
                assertThat(res.isJoined()).isFalse();
            }

            @Test
            @DisplayName("로그인 유저의 전체 채팅방 목록 조회 성공")
            void getChatRoomsSuccessWhenLoggedIn() {
                // given
                given(chatRoomRepository.findAll()).willReturn(List.of(CHAT_ROOM));
                given(chatRoomMemberService.isExists(CHAT_ROOM.getId(), USER.getId())).willReturn(true);

                // when
                List<ChatRoomSummaryRes> result = chatRoomService.getChatRooms(Optional.of(USER.getId()));

                // then
                assertThat(result).isNotNull();
                assertThat(result.size()).isEqualTo(1);
                ChatRoomSummaryRes res = result.getFirst();
                assertThat(res.isJoined()).isTrue();
            }
        }
    }
}
