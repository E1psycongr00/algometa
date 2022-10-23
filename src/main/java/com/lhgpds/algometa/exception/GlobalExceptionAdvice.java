package com.lhgpds.algometa.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lhgpds.algometa.exception.common.BusinessException;
import com.lhgpds.algometa.exception.dto.ResponseErrorDto;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * > 요청에 매개변수가 없으면 오류 메시지와 함께 400 Bad Request 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 컨트롤러에 전송된 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<ResponseErrorDto> handleMethodMissingServletRequestParameterException(
        MissingServletRequestParameterException e, HttpServletRequest httpServletRequest) {

        log.error(e.getClass().getSimpleName(), e.getMessage());
        ResponseErrorDto errorResponse = ResponseErrorDto.of(ErrorCode.INVALID_INPUT_VALUE,
            httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * > 요청이 유효하지 않으면 오류 메시지와 함께 400 Bad Request 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 컨트롤러에 전송된 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ResponseErrorDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseErrorDto errorResponse = ResponseErrorDto.of(ErrorCode.INVALID_INPUT_VALUE,
            e.getBindingResult(), httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * > 인자 타입이 일치하지 않는 경우 400 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ResponseErrorDto> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseErrorDto errorResponse = ResponseErrorDto.of(e, httpServletRequest.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * > 잘못된 httpMethod를 사용한 경우 상태가 405 응답 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ResponseErrorDto> handleMethodNotAllowedException(
        HttpRequestMethodNotSupportedException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e);

        ResponseErrorDto errorResponse = ResponseErrorDto.of(ErrorCode.METHOD_NOT_ALLOWED,
            "Allow: " + Arrays.toString(e.getSupportedMethods()),
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }


    /**
     * > 완전히 잘못된 형식으로 요청한 경우 상태가 400인 응답 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    private ResponseEntity<ResponseErrorDto> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e, HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName());

        if (e.getRootCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) e.getRootCause();
            return ResponseEntity.badRequest()
                .body(ResponseErrorDto.of(invalidFormatException,
                    httpServletRequest.getRequestURI()));
        }

        return ResponseEntity.badRequest()
            .body(ResponseErrorDto.of(ErrorCode.INVALID_INPUT_VALUE,
                httpServletRequest.getRequestURI()));
    }


    /**
     * > 비즈니스 계층에서 발생한 예외를 처리하고 오류 코드 및 메시지가 포함된 응답을 반환
     *
     * @param e                  예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ResponseErrorDto> handleBusinessException(BusinessException e,
        HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getSimpleName(), e.getMessage());

        ResponseErrorDto responseErrorDto = ResponseErrorDto.of(e.getErrorCode(), e.getMessage(),
            httpServletRequest.getRequestURI());
        return ResponseEntity.status(e.getErrorCode().getCode()).body(responseErrorDto);
    }

    /**
     * > 핸들링한 예외 이외의 예외는 500 응답을 반환
     *
     * @param e 예외 객체
     * @param httpServletRequest 서버로 보낸 요청 개체
     * @return ResponseEntity<ResponseErrorDto>
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ResponseErrorDto> handleException(Exception e,
        HttpServletRequest httpServletRequest) {
        log.error(e.getClass().getName(), e);

        ResponseErrorDto errorResponse = ResponseErrorDto.of(ErrorCode.INTERNAL_SERVER_ERROR,
            httpServletRequest.getRequestURI());
        return ResponseEntity.internalServerError().body(errorResponse);
    }


}
