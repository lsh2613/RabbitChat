package com.rabbitmqprac.service;

import com.rabbitmqprac.application.dto.chatroom.req.ChatRoomCreateReq;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomInfoRes;
import com.rabbitmqprac.common.fixture.ChatRoomFixture;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.chatmessage.service.ChatMessageService;
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
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private static final UserFixture USER_FIXTURE = UserFixture.FIRST_USER;
    private static final ChatRoomFixture CHAT_ROOM_FIXTURE = ChatRoomFixture.FIRST_CHAT_ROOM;

    private User user = mock(User.class);
    private ChatRoom chatRoom = mock(ChatRoom.class);

    @BeforeEach
    void setUp() {
        given(user.getId()).willReturn(USER_FIXTURE.getId());
        given(chatRoom.getId()).willReturn(CHAT_ROOM_FIXTURE.getId());
    }

    @Nested
    @DisplayName("채팅방 생성 성공 시나리오")
    class CreateChatRoomSuccessScenarios {
        @Test
        @DisplayName("채팅방 생성 성공")
        void createChatRoom() {
            // given
            ChatRoomCreateReq req = new ChatRoomCreateReq(chatRoom.getTitle(), chatRoom.getMaxCapacity());
            given(entityFacade.readUser(user.getId())).willReturn(user);
            given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

            // when
            ChatRoomDetailRes result = chatRoomService.create(user.getId(), req);

            // then
            assertThat(result.title()).isEqualTo(chatRoom.getTitle());
            assertThat(result.maxCapacity()).isEqualTo(chatRoom.getMaxCapacity());
        }
    }

    @Nested
    @DisplayName("채팅방 생성 실패 시나리오")
    class CreateChatRoomFailScenarios {
        @Test
        @DisplayName("존재하지 않는 유저로 채팅방 생성")
        void createChatRoomWhenUserNotFound() {
            // given
            ChatRoomCreateReq req = new ChatRoomCreateReq(chatRoom.getTitle(), chatRoom.getMaxCapacity());
            given(entityFacade.readUser(user.getId())).willThrow(new ChatRoomErrorException(ChatRoomErrorCode.NOT_FOUND));

            // when
            ChatRoomErrorException e = assertThrows(ChatRoomErrorException.class, () -> chatRoomService.create(user.getId(), req));

            // then
            assertThat(e.getErrorCode()).isEqualTo(ChatRoomErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("내 채팅방 목록 조회 성공 시나리오")
    class GetMyChatRoomsSuccessScenarios {
        @Test
        @DisplayName("내 채팅방 목록 조회 성공")
        void getMyChatRooms() {
            // given
            given(entityFacade.readUser(user.getId())).willReturn(user);
            given(chatRoomMemberService.readChatRoomMembersByUserId(user.getId())).willReturn(List.of());

            // when
            List<ChatRoomDetailRes> result = chatRoomService.getMyChatRooms(user.getId());

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("내 채팅방 목록 조회 실패 시나리오")
    class GetMyChatRoomsFailScenarios {
        @Test
        @DisplayName("존재하지 않는 유저의 채팅방 목록 조회")
        void getMyChatRoomsWhenUserNotFound() {
            // given
            given(entityFacade.readUser(user.getId())).willThrow(new ChatRoomErrorException(ChatRoomErrorCode.NOT_FOUND));

            // when
            ChatRoomErrorException e = assertThrows(ChatRoomErrorException.class, () -> chatRoomService.getMyChatRooms(user.getId()));

            // then
            assertThat(e.getErrorCode()).isEqualTo(ChatRoomErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("전체 채팅방 목록 조회 성공 시나리오")
    class GetChatRoomsSuccessScenarios {
        @Test
        @DisplayName("비로그인 유저의 전체 채팅방 목록 조회 성공")
        void getChatRoomsWhenNotLoggedIn() {
            // given
            given(chatRoomRepository.findAll()).willReturn(List.of(chatRoom));

            // when
            List<ChatRoomInfoRes> result = chatRoomService.getChatRooms(Optional.ofNullable(null));

            // then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(1);

            ChatRoomInfoRes res = result.getFirst();
            assertThat(res.isJoined()).isFalse();
        }

        @Test
        @DisplayName("로그인 유저의 전체 채팅방 목록 조회 성공")
        void getChatRoomsWhenLoggedIn() {
            // given
            given(chatRoomRepository.findAll()).willReturn(List.of(chatRoom));
            given(chatRoomMemberService.isExists(chatRoom.getId(), user.getId())).willReturn(true);

            // when
            List<ChatRoomInfoRes> result = chatRoomService.getChatRooms(Optional.of(user.getId()));

            // then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(1);

            ChatRoomInfoRes res = result.getFirst();
            assertThat(res.isJoined()).isTrue();
        }
    }
}