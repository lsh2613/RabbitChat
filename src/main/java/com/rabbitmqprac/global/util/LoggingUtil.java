package com.rabbitmqprac.global.util;

import com.rabbitmqprac.global.annotation.Util;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Util
public final class LoggingUtil {

    public static List<String> getArguments(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .map(LoggingUtil::getObjectFields)
                .toList();
    }

    private static String getObjectFields(Object obj) {
        if (Objects.isNull(obj)) {
            return "null";
        }

        StringBuilder result = new StringBuilder();
        Class<?> objClass = obj.getClass();
        result.append(objClass.getSimpleName()).append(" {");

        Field[] fields = objClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                result.append(fields[i].getName()).append(" = ")
                        .append(fields[i].get(obj));
            } catch (IllegalAccessException e) {
                result.append(fields[i].getName()).append("=ACCESS_DENIED");
            }
            if (i < fields.length - 1) {
                result.append(", ");
            }
        }
        result.append("}");
        return result.toString();
    }

    public static String getParameterMessage(List<String> arguments) {
        if (arguments == null)
            return "";

        return String.join(" | ", arguments);
    }
}
