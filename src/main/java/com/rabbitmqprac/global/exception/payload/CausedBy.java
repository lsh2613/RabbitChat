package com.rabbitmqprac.global.exception.payload;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 에러 코드를 구성하는 상세 코드
 *
 * @param statusCode {@link StatusCode} 상태 코드
 * @param reasonCode {@link ReasonCode} 이유 코드
 * @param domainCode {@link DomainCode} 도메인 코드
 * @author LSH
 */
public record CausedBy(
        StatusCode statusCode,
        ReasonCode reasonCode,
        DomainCode domainCode,
        FieldCode fieldCode
) {
    public CausedBy {
        Objects.requireNonNull(statusCode, "statusCode must not be null");
        Objects.requireNonNull(reasonCode, "reasonCode must not be null");

        if (!isValidCodes(statusCode.getCode(), reasonCode.getCode())) {
            throw new IllegalArgumentException("Invalid bit count");
        }
    }

    /**
     * CausedBy 객체를 생성하는 정적 팩토리 메서드
     * <br/>
     * 모든 코드의 조합으로 생성된 최종 코드는 4자리의 정수 문자열로 구성된다.
     *
     * @param statusCode {@link StatusCode} 상태 코드 (3자리, 필수)
     * @param reasonCode {@link ReasonCode} 이유 코드 (1자리)
     * @param domainCode {@link DomainCode} 도메인 코드 (1자리)
     * @param fieldCode  {@link FieldCode} 필드 코드 (1자리)
     * @return CausedBy
     * @throws IllegalArgumentException 전체 코드가 4자리 이상이 아닌 경우
     * @throws NullPointerException     인자가 null인 경우
     */
    public static CausedBy of(StatusCode statusCode, ReasonCode reasonCode, DomainCode domainCode, FieldCode fieldCode) {
        return new CausedBy(statusCode, reasonCode, domainCode, fieldCode);
    }

    public static CausedBy of(StatusCode statusCode, ReasonCode reasonCode, DomainCode domainCode) {
        return new CausedBy(statusCode, reasonCode, domainCode, null);
    }

    public static CausedBy of(StatusCode statusCode, ReasonCode reasonCode) {
        return new CausedBy(statusCode, reasonCode, null, null);
    }

    /**
     * status code, reason code, domain code, field code를 조합하여 에러 코드를 생성한다.
     *
     * @return String : 5자리 정수로 구성된 에러 코드
     */
    public String getCode() {
        return generateCode();
    }

    /**
     * 에러가 발생한 이유를 반환한다.
     * <br/>
     * Reason은 사전에 예외 문서에 명시한 정보를 반환한다.
     *
     * @return String : 에러가 발생한 이유
     */
    public String getReason() {
        return reasonCode.name();
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(statusCode)) sb.append(statusCode.getCode());
        if (Objects.nonNull(reasonCode)) sb.append(reasonCode.getCode());
        if (Objects.nonNull(domainCode)) sb.append(domainCode.getCode());
        if (Objects.nonNull(fieldCode)) sb.append(fieldCode.getCode());
        return sb.toString();
    }

    private boolean isValidCodes(int statusCode, int reasonCode) {
        return isValidDigit(statusCode, 3)
                && isValidDigit(reasonCode, 1);
    }

    private boolean isValidDigit(int number, long expectedDigit) {
        return calcDigit(number) == expectedDigit;
    }

    private long calcDigit(int number) {
        if (number == 0) return 1;
        return Stream.iterate(number, n -> n > 0, n -> n / 10)
                .count();
    }

    public void toErrorResponse() {

    }
}
