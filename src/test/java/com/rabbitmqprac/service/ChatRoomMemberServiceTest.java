package com.rabbitmqprac.service;

import com.rabbitmqprac.common.fixture.ChatRoomFixture;
import com.rabbitmqprac.common.fixture.ChatRoomMemberFixture;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorCode;
import com.rabbitmqprac.domain.context.chatroom.exception.ChatRoomErrorException;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.repository.ChatRoomMemberRepository;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatRoomMemberService 테스트")
class ChatRoomMemberServiceTest {
    @Mock
    private EntityFacade entityFacade;
    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @InjectMocks
    private ChatRoomMemberService chatRoomMemberService;

    private static final UserFixture USER_FIXTURE = UserFixture.FIRST_USER;
    private static final ChatRoomFixture CHAT_ROOM_FIXTURE = ChatRoomFixture.FIRST_CHAT_ROOM;
    private static final ChatRoomMemberFixture ADMIN_FIXTURE = ChatRoomMemberFixture.ADMIN;
    private static final ChatRoomMemberFixture MEMBER_FIXTURE = ChatRoomMemberFixture.MEMBER;

    private static final User USER = USER_FIXTURE.toEntity();
    private static final ChatRoom CHAT_ROOM = CHAT_ROOM_FIXTURE.toEntity();
    private static final ChatRoomMember CHAT_ROOM_ADMIN = ADMIN_FIXTURE.toEntity(CHAT_ROOM, USER);
    private static final ChatRoomMember CHAT_ROOM_MEMBER = MEMBER_FIXTURE.toEntity(CHAT_ROOM, USER);

    @Nested
    @DisplayName("채팅방 어드민 생성 시나리오")
    class CreateAdminScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("어드민 생성 성공")
            void createAdminSuccess() {
                // given
                given(chatRoomMemberRepository.existsByChatRoomAndUser(CHAT_ROOM, USER)).willReturn(false);

                // when
                chatRoomMemberService.createAdmin(USER, CHAT_ROOM);

                // then
                verify(chatRoomMemberRepository).save(any(ChatRoomMember.class));
            }
        }
        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("이미 가입한 유저가 어드민 생성 요청 시 CONFLICT 예외")
            void createAdminFailByAlreadyJoined() {
                // given
                given(chatRoomMemberRepository.existsByChatRoomAndUser(CHAT_ROOM, USER)).willReturn(true);

                // when
                ChatRoomErrorException ex = assertThrows(ChatRoomErrorException.class, () -> chatRoomMemberService.createAdmin(USER, CHAT_ROOM));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ChatRoomErrorCode.CONFLICT);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 가입 시나리오")
    class JoinChatRoomScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("채팅방 가입 성공")
            void joinChatRoomSuccess() {
                // given
                given(entityFacade.readUser(USER.getId())).willReturn(USER);
                given(entityFacade.readChatRoom(CHAT_ROOM.getId())).willReturn(CHAT_ROOM);
                given(chatRoomMemberRepository.existsByChatRoomAndUser(CHAT_ROOM, USER)).willReturn(false);
                given(chatRoomMemberRepository.save(any(ChatRoomMember.class))).willReturn(CHAT_ROOM_MEMBER);

                // when
                chatRoomMemberService.joinChatRoom(USER.getId(), CHAT_ROOM.getId());

                // then
                verify(chatRoomMemberRepository).save(any(ChatRoomMember.class));
            }
        }
        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("이미 가입한 유저가 채팅방 가입 시도 시 CONFLICT 예외")
            void joinChatRoomFailByAlreadyJoined() {
                // given
                given(entityFacade.readUser(USER.getId())).willReturn(USER);
                given(entityFacade.readChatRoom(CHAT_ROOM.getId())).willReturn(CHAT_ROOM);
                given(chatRoomMemberRepository.existsByChatRoomAndUser(CHAT_ROOM, USER)).willReturn(true);

                // when
                ChatRoomErrorException ex = assertThrows(ChatRoomErrorException.class, () -> chatRoomMemberService.joinChatRoom(USER.getId(), CHAT_ROOM.getId()));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ChatRoomErrorCode.CONFLICT);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 멤버 조회 시나리오")
    class GetChatRoomMembersScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("채팅방 멤버 조회 성공")
            void getChatRoomMembersSuccess() {
                // given
                given(entityFacade.readChatRoom(CHAT_ROOM.getId())).willReturn(CHAT_ROOM);
                given(chatRoomMemberRepository.findAllWithUserByChatRoomId(CHAT_ROOM.getId())).willReturn(List.of(CHAT_ROOM_ADMIN, CHAT_ROOM_MEMBER));

                // when
                var result = chatRoomMemberService.getChatRoomMembers(CHAT_ROOM.getId());

                // then
                assertThat(result).isNotNull();
                assertThat(result.size()).isEqualTo(2);
            }
        }
    }

    @Nested
    @DisplayName("User ID로 채팅방 멤버 조회 시나리오")
    class ReadChatRoomMembersByUserIdScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("User ID로 채팅방 멤버 조회 성공")
            void readChatRoomMembersByUserIdSuccess() {
                // given
                given(chatRoomMemberRepository.findAllByUserId(USER.getId())).willReturn(List.of(CHAT_ROOM_ADMIN, CHAT_ROOM_MEMBER));

                // when
                List<ChatRoomMember> result = chatRoomMemberService.readChatRoomMembersByUserId(USER.getId());

                // then
                assertThat(result).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 인원 조회 시나리오")
    class CountChatRoomMembersScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("채팅방 인원 조회 성공")
            void countChatRoomMembersSuccess() {
                // given
                given(chatRoomMemberRepository.countByChatRoomId(CHAT_ROOM.getId())).willReturn(2);

                // when
                int count = chatRoomMemberService.countChatRoomMembers(CHAT_ROOM.getId());

                // then
                assertThat(count).isEqualTo(2);
            }
        }
    }
}
