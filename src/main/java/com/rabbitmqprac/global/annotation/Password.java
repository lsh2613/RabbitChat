package com.rabbitmqprac.global.annotation;

import com.rabbitmqprac.global.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface Password {
    String message() default "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
