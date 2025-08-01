package com.rabbitmqprac.global.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.rabbitmqprac.global.exception.payload.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import static com.rabbitmqprac.global.exception.payload.ReasonCode.TYPE_MISMATCH_ERROR_IN_REQUEST_BODY;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Custom Exception을 처리하는 메서드
     *
     * @see GlobalErrorException
     */
    @ExceptionHandler(GlobalErrorException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalErrorException(GlobalErrorException e) {
        log.warn("handleGlobalErrorException : {}", e.getMessage());
        CausedBy causedBy = e.causedBy();
        ErrorResponse response = ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getExplainError());

        return ResponseEntity.status(causedBy.statusCode().getCode()).body(response);
    }

    /**
     * API 호출 시 'Cookie' 내에 데이터 값이 유효하지 않은 경우
     *
     * @see MissingRequestCookieException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestCookieException.class)
    protected ErrorResponse handleMissingRequestCookieException(MissingRequestCookieException e) {
        log.warn("handleMissingRequestCookieException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 'Method' 내에 데이터 값이 유효하지 않은 경우
     *
     * @see HttpRequestMethodNotSupportedException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("handleHttpRequestMethodNotSupportedException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @see MissingRequestHeaderException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.warn("handleMissingRequestHeaderException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 'Parameter' 내에 데이터 값이 존재하지 않은 경우
     *
     * @see MissingServletRequestParameterException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("handleMissingServletRequestParameterException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        log.warn("handleHandlerMethodValidationException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MISSING_REQUIRED_PARAMETER);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 인가 관련 예외를 처리하는 메서드
     *
     * @see AccessDeniedException
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.warn("handleAccessDeniedException : {}", e.getMessage());

        CausedBy causedBy = CausedBy.of(StatusCode.FORBIDDEN, ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN);

        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 객체 혹은 파라미터 데이터 값이 유효하지 않은 경우
     *
     * @see MethodArgumentNotValidException
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException: {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        return ErrorResponse.failure(bindingResult, ReasonCode.REQUIRED_PARAMETERS_MISSING_IN_REQUEST_BODY);
    }

    /**
     * API 호출 시 객체 혹은 파라미터 데이터 값이 유효하지 않은 경우
     *
     * @see CustomValidationException
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(CustomValidationException.class)
    protected ErrorResponse handleCustomValidationException(CustomValidationException e) {
        log.warn("handleCustomValidationException: {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        return ErrorResponse.failure(bindingResult, ReasonCode.REQUIRED_PARAMETERS_MISSING_IN_REQUEST_BODY);
    }

    /**
     * API 호출 시 객체 혹은 파라미터 데이터 값이 유효하지 않은 경우
     *
     * @see MethodArgumentTypeMismatchException
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("handleMethodArgumentTypeMismatchException: {}", e.getMessage());

        Class<?> type = e.getRequiredType();
        assert type != null;

        Map<String, String> fieldErrors = new HashMap<>();
        if (type.isEnum()) {
            fieldErrors.put(e.getName(), "The parameter " + e.getName() + " must have a value among : " + StringUtils.join(type.getEnumConstants(), ", "));
        } else {
            fieldErrors.put(e.getName(), "The parameter " + e.getName() + " must have a value of type " + type.getSimpleName());
        }

        CausedBy causedBy = CausedBy.of(StatusCode.UNPROCESSABLE_CONTENT, TYPE_MISMATCH_ERROR_IN_REQUEST_BODY);
        return ErrorResponse.failure(causedBy.getCode(), causedBy.getReason(), e.getMessage(), fieldErrors);
    }

    /**
     * JSON 형식의 요청 데이터를 파싱하는 과정에서 발생하는 예외를 처리하는 메서드
     *
     * @see HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("handleHttpMessageNotReadableException : {}", e.getMessage());

        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            CausedBy causedBy = CausedBy.of(StatusCode.UNPROCESSABLE_CONTENT, ReasonCode.TYPE_MISMATCH_ERROR_IN_REQUEST_BODY);

            return ResponseEntity.unprocessableEntity().body(
                    ErrorResponse.of(
                            causedBy.getCode(),
                            causedBy.getReason(),
                            mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 유효하지 않습니다."
                    )
            );
        }

        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.MALFORMED_REQUEST_BODY);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage())
        );
    }

    /**
     * 잘못된 URL 호출 시
     *
     * @see NoHandlerFoundException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("handleNoHandlerFoundException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.NOT_FOUND, ReasonCode.INVALID_URL_OR_ENDPOINT);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * 존재하지 않는 URL 호출 시
     *
     * @see NoHandlerFoundException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    protected ErrorResponse handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("handleNoResourceFoundException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.NOT_FOUND, ReasonCode.INVALID_URL_OR_ENDPOINT);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * API 호출 시 데이터를 반환할 수 없는 경우
     *
     * @return ResponseEntity<ErrorResponse>
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected ErrorResponse handleHttpMessageNotWritableException(HttpMessageNotWritableException e) {
        log.warn("handleHttpMessageNotWritableException : {}", e.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * NullPointerException이 발생한 경우
     *
     * @return ResponseEntity<ErrorResponse>
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    protected ErrorResponse handleNullPointerException(NullPointerException e) {
        log.warn("handleNullPointerException : {}", e.getMessage());
        e.printStackTrace();
        CausedBy causedBy = CausedBy.of(StatusCode.INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }

    /**
     * IllegalArgumentException이 발생한 경우
     *
     * @return ResponseEntity<ErrorResponse>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("handleIllegalArgumentException : {}", e.getMessage());
        e.printStackTrace();
        CausedBy causedBy = CausedBy.of(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }
    // ================================================================================== //

    /**
     * 기타 예외가 발생한 경우
     *
     * @param e Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e) {
        log.warn("{} : handleException : {}", e.getClass(), e.getMessage());
        e.printStackTrace();
        CausedBy causedBy = CausedBy.of(StatusCode.INTERNAL_SERVER_ERROR, ReasonCode.UNEXPECTED_ERROR, DomainCode.NONE);
        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason(), e.getMessage());
    }
}
