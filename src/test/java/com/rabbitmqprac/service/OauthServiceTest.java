package com.rabbitmqprac.service;

import com.rabbitmqprac.application.dto.oauth.req.OauthSignInReq;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignUpReq;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorCode;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorException;
import com.rabbitmqprac.domain.context.oauth.service.OauthService;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import com.rabbitmqprac.domain.persistence.oauth.entity.Oauth;
import com.rabbitmqprac.domain.persistence.oauth.repository.OauthRepository;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.helper.JwtHelper;
import com.rabbitmqprac.global.helper.OauthHelper;
import com.rabbitmqprac.infra.oauth.dto.OauthTokenRes;
import com.rabbitmqprac.infra.oauth.dto.OidcDecodePayload;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OauthService 테스트")
public class OauthServiceTest {
    @Mock
    private OauthHelper oauthHelper;
    @Mock
    private OauthRepository oauthRepository;
    @Mock
    private UserService userService;
    @Mock
    private JwtHelper jwtHelper;

    @InjectMocks
    private OauthService oauthService;

    private static final User user = mock(User.class);
    private static final Oauth oauth = mock(Oauth.class);

    @BeforeEach
    void setUp() {
        when(user.getId()).thenReturn(UserFixture.FIRST_USER.toEntity().getId());
        when(oauth.getUser()).thenReturn(user);
    }

    @Nested
    @DisplayName("signIn 시나리오")
    class SignInScenario {
        private final OauthSignInReq req = mock(OauthSignInReq.class);
        private final OauthProvider provider = mock(OauthProvider.class);
        private final String code = "test-code";
        private final String idToken = "test-id-token";
        private final OidcDecodePayload payload = mock(OidcDecodePayload.class);
        private final Jwts jwts = mock(Jwts.class);
        private final OauthTokenRes oauthTokenRes = mock(OauthTokenRes.class);

        @BeforeEach
        void setUp() {
            when(req.code()).thenReturn(code);
            when(oauthHelper.getIdToken(provider, code)).thenReturn(oauthTokenRes);
            when(oauthTokenRes.idToken()).thenReturn(idToken);
            when(oauthHelper.getOidcDecodedPayload(provider, idToken)).thenReturn(payload);
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("존재하는 유저일 때 정상적으로 토큰 반환")
            void signInSuccessWithExistingUser() {
                // given
                when(oauthRepository.findBySubAndOauthProvider(payload.sub(), provider)).thenReturn(Optional.of(oauth));
                when(jwtHelper.createToken(user)).thenReturn(jwts);

                // when
                Pair<Long, Jwts> result = oauthService.signIn(provider, req);

                // then
                assertThat(result.getLeft()).isEqualTo(user.getId());
                assertThat(result.getRight()).isEqualTo(jwts);
            }

            @Test
            @DisplayName("존재하지 않는 유저일 때 -1L, null 반환")
            void signInSuccessWittNotExistingUser() {
                // given
                when(oauthRepository.findBySubAndOauthProvider(payload.sub(), provider)).thenReturn(Optional.empty());

                // when
                Pair<Long, Jwts> result = oauthService.signIn(provider, req);

                // then
                assertThat(result.getLeft()).isEqualTo(-1L);
                assertThat(result.getRight()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("signUp 시나리오")
    class SignUpScenario {
        private final OauthProvider provider = mock(OauthProvider.class);
        private final String code = "test-code";
        private final String nickname = "test-nickname";
        private final String idToken = "test-id-token";
        private final OauthSignUpReq req = mock(OauthSignUpReq.class);
        private final OauthTokenRes tokenRes = mock(OauthTokenRes.class);
        private final OidcDecodePayload payload = mock(OidcDecodePayload.class);
        private final Jwts jwts = mock(Jwts.class);

        @BeforeEach
        void setUp() {
            when(req.code()).thenReturn(code);
            when(oauthHelper.getIdToken(provider, code)).thenReturn(tokenRes);
            when(tokenRes.idToken()).thenReturn(idToken);
            when(oauthHelper.getOidcDecodedPayload(provider, idToken)).thenReturn(payload);
            when(req.nickname()).thenReturn(nickname);
        }

        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상적으로 회원가입 및 토큰 반환")
            void signUpSuccess() {
                // given
                when(oauthRepository.existsBySubAndOauthProvider(payload.sub(), provider)).thenReturn(false);
                doNothing().when(userService).validateNicknameDuplication(nickname);
                when(userService.create(nickname)).thenReturn(user);
                when(jwtHelper.createToken(user)).thenReturn(jwts);

                // when
                Pair<Long, Jwts> result = oauthService.signUp(provider, req);

                // then
                verify(userService).create(nickname);
                verify(oauthRepository).save(any(Oauth.class));
                assertThat(result.getLeft()).isEqualTo(user.getId());
                assertThat(result.getRight()).isEqualTo(jwts);
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("이미 가입된 sub일 때 예외 발생")
            void signUpFail_conflict() {
                // given
                when(oauthRepository.existsBySubAndOauthProvider(payload.sub(), provider)).thenReturn(true);

                // when
                OauthErrorException ex = assertThrows(OauthErrorException.class, () -> oauthService.signUp(provider, req));

                // then
                assertThat(ex.getErrorCode()).isEqualTo(OauthErrorCode.CONFLICT);
            }
        }
    }
}
