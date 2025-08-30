package com.rabbitmqprac.domain.context.oauth.service;

import com.rabbitmqprac.application.dto.oauth.req.OauthSignInReq;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignUpReq;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorCode;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {
    private final OauthHelper oauthHelper;
    private final OauthRepository oauthRepository;
    private final UserService userService;
    private final JwtHelper jwtHelper;

    @Transactional(readOnly = true)
    public Pair<Long, Jwts> signIn(OauthProvider oauthProvider, OauthSignInReq req) {
        String code = URLDecoder.decode(req.code(), StandardCharsets.UTF_8);
        OauthTokenRes tokenRes = oauthHelper.getIdToken(oauthProvider, code);
        OidcDecodePayload payload = oauthHelper.getOidcDecodedPayload(oauthProvider, tokenRes.idToken());
        log.debug("payload : {}", payload);

        User user = oauthRepository.findBySubAndOauthProvider(payload.sub(), oauthProvider)
                .map(oauth -> oauth.getUser())
                .orElse(null);

        return (user != null)
                ? Pair.of(user.getId(), jwtHelper.createToken(user))
                : Pair.of(-1L, null);
    }

    @Transactional
    public Pair<Long, Jwts> signUp(OauthProvider oauthProvider, OauthSignUpReq req) {
        String code = URLDecoder.decode(req.code(), StandardCharsets.UTF_8);
        OauthTokenRes tokenRes = oauthHelper.getIdToken(oauthProvider, code);
        OidcDecodePayload payload = oauthHelper.getOidcDecodedPayload(oauthProvider, tokenRes.idToken());

        if (oauthRepository.existsBySubAndOauthProvider(payload.sub(), oauthProvider)) {
            throw new OauthErrorException(OauthErrorCode.CONFLICT);
        }

        userService.validateNicknameDuplication(req.nickname());

        User user = userService.create(req.nickname());
        oauthRepository.save(Oauth.of(oauthProvider, payload.sub(), user));

        return Pair.of(user.getId(), jwtHelper.createToken(user));
    }
}
