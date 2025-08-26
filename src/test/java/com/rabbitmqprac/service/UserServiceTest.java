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

    private static User user = UserFixture.FIRST_USER.toEntity();

    @Nested
    @DisplayName("유저 저장 성공 시나리오")
    class UserSaveSuccessScenarios {
        @Test
        @DisplayName("유저 저장 성공")
        void saveUser() {
            // given
            UserCreateReq req = new UserCreateReq(user.getNickname(), user.getUsername(), user.getPassword());
            given(userRepository.save(any(User.class))).willReturn(user);
            given(userRepository.existsByUsername(user.getUsername())).willReturn(Boolean.FALSE);

            // when
            User savedUser = userService.saveUserWithEncryptedPassword(req);

            // then
            assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
            assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        }
    }

    @Nested
    @DisplayName("유저 저장 실패 시나리오")
    class UserSaveFailScenarios {
        @Test
        @DisplayName("nickname 중복")
        void saveUserWhenExistingUserByUsername() {
            // given
            UserCreateReq req = new UserCreateReq(user.getNickname(), user.getUsername(), user.getPassword());
            given(userRepository.existsByUsername(user.getUsername())).willReturn(Boolean.TRUE);

            // when
            UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.saveUserWithEncryptedPassword(req));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.CONFLICT_USERNAME);
        }
    }

    @Nested
    @DisplayName("유저 조회 성공 시나리오")
    class UserReadSuccessScenarios {
        @Test
        @DisplayName("유저 조회 성공")
        void readUser() {
            // given
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

            // when
            User foundUser = userService.readUser(user.getId());

            // then
            assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
        }
    }

    @Nested
    @DisplayName("유저 조회 실패 시나리오")
    class UserReadFailScenarios {
        @Test
        @DisplayName("존재하지 않는 userId")
        void readUserWhenNotFoundedUserId() {
            // given
            given(userRepository.findById(user.getId())).willReturn(Optional.empty());

            // when
            UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.readUser(user.getId()));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
        }
    }


    @Nested
    @DisplayName("유저 리스트 조회 성공 시나리오")
    class UserListScenarios {
        @Test
        @DisplayName("유저 리스트 조회 성공")
        void getUserDetails() {
            // given
            given(userRepository.findAll()).willReturn(List.of(user));

            // when
            List<UserDetailRes> details = userService.getUserDetails();

            // then
            assertThat(details).hasSize(1);
            assertThat(details.get(0).nickname()).isEqualTo(user.getUsername());
        }
    }


    @Nested
    @DisplayName("단일 유저 상세 조회 성공 시나리오")
    class UserDetailSuccessScenarios {
        @Test
        @DisplayName("유저 상세 조회 성공")
        void getUserDetail() {
            // given
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

            // when
            UserDetailRes detail = userService.getUserDetail(user.getId());

            // then
            assertThat(detail.nickname()).isEqualTo(user.getUsername());
        }
    }

    @Nested
    @DisplayName("단일 유저 상세 조회 실패 시나리오")
    class UserDetailFailScenarios {
        @Test
        @DisplayName("존재하지 않는 userId")
        void getUserDetailWhenNotFoundedUserId() {
            // given
            given(userRepository.findById(user.getId())).willReturn(Optional.empty());

            // when
            UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.getUserDetail(user.getId()));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("username으로 유저 조회 성공 시나리오")
    class UserReadByUsernameSuccessScenarios {
        @Test
        @DisplayName("username으로 유저 조회 성공")
        void readUserByUsername() {
            // given
            given(userRepository.findByUsername(user.getUsername())).willReturn(java.util.Optional.of(user));

            // when
            User foundUser = userService.readUserByUsername(user.getUsername());

            // then
            assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
        }
    }

    @Nested
    @DisplayName("username으로 유저 조회 실패 시나리오")
    class UserReadByUsernameFailScenarios {
        @Test
        @DisplayName("존재하지 않는 nickname")
        void readUserByUsernameWhenNotFoundedUser() {
            // given
            given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.empty());

            // when
            UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.readUserByUsername(user.getUsername()));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("닉네임 변경 성공 시나리오")
    class UpdateNicknameSuccessScenarios {
        @Test
        @DisplayName("닉네임 변경 성공")
        void updateNicknameSuccess() {
            // given
            NicknameUpdateReq req = new NicknameUpdateReq("newNickname");
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(userRepository.existsByNickname(req.nickname())).willReturn(Boolean.FALSE);

            // when
            userService.updateNickname(user.getId(), req);

            // then
            assertThat(user.getNickname()).isEqualTo(req.nickname());
        }
    }

    @Nested
    @DisplayName("닉네임 변경 실패 시나리오")
    class UpdateNicknameFailScenarios {
        @Test
        @DisplayName("존재하지 않은 유저")
        void updateNicknameFailWhenNotFoundedUser() {
            // given
            NicknameUpdateReq req = mock(NicknameUpdateReq.class);
            given(userRepository.findById(user.getId())).willReturn(Optional.empty());

            // when
            UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.updateNickname(user.getId(), req));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("닉네임 중복")
        void updateNicknameFailByDuplicate() {
            // given
            NicknameUpdateReq req = new NicknameUpdateReq("newNickname");
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(userRepository.existsByNickname(req.nickname())).willReturn(Boolean.TRUE);

            // when
            UserErrorException ex = assertThrows(UserErrorException.class, () -> userService.updateNickname(user.getId(), req));

            // then
            assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.CONFLICT_USERNAME);
        }
    }

    @Nested
    @DisplayName("nickname 중복 체크 성공 시나리오")
    class UsernameDuplicateCheckSuccessScenarios {
        @Test
        @DisplayName("nickname 중복 체크 - 중복")
        void isDuplicatedUsernameTrue() {
            // given
            given(userRepository.existsByUsername(user.getUsername())).willReturn(Boolean.TRUE);

            // when
            Boolean result = userService.isDuplicatedUsername(user.getUsername());

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("nickname 중복 체크 - 중복 아님")
        void isDuplicatedUsernameFalse() {
            // given
            given(userRepository.existsByUsername(user.getUsername())).willReturn(Boolean.FALSE);

            // when
            Boolean result = userService.isDuplicatedUsername(user.getUsername());

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("닉네임 중복 체크 성공 시나리오")
    class NicknameDuplicateCheckSuccessScenarios {
        private static NicknameCheckReq req = mock(NicknameCheckReq.class);

        @BeforeEach
        void setUp() {
            given(req.nickname()).willReturn(user.getNickname());
        }

        @Test
        @DisplayName("닉네임 중복 체크 - 중복")
        void isDuplicatedNicknameTrue() {
            // given
            given(userRepository.existsByNickname(user.getNickname())).willReturn(Boolean.TRUE);

            // when
            Boolean result = userService.isDuplicatedNickname(req);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("닉네임 중복 체크 - 중복 아님")
        void isDuplicatedNicknameFalse() {
            // given
            given(userRepository.existsByNickname(user.getNickname())).willReturn(Boolean.FALSE);

            // when
            Boolean result = userService.isDuplicatedNickname(req);

            // then
            assertThat(result).isFalse();
        }
    }

}
