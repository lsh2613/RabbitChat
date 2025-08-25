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

    @BeforeEach
    void setUp() {
        when(user.getId()).thenReturn(UserFixture.FIRST_USER.toEntity().getId());
    }

    @Nested
    @DisplayName("signIn 시나리오")
    class SignInScenario {
        private final OauthProvider provider = mock(OauthProvider.class);
        private final String code = "test-code";
        private final OidcDecodePayload payload = mock(OidcDecodePayload.class);
        private final Oauth oauth = mock(Oauth.class);
        private final Jwts jwts = mock(Jwts.class);

        @BeforeEach
        void setUp() {
            when(oauth.getUser()).thenReturn(user);
            when(oauthHelper.getOidcDecodedPayload(any(), any())).thenReturn(payload);
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
                Pair<Long, Jwts> result = oauthService.signIn(provider, new OauthSignInReq(code));

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
                Pair<Long, Jwts> result = oauthService.signIn(provider, new OauthSignInReq(code));

                // then
                assertThat(result.getLeft()).isEqualTo(-1L);
                assertThat(result.getRight()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("signUp 시나리오")
    class SignUpScenario {
        private final OauthProvider provider = OauthProvider.GOOGLE;
        private final String code = "test-code";
        private final String nickname = "test-nickname";
        private final OauthSignUpReq req = mock(OauthSignUpReq.class);
        private final OauthTokenRes tokenRes = mock(OauthTokenRes.class);
        private final OidcDecodePayload payload = mock(OidcDecodePayload.class);
        private final Oauth oauth = mock(Oauth.class);
        private final Jwts jwts = mock(Jwts.class);

        @BeforeEach
        void setUp() {
            when(oauthHelper.getIdToken(any(), any())).thenReturn(tokenRes);
            when(oauthHelper.getOidcDecodedPayload(any(), any())).thenReturn(payload);
            when(req.code()).thenReturn(code);
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
                when(oauthRepository.save(any())).thenReturn(oauth);
                when(jwtHelper.createToken(user)).thenReturn(jwts);

                // when
                Pair<Long, Jwts> result = oauthService.signUp(provider, req);

                // then
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
