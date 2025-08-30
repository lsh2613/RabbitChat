package com.rabbitmqprac.service;

import com.rabbitmqprac.application.dto.user.res.UserDetailRes;
import com.rabbitmqprac.application.dto.user.req.NicknameCheckReq;
import com.rabbitmqprac.application.dto.user.req.NicknameUpdateReq;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.user.dto.req.UserCreateReq;
import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.domain.context.user.exception.UserErrorException;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
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
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final User USER = UserFixture.FIRST_USER.toEntity();

    @Nested
    @DisplayName("유저 저장 시나리오")
    class SaveUserScenario {
        private final UserCreateReq req = new UserCreateReq(USER.getNickname(), USER.getUsername(), USER.getPassword());

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("유저 저장 성공")
            void saveUserSuccess() {
                // given
                given(userRepository.existsByUsername(USER.getUsername())).willReturn(false);
                given(userRepository.save(any(User.class))).willReturn(USER);

                // when
                User saved = userService.saveUserWithEncryptedPassword(req);

                // then
                assertThat(saved.getUsername()).isEqualTo(USER.getUsername());
                assertThat(saved.getPassword()).isEqualTo(USER.getPassword());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("username 중복")
            void saveUserFailByDuplicateUsername() {
                // given
                given(userRepository.existsByUsername(USER.getUsername())).willReturn(true);

                // when
                UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.saveUserWithEncryptedPassword(req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.CONFLICT_USERNAME);
            }
        }
    }

    @Nested
    @DisplayName("유저 조회 시나리오")
    class ReadUserScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("유저 조회 성공")
            void readUserSuccess() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.of(USER));

                // when
                User found = userService.readUser(USER.getId());

                // then
                assertThat(found.getUsername()).isEqualTo(USER.getUsername());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 userId")
            void readUserFailByNotFound() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.empty());
                // when
                UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.readUser(USER.getId()));
                // then
                assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("유저 리스트 조회 시나리오")
    class GetUserDetailsScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("유저 리스트 조회 성공")
            void getUserDetailsSuccess() {
                // given
                given(userRepository.findAll()).willReturn(List.of(USER));

                // when
                List<UserDetailRes> details = userService.getUserDetails();

                // then
                assertThat(details).hasSize(1);
                assertThat(details.get(0).nickname()).isEqualTo(USER.getUsername());
            }
        }
    }

    @Nested
    @DisplayName("단일 유저 상세 조회 시나리오")
    class GetUserDetailScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("유저 상세 조회 성공")
            void getUserDetailSuccess() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.of(USER));

                // when
                UserDetailRes detail = userService.getUserDetail(USER.getId());

                // then
                assertThat(detail.nickname()).isEqualTo(USER.getUsername());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 userId")
            void getUserDetailFailByNotFound() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.empty());

                // when
                UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.getUserDetail(USER.getId()));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("username으로 유저 조회 시나리오")
    class ReadUserByUsernameScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("username으로 유저 조회 성공")
            void readUserByUsernameSuccess() {
                // given
                given(userRepository.findByUsername(USER.getUsername())).willReturn(Optional.of(USER));

                // when
                User found = userService.readUserByUsername(USER.getUsername());

                // then
                assertThat(found.getUsername()).isEqualTo(USER.getUsername());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않는 username")
            void readUserByUsernameFailByNotFound() {
                // given
                given(userRepository.findByUsername(USER.getUsername())).willReturn(Optional.empty());

                // when
                UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.readUserByUsername(USER.getUsername()));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("닉네임 변경 시나리오")
    class UpdateNicknameScenario {
        private final NicknameUpdateReq req = new NicknameUpdateReq("newNickname");

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("닉네임 변경 성공")
            void updateNicknameSuccess() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.of(USER));
                given(userRepository.existsByNickname(req.nickname())).willReturn(false);

                // when
                userService.updateNickname(USER.getId(), req);

                // then
                assertThat(USER.getNickname()).isEqualTo(req.nickname());
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("존재하지 않은 유저")
            void updateNicknameFailByNotFound() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.empty());

                // when
                UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.updateNickname(USER.getId(), req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
            }

            @Test
            @DisplayName("닉네임 중복")
            void updateNicknameFailByDuplicate() {
                // given
                given(userRepository.findById(USER.getId())).willReturn(Optional.of(USER));
                given(userRepository.existsByNickname(req.nickname())).willReturn(true);

                // when
                UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.updateNickname(USER.getId(), req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.CONFLICT_USERNAME);
            }
        }
    }

    @Nested
    @DisplayName("username 중복 체크 시나리오")
    class UsernameDuplicateCheckScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("username 중복 체크 - 중복")
            void isDuplicatedUsernameTrue() {
                // given
                given(userRepository.existsByUsername(USER.getUsername())).willReturn(true);

                // when
                Boolean result = userService.isDuplicatedUsername(USER.getUsername());

                // then
                assertThat(result).isTrue();
            }

            @Test
            @DisplayName("username 중복 체크 - 중복 아님")
            void isDuplicatedUsernameFalse() {
                // given
                given(userRepository.existsByUsername(USER.getUsername())).willReturn(false);

                // when
                Boolean result = userService.isDuplicatedUsername(USER.getUsername());

                // then
                assertThat(result).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("닉네임 중복 체크 시나리오")
    class NicknameDuplicateCheckScenario {
        private final NicknameCheckReq req = mock(NicknameCheckReq.class);

        @BeforeEach
        void setUp() {
            given(req.nickname()).willReturn(USER.getNickname());
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("닉네임 중복 체크 - 중복")
            void isDuplicatedNicknameTrue() {
                // given
                given(userRepository.existsByNickname(USER.getNickname())).willReturn(true);

                // when
                Boolean result = userService.isDuplicatedNickname(req);

                // then
                assertThat(result).isTrue();
            }

            @Test
            @DisplayName("닉네임 중복 체크 - 중복 아님")
            void isDuplicatedNicknameFalse() {
                // given
                given(userRepository.existsByNickname(USER.getNickname())).willReturn(false);

                // when
                Boolean result = userService.isDuplicatedNickname(req);

                // then
                assertThat(result).isFalse();
            }
        }
    }
}
