package com.rabbitmqprac.global.exception.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object fieldErrors;

    @Builder
    private ErrorResponse(String code, String message, Object fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    public static ErrorResponse failure(String code, String message, Object fieldErrors) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .fieldErrors(fieldErrors)
                .build();
    }

    /**
     * 422 Unprocessable Content 예외에서 발생한 BindingResult를 응답으로 변환한다.
     *
     * @param bindingResult : BindingResult
     * @return ErrorResponse
     */
    public static ErrorResponse failure(BindingResult bindingResult, ReasonCode reasonCode) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        CausedBy causedBy = CausedBy.of(StatusCode.UNPROCESSABLE_CONTENT, reasonCode, DomainCode.NONE);
        return failure(causedBy.getCode(), causedBy.getReason(), fieldErrors);
    }
}
