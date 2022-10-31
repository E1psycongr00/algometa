package com.lhgpds.algometa.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lhgpds.algometa.exception.common.BusinessException;
import com.lhgpds.algometa.exception.dto.ResponseError;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * > 요청에 매개변수가 없으면 오류 메시지와 함께 400 Bad Request 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 컨트롤러에 전송된 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<ResponseError> handleMethodMissingServletRequestParameterException(
        MissingServletRequestParameterException e, HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName(), e.getMessage());
        ResponseError errorResponse = ResponseError.of(ErrorCode.INVALID_INPUT_VALUE,
            httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * > 요청이 유효하지 않으면 오류 메시지와 함께 400 Bad Request 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 컨트롤러에 전송된 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ResponseError> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseError errorResponse = ResponseError.of(ErrorCode.INVALID_INPUT_VALUE,
            e.getBindingResult(), httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * > 인자 타입이 일치하지 않는 경우 400 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ResponseError> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseError errorResponse = ResponseError.of(e, httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * > 잘못된 httpMethod를 사용한 경우 상태가 405 응답 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ResponseError> handleMethodNotAllowedException(
        HttpRequestMethodNotSupportedException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e);

        ResponseError errorResponse = ResponseError.of(ErrorCode.METHOD_NOT_ALLOWED,
            "Allow: " + Arrays.toString(e.getSupportedMethods()),
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }


    /**
     * > 완전히 잘못된 형식으로 요청한 경우 상태가 400인 응답 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    private ResponseEntity<ResponseError> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName());

        if (e.getRootCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) e.getRootCause();
            return ResponseEntity.badRequest()
                .body(ResponseError.of(invalidFormatException,
                    httpServletRequest.getRequestURI()));
        }

        return ResponseEntity.badRequest()
            .body(ResponseError.of(ErrorCode.INVALID_INPUT_VALUE,
                httpServletRequest.getRequestURI()));
    }

    /**
     * > AccessDeniedException이 발생하면 오류를 기록하고 오류 코드 FORBIDDEN 및 요청 URI를 사용하여 ResponseError 개체를 만들고
     * 403 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버에 대한 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException e,
        HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName());
        ResponseError errorResponse = ResponseError.of(ErrorCode.FORBIDDEN,
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    /**
     * > Jwt 토큰 인증 처리시 DB에 user가 없다면 발생하는 예외를 처리함
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버에 대한 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<ResponseError> handleUsernameNotFoundException(
        UsernameNotFoundException e, HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName());
        ResponseError errorResponse = ResponseError.of(ErrorCode.RESOURCE_NOT_FOUND,
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * > MultiPart key 정보가 누락되서 발생하는 예외. algometa 서비스의 경우 이미지에서 multipart를 사용하기 때문에 "images"가 key에
     * 없으면 예외가 발생한다
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버에 대한 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    private ResponseEntity<ResponseError> handleMissingServletRequestPartException(
        MissingServletRequestPartException e, HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName());
        ResponseError errorResponse = ResponseError.of(ErrorCode.INVALID_INPUT_VALUE,
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * > 업로드 사이즈를 초과한 경우 예외를 처리하고 `ResponseError` 본문과 함께 `ResponseEntity`를 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버에 대한 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    private ResponseEntity<ResponseError> handleMaxUploadSizeExceededException(
        MaxUploadSizeExceededException e, HttpServletRequest httpServletRequest
    ) {

        log.error(e.getClass().getSimpleName());
        ResponseError errorResponse = ResponseError.of(ErrorCode.INVALID_INPUT_VALUE,
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * > 비즈니스 계층에서 발생한 예외를 처리하고 오류 코드 및 메시지가 포함된 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ResponseError> handleBusinessException(BusinessException e,
        HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseError responseErrorDto = ResponseError.of(e.getErrorCode(), e.getMessage(),
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(e.getErrorCode().getCode()).body(responseErrorDto);
    }

    /**
     * > 핸들링한 예외 이외의 예외는 500 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseError>
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ResponseError> handleException(Exception e,
        HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getName(), e);
        ResponseError errorResponse = ResponseError.of(ErrorCode.INTERNAL_SERVER_ERROR,
            e.getMessage(),
            httpServletRequest.getRequestURI());
        return ResponseEntity.internalServerError().body(errorResponse);
    }


}
