package com.rabbitmqprac.global.annotation;

import com.rabbitmqprac.global.validator.NicknameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {NicknameValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface Nickname {
    String message() default "한글, 영문, 숫자만 사용하여, 2~10자의 닉네임을 입력해 주세요";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
