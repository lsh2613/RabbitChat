package com.rabbitmqprac.global.util;

import com.rabbitmqprac.global.annotation.ApiExceptionExplanation;
import com.rabbitmqprac.global.annotation.ApiExceptionExplanations;
import com.rabbitmqprac.global.exception.payload.BaseErrorCode;
import com.rabbitmqprac.global.exception.payload.CausedBy;
import com.rabbitmqprac.global.exception.payload.ErrorResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ApiExceptionExplainParser {
    public static void parse(Operation operation, HandlerMethod handlerMethod) {
        ApiExceptionExplanations explanations = handlerMethod.getMethodAnnotation(ApiExceptionExplanations.class);
        if (explanations != null) {
            generateExceptionResponseDocs(operation, explanations.value());
            return;
        }
        ApiExceptionExplanation explanation = handlerMethod.getMethodAnnotation(ApiExceptionExplanation.class);
        if (explanation != null) {
            generateExceptionResponseDocs(operation, new ApiExceptionExplanation[]{explanation});
        }
    }

    private static void generateExceptionResponseDocs(Operation operation, ApiExceptionExplanation[] exceptions) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> holders = Arrays.stream(exceptions)
                .flatMap(ApiExceptionExplainParser::expandExampleHolders)
                .collect(Collectors.groupingBy(ExampleHolder::httpStatus));

        addExamplesToResponses(responses, holders);
    }

    private static Stream<ExampleHolder> expandExampleHolders(ApiExceptionExplanation annotation) {
        String[] constantsArr = annotation.constants();
        if (constantsArr == null || constantsArr.length == 0) {
            return Stream.of(ExampleHolder.from(annotation, null));
        }
        return Arrays.stream(constantsArr)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(constant -> ExampleHolder.from(annotation, constant));
    }

    private static void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> holders) {
        holders.forEach((httpStatus, exampleHolders) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse response = new ApiResponse();

            exampleHolders.forEach(holder -> mediaType.addExamples(holder.name(), holder.holder()));
            content.addMediaType("application/json", mediaType);
            response.setContent(content);

            responses.addApiResponse(String.valueOf(httpStatus), response);
        });
    }

    @Builder(access = AccessLevel.PRIVATE)
    private record ExampleHolder(int httpStatus, String name, String mediaType, String description, Example holder) {
        static ExampleHolder from(ApiExceptionExplanation annotation, String constantOverride) {
            BaseErrorCode errorCode = getErrorCode(annotation, constantOverride);

            return ExampleHolder.builder()
                    .httpStatus(errorCode.causedBy().statusCode().getCode())
                    .name(errorCode.getExplainError())
                    .mediaType(annotation.mediaType())
                    .holder(createExample(errorCode))
                    .build();
        }

        @SuppressWarnings("unchecked")
        public static <E extends Enum<E> & BaseErrorCode> E getErrorCode(ApiExceptionExplanation annotation, String constantOverride) {
            Class<E> enumClass = (Class<E>) annotation.errorCode();
            String constant = constantOverride != null ? constantOverride : (annotation.constants().length > 0 ? annotation.constants()[0] : null);
            return Enum.valueOf(enumClass, constant);
        }

        private static Example createExample(BaseErrorCode errorCode) {
            CausedBy causedBy = errorCode.causedBy();
            ErrorResponse response = ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), errorCode.getExplainError());

            Example example = new Example();
            example.setValue(response);

            return example;
        }
    }
}
