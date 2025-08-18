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

    private static final User user = UserFixture.FIRST_USER.toEntity();
    private static final ChatRoom chatRoom = ChatRoomFixture.FIRST_CHAT_ROOM.toEntity();
    private static final ChatRoomMember chatRoomAdmin = ChatRoomMemberFixture.ADMIN.toEntity(chatRoom, user);
    private static final ChatRoomMember chatRoomMember = ChatRoomMemberFixture.MEMBER.toEntity(chatRoom, user);

    @Nested
    @DisplayName("채팅방 어드민 생성 시나리오")
    class CreateAdminScenarios {
        @Nested
        @DisplayName("채팅방 어드민 생성 성공 시나리오")
        class CreateAdminSuccessScenarios {
            @Test
            @DisplayName("성공")
            void createAdminSuccess() {
                // given
                given(chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user)).willReturn(Boolean.FALSE);

                // when
                chatRoomMemberService.createAdmin(user, chatRoom);

                // then
                verify(chatRoomMemberRepository).save(any(ChatRoomMember.class));
            }
        }
        @Nested
        @DisplayName("채팅방 어드민 생성 실패 시나리오")
        class CreateAdminFailScenarios {
            @Test
            @DisplayName("이미 가입한 유저가 가입 요청 시 CONFLICT 예외")
            void createAdminFailByAlreadyJoined() {
                // given
                given(chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user)).willReturn(true);

                // when
                ChatRoomErrorException ex = assertThrows(ChatRoomErrorException.class, () -> chatRoomMemberService.createAdmin(user, chatRoom));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ChatRoomErrorCode.CONFLICT);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 가입 시나리오")
    class JoinChatRoomScenarios {
        @Nested
        @DisplayName("채팅방 가입 성공 시나리오")
        class JoinChatRoomSuccessScenarios {
            @Test
            @DisplayName("성공")
            void joinChatRoomSuccess() {
                // given
                given(entityFacade.readUser(user.getId())).willReturn(user);
                given(entityFacade.readChatRoom(chatRoom.getId())).willReturn(chatRoom);
                given(chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user)).willReturn(false);
                given(chatRoomMemberRepository.save(any(ChatRoomMember.class))).willReturn(chatRoomMember);

                // when
                chatRoomMemberService.joinChatRoom(user.getId(), chatRoom.getId());

                // then
                verify(chatRoomMemberRepository).save(any(ChatRoomMember.class));
            }
        }
        @Nested
        @DisplayName("채팅방 가입 실패 시나리오")
        class JoinChatRoomFailScenarios {
            @Test
            @DisplayName("이미 가입한 유저가 채팅방 가입 시도 시 CONFLICT 예외")
            void joinChatRoomFailByAlreadyJoined() {
                // given
                given(entityFacade.readUser(user.getId())).willReturn(user);
                given(entityFacade.readChatRoom(chatRoom.getId())).willReturn(chatRoom);
                given(chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user)).willReturn(true);

                // when
                ChatRoomErrorException ex = assertThrows(ChatRoomErrorException.class, () -> chatRoomMemberService.joinChatRoom(user.getId(), chatRoom.getId()));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(ChatRoomErrorCode.CONFLICT);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 멤버 조회 시나리오")
    class GetChatRoomMembersScenarios {
        @Nested
        @DisplayName("채팅방 멤버 조회 성공 시나리오")
        class GetChatRoomMembersSuccessScenarios {
            @Test
            @DisplayName("성공")
            void getChatRoomMembersSuccess() {
                // given
                given(entityFacade.readChatRoom(chatRoom.getId())).willReturn(chatRoom);
                given(chatRoomMemberRepository.findAllWithUserByChatRoomId(chatRoom.getId())).willReturn(List.of(chatRoomAdmin, chatRoomMember));

                // when
                var result = chatRoomMemberService.getChatRoomMembers(chatRoom.getId());

                // then
                assertThat(result).isNotNull();
                assertThat(result.size()).isEqualTo(2);
            }
        }
    }

    @Nested
    @DisplayName("User ID로 채팅방 멤버 조회 시나리오")
    class ReadChatRoomMembersByUserIdScenarios {
        @Nested
        @DisplayName("User ID로 채팅방 멤버 조회 성공 시나리오")
        class ReadChatRoomMembersByUserIdSuccessScenarios {
            @Test
            @DisplayName("성공")
            void readChatRoomMembersByUserIdSuccess() {
                // given
                given(chatRoomMemberRepository.findAllByUserId(user.getId())).willReturn(List.of(chatRoomAdmin, chatRoomMember));

                // when
                List<ChatRoomMember> result = chatRoomMemberService.readChatRoomMembersByUserId(user.getId());

                // then
                assertThat(result).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 인원 조회 시나리오")
    class CountChatRoomMembersScenarios {
        @Nested
        @DisplayName("채팅방 인원 조회 성공 시나리오")
        class CountChatRoomMembersSuccessScenarios {
            @Test
            @DisplayName("성공")
            void countChatRoomMembersSuccess() {
                // given
                given(chatRoomMemberRepository.countByChatRoomId(chatRoom.getId())).willReturn(2);

                // when
                int count = chatRoomMemberService.countChatRoomMembers(chatRoom.getId());

                // then
                assertThat(count).isEqualTo(2);
            }
        }
    }
}
