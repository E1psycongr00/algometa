package com.lhgpds.algometa.exception.common;


import com.lhgpds.algometa.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateException extends BusinessException{


    public DuplicateException(String message) {
        super(ErrorCode.INVALID_INPUT_VALUE, message);
    }
}
