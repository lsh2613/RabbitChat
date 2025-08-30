package com.rabbitmqprac.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 여러 예외 응답 예시를 한 번에 문서화할 때 사용하는 어노테이션입니다.
 * <br>
 * <pre>
 * - value: {@link ApiExceptionExplanation} 어노테이션 배열을 받아 여러 예외 상황을 한 번에 기술할 수 있습니다.
 * </pre>
 *
 * <pre><code>
 * {@code
 * @ApiResponseExplanations({
 *     @ApiExceptionExplanation(
 *         errorCodes = OauthErrorCode.class,
 *         constants = "MISSING_ISS, INVALID_ISS"
 *     ),
 *     @ApiExceptionExplanation(
 *         errorCodes = OauthErrorCode.class,
 *         constants = "EXPIRED_TOKEN"
 *     )
 * })
 * }
 * </code></pre>
 * <p>
 * 단일 예외만 문서화할 경우 {@link ApiExceptionExplanation}만 사용해도 됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiExceptionExplanations {
    ApiExceptionExplanation[] value() default {};
}
