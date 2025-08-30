package com.rabbitmqprac.global.aspect;

import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.global.util.LoggingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Profile("prod")
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class UndefinedExceptionLoggingAspect {

    @Pointcut("execution(public * com.rabbitmqprac.application.controller..*.*(..))")
    private void logPointcut() {
    }

    @AfterThrowing(value = "logPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        if (exception instanceof GlobalErrorException) return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();

        String parameterMessage = getParameterMessage(joinPoint);

        MDC.put("httpStatus", "500");

        Throwable cause = exception.getCause();
        String stackTrace = getStackTrace(exception);

        log.error("[SERVER ERROR] POINT : {} || ARGUMENTS : {}", className, parameterMessage);
        log.error("[SERVER ERROR] MESSAGE : {}", exception.getMessage());
        log.error("[SERVER ERROR] CAUSE : {}", cause != null ? cause.toString() : "No cause available"
        );
        log.error("[SERVER ERROR] FINAL POINT : {}",
                (stackTrace != null && stackTrace.length() > 0) ? getFirstLine(stackTrace) : "No stack trace available"
        );

        MDC.remove("httpStatus");
    }

    private static String getParameterMessage(JoinPoint joinPoint) {
        List<String> arguments = LoggingUtil.getArguments(joinPoint);
        return LoggingUtil.getParameterMessage(arguments);
    }

    private static String getFirstLine(String stackTrace) {
        return stackTrace.split("\n")[0];
    }

    private static String getStackTrace(Exception exception) {
        StringWriter stackTraceWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTraceWriter));
        String stackTrace = stackTraceWriter.toString();
        return stackTrace;
    }
}

