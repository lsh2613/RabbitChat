package com.rabbitmqprac.infra.security.exception;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import com.rabbitmqprac.global.exception.payload.DomainCode;
import com.rabbitmqprac.global.exception.payload.ReasonCode;
import com.rabbitmqprac.global.exception.payload.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.rabbitmqprac.global.exception.payload.ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN;
import static com.rabbitmqprac.global.exception.payload.ReasonCode.EXPIRED_OR_REVOKED_TOKEN;
import static com.rabbitmqprac.global.exception.payload.ReasonCode.INSUFFICIENT_PERMISSIONS;
import static com.rabbitmqprac.global.exception.payload.ReasonCode.INVALID_REQUEST_SYNTAX;
import static com.rabbitmqprac.global.exception.payload.ReasonCode.MISSING_OR_INVALID_AUTHENTICATION_CREDENTIALS;
import static com.rabbitmqprac.global.exception.payload.ReasonCode.USER_ACCOUNT_SUSPENDED_OR_BANNED;
import static com.rabbitmqprac.global.exception.payload.ReasonCode.WITHOUT_OWNERSHIP;
import static com.rabbitmqprac.global.exception.payload.StatusCode.BAD_REQUEST;
import static com.rabbitmqprac.global.exception.payload.StatusCode.FORBIDDEN;
import static com.rabbitmqprac.global.exception.payload.StatusCode.INTERNAL_SERVER_ERROR;
import static com.rabbitmqprac.global.exception.payload.StatusCode.UNAUTHORIZED;


@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {
    /**
     * 400 BAD_REQUEST: 클라이언트의 요청이 부적절 할 경우
     */
    INVALID_HEADER(BAD_REQUEST, INVALID_REQUEST_SYNTAX, "유효하지 않은 헤더 포맷입니다"),

    /**
     * 401 UNAUTHORIZED: 인증되지 않은 사용자
     */
    EMPTY_ACCESS_TOKEN(UNAUTHORIZED, MISSING_OR_INVALID_AUTHENTICATION_CREDENTIALS, "토큰이 비어있습니다"),
    FAILED_AUTHENTICATION(UNAUTHORIZED, MISSING_OR_INVALID_AUTHENTICATION_CREDENTIALS, "인증에 실패하였습니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, EXPIRED_OR_REVOKED_TOKEN, "사용기간이 만료된 토큰입니다"),
    INSUFFICIENT_PERMISSIONS_TOKEN(UNAUTHORIZED, INSUFFICIENT_PERMISSIONS, "자원에 대한 충분한 권한이 없습니다."),
    TAMPERED_TOKEN(UNAUTHORIZED, ReasonCode.TAMPERED_OR_MALFORMED_TOKEN, "서명이 조작된 토큰입니다"),
    MALFORMED_TOKEN(UNAUTHORIZED, ReasonCode.TAMPERED_OR_MALFORMED_TOKEN, "비정상적인 토큰입니다"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED, ReasonCode.TAMPERED_OR_MALFORMED_TOKEN, "지원하지 않는 토큰입니다"),
    WITHOUT_OWNERSHIP_REFRESH_TOKEN(UNAUTHORIZED, WITHOUT_OWNERSHIP, "소유권이 없는 리프레시 토큰입니다"),

    /**
     * 403 FORBIDDEN: 인증된 클라이언트가 권한이 없는 자원에 접근
     */
    FORBIDDEN_ACCESS_TOKEN(FORBIDDEN, ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN, "해당 토큰에는 엑세스 권한이 없습니다"),
    TAKEN_AWAY_TOKEN(FORBIDDEN, ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN, "탈취당한 토큰입니다. 다시 로그인 해주세요."),
    SUSPENDED_OR_BANNED_TOKEN(FORBIDDEN, USER_ACCOUNT_SUSPENDED_OR_BANNED, "사용자 계정이 정지되었습니다"),

    /**
     * 500 INTERNAL_SERVER_ERROR: 서버 내부 에러
     */
    INVALID_JWT_DTO_FORMAT(INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR, "서버 내부 에러가 발생했습니다."),
    UNEXPECTED_ERROR(INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR, "예상치 못한 에러가 발생했습니다.");

    private final StatusCode statusCode;
    private final ReasonCode reasonCode;
    private final String message;
    private final DomainCode domainCode = DomainCode.JWT;

    @Override
    public CausedBy causedBy() {
        return CausedBy.of(statusCode, reasonCode, domainCode);
    }

    @Override
    public String getExplainError() throws NoSuchFieldError {
        return message;
    }
}
