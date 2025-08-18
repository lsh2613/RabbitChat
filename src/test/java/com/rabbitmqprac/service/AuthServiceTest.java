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
import static org.mockito.BDDMockito.anyLong;
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

    private static User user = UserFixture.FIRST_USER.toEntity();

    @Nested
    @DisplayName("회원가입 성공 시나리오")
    class SignUpSuccessScenarios {
        @Test
        @DisplayName("회원가입 성공")
        void signUp() {
            // given
            final String username = "username";
            final String password = "password_123";
            final String confirmPassword = "password_123";
            AuthSignUpReq req = new AuthSignUpReq(username, password, confirmPassword);
            Jwts jwts = mock(Jwts.class);

            given(userService.saveUserWithEncryptedPassword(any(UserCreateReq.class)))
                    .willReturn(user);
            given(jwtHelper.createToken(user)).willReturn(jwts);

            // when
            Pair<Long, Jwts> result = authService.signUp(req);

            // then
            assertThat(result.getLeft()).isEqualTo(user.getId());
            assertThat(result.getRight()).isEqualTo(jwts);
            verify(userService).saveUserWithEncryptedPassword(any(UserCreateReq.class));
            verify(jwtHelper).createToken(user);
        }
    }

    @Nested
    @DisplayName("회원가입 실패 시나리오")
    class SignUpFailScenarios {
        @Test
        @DisplayName("password와 confirmPassword가 다른 경우 회원 가입에 실패")
        void signUpWhenInvalidPassword() {
            // given
            final String username = "username";
            final String password = "password";
            final String confirmPassword = "invalid_password";
            AuthSignUpReq req = new AuthSignUpReq(username, password, confirmPassword);

            // when
            AuthErrorException errorException = assertThrows(AuthErrorException.class, () -> authService.signUp(req));

            // then
            assertThat(errorException.getErrorCode()).isEqualTo(AuthErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }
    }

    @Nested
    @DisplayName("로그인 성공 시나리오")
    class SignInSuccessScenarios {
        @Test
        @DisplayName("로그인 성공")
        void signIn() {
            // given
            final String username = "username";
            final String password = "password";
            AuthSignInReq req = new AuthSignInReq(username, password);
            Jwts jwts = mock(Jwts.class);

            given(userService.readUserByUsername(username)).willReturn(user);
            given(jwtHelper.createToken(user)).willReturn(jwts);
            given(bCryptPasswordEncoder.matches(password, user.getPassword())).willReturn(Boolean.TRUE);

            // when
            Pair<Long, Jwts> result = authService.signIn(req);

            // then
            assertThat(result.getLeft()).isEqualTo(user.getId());
            assertThat(result.getRight()).isEqualTo(jwts);
            verify(jwtHelper).createToken(user);
        }
    }

    @Nested
    @DisplayName("로그인 실패 시나리오")
    class SignInFailScenarios {
        @Test
        @DisplayName("로그인 유저의 패스워드가 올바르지 않다면 로그인 실패")
        void signInWhenInvalidPassword() {
            // given
            final String username = "username";
            final String password = "password";
            AuthSignInReq req = new AuthSignInReq(username, password);
            given(userService.readUserByUsername(username)).willReturn(user);
            given(bCryptPasswordEncoder.matches(password, user.getPassword())).willReturn(Boolean.FALSE);

            // when
            AuthErrorException errorException = assertThrows(AuthErrorException.class, () -> authService.signIn(req));

            // then
            assertThat(errorException.getErrorCode()).isEqualTo(AuthErrorCode.INVALID_PASSWORD);
            verify(jwtHelper, never()).createToken(user);
        }
    }

    @Nested
    @DisplayName("패스워드 변경 성공 시나리오")
    class UpdatePasswordScenarios {
        @Test
        @DisplayName("패스워드 변경 성공")
        void updatePasswordSuccess() {
            // given
            final Long userId = anyLong();
            final String oldPassword = "oldPassword";
            final String newPassword = "newPassword";
            AuthUpdatePasswordReq req = new AuthUpdatePasswordReq(oldPassword, newPassword);

            given(userService.readUser(userId)).willReturn(user);
            given(bCryptPasswordEncoder.matches(req.oldPassword(), user.getPassword())).willReturn(true);
            given(req.newPassword(bCryptPasswordEncoder)).willReturn(newPassword);

            // when
            authService.updatePassword(userId, req);

            // then
            assertThat(user.getPassword()).isEqualTo(newPassword);
        }

        @Test
        @DisplayName("기존 패스워드가 틀리면 변경 실패")
        void updatePasswordFailWhenInvalidOldPassword() {
            // given
            final Long userId = anyLong();
            final String oldPassword = "oldPassword";
            final String newPassword = "newPassword";
            AuthUpdatePasswordReq req = new AuthUpdatePasswordReq(oldPassword, newPassword);
            given(userService.readUser(userId)).willReturn(user);
            given(bCryptPasswordEncoder.matches(oldPassword, user.getPassword())).willReturn(false);

            // when
            AuthErrorException errorException = assertThrows(AuthErrorException.class, () -> authService.updatePassword(userId, req));

            // then
            assertThat(errorException.getErrorCode()).isEqualTo(AuthErrorCode.INVALID_PASSWORD);
        }
    }
}
