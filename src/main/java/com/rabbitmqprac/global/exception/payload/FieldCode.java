package com.rabbitmqprac.global.exception.payload;

/**
 * 도메인의 필드 코드
 * <pre>
 * {@code
 * @RequiredArgsConstructor
 * public enum UserFieldCode implements FieldCode {
 *    ZERO(0),
 *    ID(1),
 *    PASSWORD(2),
 *    NAME(3);
 *
 *    private final int code;
 *
 *    @Override
 *    public int getCode() {
 *      return code;
 *    }
 *
 *    @Override
 *    public String getFieldName() {
 *      return name().toLowerCase();
 *    }
 * }
 * }
 * </pre>
 * @author LSH
 */
public interface FieldCode {
    int getCode();
    String getFieldName();
}
