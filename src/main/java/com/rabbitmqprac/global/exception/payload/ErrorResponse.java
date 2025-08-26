package com.rabbitmqprac.global.exception.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Schema(title = "API 응답 - 실패 및 에러")
@Builder
@Getter
public class ErrorResponse {
    @Schema(
            title = "에러 코드",
            description = "정의된 에러의 4~6자리 정수형 문자열로 상태코드(3)+이유코드(1)+도메인코드(1)+필드코드(1)로 구성됩니다.",
            example = "40401"
    )
    private String code;
    @Schema(title = "에러 이유", description = "에러 코드의 이유코드에 해당하며, 에러 원인의 디테일한 상태값을 제공", example = "REQUESTED_RESOURCE_NOT_FOUND")
    private String reason;
    @Schema(title = "에러 메시지", description = "에러 메시지", example = "회원을 찾을 수 없습니다.")
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object fieldErrors;

    public static ErrorResponse of(String code, String reason, String message) {
        return ErrorResponse.builder()
                .code(code)
                .reason(reason)
                .message(message)
                .build();
    }

    public static ErrorResponse failure(String code, String reason, String message, Object fieldErrors) {
        return ErrorResponse.builder()
                .code(code)
                .reason(reason)
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
        return failure(causedBy.getCode(), causedBy.getReason(), "입력값 검증에 실패했습니다.", fieldErrors);
    }
}
