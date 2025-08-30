package com.rabbitmqprac.global.validator;

import com.rabbitmqprac.global.annotation.Nickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]{2,10}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && PASSWORD_PATTERN.matcher(value).matches();
    }
}
