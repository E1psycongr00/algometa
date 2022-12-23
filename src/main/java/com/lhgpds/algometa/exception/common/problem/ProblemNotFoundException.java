package com.lhgpds.algometa.exception.common.problem;

import com.lhgpds.algometa.exception.ErrorCode;
import com.lhgpds.algometa.exception.common.BusinessException;

public class ProblemNotFoundException extends BusinessException {

    private static final String NOT_FOUND_PROBLEM = "해당 Problem을 찾을 수 없습니다.";

    public ProblemNotFoundException() {
        super(ErrorCode.INVALID_INPUT_VALUE, NOT_FOUND_PROBLEM);
    }
}
