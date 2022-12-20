package com.lhgpds.algometa.internal.problem.domain.vo.code;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    SUCCESS("성공", "Success"),
    TLE("시간 초과", "Time Limit Error"),
    MLE("메모리 초과", "Memory Limit Error"),
    FAIL("실패", "Fail");

    private static final String INVALID_VALUE_INPUT = "잘못된 입력입니다. 카테고리에 포함된 이름을 입력해주세요.";
    private static final String STATUS_LIST = Arrays.toString(Status.values());
    private final String details;
    private final String message;

    public static Status fromStatus(String value) {
        for (Status status : Status.values()) {
            if (status.name().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(INVALID_VALUE_INPUT);
    }
}