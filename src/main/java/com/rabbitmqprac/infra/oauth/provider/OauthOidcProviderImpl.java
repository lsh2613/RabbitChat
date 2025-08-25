package com.rabbitmqprac.infra.oauth.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorCode;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorException;
import com.rabbitmqprac.global.util.JwtErrorCodeUtil;
import com.rabbitmqprac.infra.oauth.dto.OidcDecodePayload;
import com.rabbitmqprac.infra.security.exception.JwtErrorCode;
import com.rabbitmqprac.infra.security.exception.JwtErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthOidcProviderImpl implements OauthOidcProvider {
    private static final String KID = "kid";
    private static final String RSA = "RSA";
    private final ObjectMapper objectMapper;

    @Override
    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud, String nonce) {
        return getUnsignedTokenClaims(token, iss, aud, nonce).get("header").get(KID);
    }

    @Override
    public OidcDecodePayload getOIDCTokenBody(String token, String modulus, String exponent) {
        Claims body = getOIDCTokenJws(token, modulus, exponent).getPayload();
        String aud = body.getAudience().iterator().next(); // aud가 여러개일 경우 첫 번째 aud를 사용

        return new OidcDecodePayload(
                body.getIssuer(),
                aud,
                body.getSubject(),
                body.get("email", String.class)
        );
    }

    /**
     * ID Token의 header와 body를 Base64 방식으로 디코딩하는 메서드 <br/>
     */
    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> getUnsignedTokenClaims(String token, String iss, String aud, String nonce) {
        try {
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String unsignedToken = getUnsignedToken(token);
            String headerJson = new String(decoder.decode(unsignedToken.split("\\.")[0]));
            String payloadJson = new String(decoder.decode(unsignedToken.split("\\.")[1]));

            Map<String, String> header = objectMapper.readValue(headerJson, Map.class);
            Map<String, String> payload = objectMapper.readValue(payloadJson, Map.class);

            validatePayload(payload, iss, aud, nonce);

            return Map.of("header", header, "payload", payload);
        } catch (JsonProcessingException e) {
            log.warn("getUnsignedTokenClaims : Error - {},  {}", e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * ID Token의 payload를 검증하는 메서드 <br/>
     */
    private void validatePayload(Map<String, String> payload, String iss, String aud, String nonce) {
        if (!payload.containsKey("iss")) {
            throw new OauthErrorException(OauthErrorCode.MISSING_ISS);
        }
        if (!payload.containsKey("aud")) {
            throw new OauthErrorException(OauthErrorCode.MISSING_AUD);
        }
        if (!payload.containsKey("nonce")) {
            throw new OauthErrorException(OauthErrorCode.MISSING_NONCE);
        }
        if (!payload.get("iss").equals(iss)) {
            throw new OauthErrorException(OauthErrorCode.INVALID_ISS);
        }
        if (!payload.get("aud").equals(aud)) {
            throw new OauthErrorException(OauthErrorCode.INVALID_AUD);
        }
        if (!payload.get("nonce").equals(nonce)) {
            throw new OauthErrorException(OauthErrorCode.INVALID_NONCE);
        }
    }

    /**
     * Token의 signature를 제거하는 메서드
     */
    private String getUnsignedToken(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw new JwtErrorException(JwtErrorCode.MALFORMED_TOKEN);
        return splitToken[0] + "." + splitToken[1];
    }

    /**
     * 공개키로 서명을 검증하는 메서드
     */
    private Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) {
        try {
            return Jwts.parser()
                    .verifyWith(getRSAPublicKey(modulus, exponent))
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e) {
            final JwtErrorCode errorCode = JwtErrorCodeUtil.determineErrorCode(e, JwtErrorCode.FAILED_AUTHENTICATION);

            log.warn("getOIDCTokenJws : Error code : {}, Error - {},  {}", errorCode, e.getClass(), e.getMessage());
            throw new JwtErrorException(errorCode);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.warn("getOIDCTokenJws : Error - {},  {}", e.getClass(), e.getMessage());
            throw new JwtErrorException(JwtErrorCode.MALFORMED_TOKEN);
        }
    }

    /**
     * n, e 조합으로 공개키를 생성하는 메서드
     */
    private PublicKey getRSAPublicKey(String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(publicKeySpec);
    }
}
