package com.rabbitmqprac.global.aspect;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import com.rabbitmqprac.global.util.LoggingUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("prod")
@Slf4j
@Aspect
@Component
public class GlobalExceptionLoggingAspect {

    @Pointcut("execution(public * com.rabbitmqprac.application.controller..*.*(..))")
    private void logPointcut() {
    }

    @AfterThrowing(value = "logPointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, GlobalErrorException ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();

        List<String> arguments = LoggingUtil.getArguments(joinPoint);
        String parameterMessage = LoggingUtil.getParameterMessage(arguments);

        BaseErrorCode errorCode = ex.getErrorCode();
        CausedBy causedBy = errorCode.causedBy();

        MDC.put("httpStatus", String.valueOf(causedBy.statusCode().getCode()));

        log.error("[ERROR] POINT : {} || ERROR CODE : {} || ARGUMENTS : {}",
                className, causedBy.getCode(), parameterMessage
        );
        log.error("[ERROR] FINAL POINT : {}", ex.getStackTrace()[0]);
        log.error("[ERROR] MESSAGE : {}", ex.getMessage());

        MDC.remove("httpStatus");
    }
}
