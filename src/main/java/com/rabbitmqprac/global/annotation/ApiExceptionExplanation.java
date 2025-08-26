package com.rabbitmqprac.global.annotation;

import com.rabbitmqprac.global.exception.payload.BaseErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 문서화에 사용되는 API 예외 응답 데이터를 담기 위한 어노테이션입니다.
 * <br>
 * <pre>
 * - errorCodes: BaseErrorCode를 구현한 Enum 클래스 타입을 지정합니다.
 * - constants: Enum 클래스의 상수명을 콤마(,)로 구분하여 여러 개 지정할 수 있습니다.
 *   예시: "INVALID_INPUT,RESOURCE_NOT_FOUND"
 * - mediaType: 응답 미디어 타입을 지정합니다. 기본값은 "application/json"입니다.
 * </pre>
 *
 * <pre><code>
 * {@code
 * @ApiExceptionExplanation(
 *     errorCodes = OauthErrorCode.class,
 *     constants = "MISSING_ISS, INVALID_ISS"
 * )
 * }
 * </code></pre>
 *
 * 여러 ErrorCode Enum을 지정해야 하는 경우 {@link ApiExceptionExplanations} 어노테이션을 사용합니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiExceptionExplanation {
    Class<? extends BaseErrorCode> errorCode();

    String[] constants();

    String mediaType() default "application/json";
}
