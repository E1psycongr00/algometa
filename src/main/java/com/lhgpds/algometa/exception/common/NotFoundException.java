package com.lhgpds.algometa.exception.common;

import com.lhgpds.algometa.exception.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
