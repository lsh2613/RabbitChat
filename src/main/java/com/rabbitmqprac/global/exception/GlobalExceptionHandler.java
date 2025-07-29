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
     * Pennyway Custom Exception을 처리하는 메서드
     *
     * @see GlobalErrorException
     */
    @ExceptionHandler(GlobalErrorException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalErrorException(GlobalErrorException e) {
        log.warn("handleGlobalErrorException : {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(e.getBaseErrorCode().causedBy().getCode(), e.getBaseErrorCode().getExplainError());

        return ResponseEntity.status(e.getBaseErrorCode().causedBy().statusCode().getCode()).body(response);
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
        String code = String.valueOf(StatusCode.BAD_REQUEST.getCode() * 10 + ReasonCode.MISSING_REQUIRED_PARAMETER.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        String code = String.valueOf(StatusCode.BAD_REQUEST.getCode() * 10 + ReasonCode.INVALID_REQUEST.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        String code = String.valueOf(StatusCode.BAD_REQUEST.getCode() * 10 + ReasonCode.MISSING_REQUIRED_PARAMETER.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        String code = String.valueOf(StatusCode.BAD_REQUEST.getCode() * 10 + ReasonCode.MISSING_REQUIRED_PARAMETER.getCode());

        return ErrorResponse.of(code, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        log.warn("handleHandlerMethodValidationException : {}", e.getMessage());
        String code = String.valueOf(StatusCode.BAD_REQUEST.getCode() * 10 + ReasonCode.MISSING_REQUIRED_PARAMETER.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        CausedBy causedBy = CausedBy.of(StatusCode.FORBIDDEN, ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN, DomainCode.NONE);

        return ErrorResponse.of(causedBy.getCode(), causedBy.getReason());
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

        String code = String.valueOf(StatusCode.UNPROCESSABLE_CONTENT.getCode() * 10 + TYPE_MISMATCH_ERROR_IN_REQUEST_BODY.getCode());
        return ErrorResponse.failure(code, TYPE_MISMATCH_ERROR_IN_REQUEST_BODY.name(), fieldErrors);
    }

    /**
     * JSON 형식의 요청 데이터를 파싱하는 과정에서 발생하는 예외를 처리하는 메서드
     *
     * @see HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("handleHttpMessageNotReadableException : {}", e.getMessage());

        String code;
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            code = String.valueOf(StatusCode.UNPROCESSABLE_CONTENT.getCode() * 10 + TYPE_MISMATCH_ERROR_IN_REQUEST_BODY.getCode());
            return ResponseEntity.unprocessableEntity().body(ErrorResponse.of(code, mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 유효하지 않습니다."));
        }

        code = String.valueOf(StatusCode.BAD_REQUEST.getCode() * 10 + ReasonCode.MALFORMED_REQUEST_BODY.getCode());
        return ResponseEntity.badRequest().body(ErrorResponse.of(code, e.getMessage()));
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
        String code = String.valueOf(StatusCode.NOT_FOUND.getCode() * 10 + ReasonCode.INVALID_URL_OR_ENDPOINT.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        String code = String.valueOf(StatusCode.NOT_FOUND.getCode() * 10 + ReasonCode.INVALID_URL_OR_ENDPOINT.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        String code = String.valueOf(StatusCode.INTERNAL_SERVER_ERROR.getCode() * 10 + ReasonCode.UNEXPECTED_ERROR.getCode());

        return ErrorResponse.of(code, e.getMessage());
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
        String code = String.valueOf(StatusCode.INTERNAL_SERVER_ERROR.getCode() * 10 + ReasonCode.UNEXPECTED_ERROR.getCode());

        return ErrorResponse.of(code, StatusCode.INTERNAL_SERVER_ERROR.name());
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
        String code = String.valueOf(StatusCode.INTERNAL_SERVER_ERROR.getCode() * 10 + ReasonCode.UNEXPECTED_ERROR.getCode());

        return ErrorResponse.of(code, StatusCode.INTERNAL_SERVER_ERROR.name());
    }
}
