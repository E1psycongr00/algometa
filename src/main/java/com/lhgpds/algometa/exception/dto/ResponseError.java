package com.lhgpds.algometa.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lhgpds.algometa.exception.ErrorCode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {

    private int code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String details;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> errors;

    private ResponseError(ErrorCode errorCode, String path) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.path = path;
    }

    private ResponseError(ErrorCode errorCode, String details, String path) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.details = details;
        this.path = path;
    }

    private ResponseError(ErrorCode errorCode, List<FieldError> errors, String path) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
        this.path = path;
    }

    public static ResponseError of(ErrorCode errorCode, String path) {
        return new ResponseError(errorCode, path);
    }

    public static ResponseError of(ErrorCode errorCode, String details, String path) {
        return new ResponseError(errorCode, details, path);
    }

    public static ResponseError of(ErrorCode errorCode, BindingResult bindingResult,
        String path) {
        return new ResponseError(errorCode, FieldError.ofBindResults(bindingResult), path);
    }

    public static ResponseError of(MethodArgumentTypeMismatchException e, String path) {
        return new ResponseError(ErrorCode.INVALID_TYPE_VALUE,
            Collections.singletonList(FieldError.ofTypeMisMatch(e)), path);
    }

    public static ResponseError of(InvalidFormatException e, String path) {
        return new ResponseError(ErrorCode.INVALID_TYPE_VALUE,
            Collections.singletonList(FieldError.ofInvalidFormat(e)), path);
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FieldError {

        private String field;
        private String value;
        private String msg;

        private static FieldError ofTypeMisMatch(MethodArgumentTypeMismatchException e) {
            return FieldError.builder()
                .field(e.getName())
                .value(getRejectValue(e.getValue()))
                .msg(e.getErrorCode())
                .build();
        }

        private static FieldError ofInvalidFormat(InvalidFormatException e) {
            return FieldError.builder()
                .field(e.getPath().get(0).getFieldName())
                .value(e.getValue() + "")
                .msg("'" + e.getTargetType().getSimpleName() + "' Type 을 원해요")
                .build();
        }

        private static List<FieldError> ofBindResults(BindingResult bindingResult) {
            return bindingResult.getFieldErrors()
                .stream()
                .map(e -> FieldError.builder()
                    .field(e.getField())
                    .value(getRejectValue(e.getRejectedValue()))
                    .msg(e.getDefaultMessage())
                    .build())
                .collect(Collectors.toList());
        }

        private static String getRejectValue(Object rejectValue) {

            return rejectValue == null ? "" : rejectValue.toString();
        }

    }
}
