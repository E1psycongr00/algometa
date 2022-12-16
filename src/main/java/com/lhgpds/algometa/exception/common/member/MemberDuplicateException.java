package com.lhgpds.algometa.exception.common.member;


import com.lhgpds.algometa.exception.ErrorCode;
import com.lhgpds.algometa.exception.common.BusinessException;
import lombok.Getter;

@Getter
public class MemberDuplicateException extends BusinessException {

    private static final String DUPLICATE_MEMBER_MESSAGE = "이미 회원이 존재합니다.";

    public MemberDuplicateException() {
        super(ErrorCode.INVALID_INPUT_VALUE, DUPLICATE_MEMBER_MESSAGE);
    }
}
