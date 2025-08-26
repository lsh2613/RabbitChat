package com.rabbitmqprac.service;

import com.rabbitmqprac.application.dto.auth.req.AuthSignInReq;
import com.rabbitmqprac.application.dto.auth.req.AuthSignUpReq;
import com.rabbitmqprac.application.dto.auth.req.AuthUpdatePasswordReq;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.auth.exception.AuthErrorCode;
import com.rabbitmqprac.domain.context.auth.exception.AuthErrorException;
import com.rabbitmqprac.domain.context.auth.service.AuthService;
import com.rabbitmqprac.domain.context.user.dto.req.UserCreateReq;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.helper.JwtHelper;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 테스트")
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtHelper jwtHelper;
    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthService authService;

    private static final User USER = UserFixture.FIRST_USER.toEntity();
    private static final Jwts JWTS = mock(Jwts.class);

    @Nested
    @DisplayName("회원가입 시나리오")
    class SignUpScenario {
        private final String nickname = "nickname";
        private final String username = "nickname";
        private final String password = "password_123";
        private final String confirmPassword = "password_123";
        private final AuthSignUpReq req = new AuthSignUpReq(nickname, username, password, confirmPassword);

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("회원가입 성공")
            void signUpSuccess() {
                // given
                given(userService.saveUserWithEncryptedPassword(any(UserCreateReq.class))).willReturn(USER);
                given(jwtHelper.createToken(USER)).willReturn(JWTS);

                // when
                Pair<Long, Jwts> result = authService.signUp(req);

                // then
                assertThat(result.getLeft()).isEqualTo(USER.getId());
                assertThat(result.getRight()).isEqualTo(JWTS);
                verify(userService).saveUserWithEncryptedPassword(any(UserCreateReq.class));
                verify(jwtHelper).createToken(USER);
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("password와 confirmPassword가 다르면 회원가입 실패")
            void signUpFailWhenPasswordMismatch() {
                // given
                AuthSignUpReq invalidReq = new AuthSignUpReq(nickname, username, password, "wrong");

                // when
                AuthErrorException ex = assertThrows(AuthErrorException.class, () -> authService.signUp(invalidReq));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(AuthErrorCode.PASSWORD_CONFIRM_MISMATCH);
            }
        }
    }

    @Nested
    @DisplayName("로그인 시나리오")
    class SignInScenario {
        private final String username = "nickname";
        private final String password = "password";
        private final AuthSignInReq req = new AuthSignInReq(username, password);

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("로그인 성공")
            void signInSuccess() {
                // given
                given(userService.readUserByUsername(username)).willReturn(USER);
                given(jwtHelper.createToken(USER)).willReturn(JWTS);
                given(bCryptPasswordEncoder.matches(password, USER.getPassword())).willReturn(true);

                // when
                Pair<Long, Jwts> result = authService.signIn(req);

                // then
                assertThat(result.getLeft()).isEqualTo(USER.getId());
                assertThat(result.getRight()).isEqualTo(JWTS);
                verify(jwtHelper).createToken(USER);
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("패스워드가 올바르지 않으면 로그인 실패")
            void signInFailWhenInvalidPassword() {
                // given
                given(userService.readUserByUsername(username)).willReturn(USER);
                given(bCryptPasswordEncoder.matches(password, USER.getPassword())).willReturn(false);

                // when
                AuthErrorException ex = assertThrows(AuthErrorException.class, () -> authService.signIn(req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(AuthErrorCode.INVALID_PASSWORD);
                verify(jwtHelper, never()).createToken(USER);
            }
        }
    }

    @Nested
    @DisplayName("패스워드 변경 시나리오")
    class UpdatePasswordScenario {
        private final Long userId = 1L;
        private final String oldPassword = "oldPassword";
        private final String newPassword = "newPassword";
        private final AuthUpdatePasswordReq req = new AuthUpdatePasswordReq(oldPassword, newPassword);

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("패스워드 변경 성공")
            void updatePasswordSuccess() {
                // given
                given(userService.readUser(userId)).willReturn(USER);
                given(bCryptPasswordEncoder.matches(oldPassword, USER.getPassword())).willReturn(true);
                given(req.newPassword(bCryptPasswordEncoder)).willReturn(newPassword);

                // when
                authService.updatePassword(userId, req);

                // then
                assertThat(USER.getPassword()).isEqualTo(newPassword);
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("기존 패스워드가 틀리면 변경 실패")
            void updatePasswordFailWhenInvalidOldPassword() {
                // given
                given(userService.readUser(userId)).willReturn(USER);
                given(bCryptPasswordEncoder.matches(oldPassword, USER.getPassword())).willReturn(false);

                // when
                AuthErrorException ex = assertThrows(AuthErrorException.class, () -> authService.updatePassword(userId, req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(AuthErrorCode.INVALID_PASSWORD);
            }
        }
    }
}
