package com.lhgpds.algometa.exception.common.member;

import com.lhgpds.algometa.exception.ErrorCode;
import com.lhgpds.algometa.exception.common.BusinessException;

public class MemberNotFoundException extends BusinessException {

    private static final String NOT_FOUND_MEMBER_MESSAGE = "이미 회원이 존재합니다";

    public MemberNotFoundException() {
        super(ErrorCode.INVALID_INPUT_VALUE, NOT_FOUND_MEMBER_MESSAGE);
    }
}
