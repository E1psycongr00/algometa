package com.lhgpds.algometa.internal.problem.domain.vo;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    BACKJOON("백준"),
    PROGRAMMERS("프로그래머스"),
    LEETCODE("리트코드"),
    ETC("기타");

    private static final String INVALID_VALUE_INPUT = "잘못된 입력입니다. 플랫폼 목록에 포함된 값을 입력해주세요.";
    private static final String PLATFORM_LIST = Arrays.toString(Platform.values());
    private final String details;

    public static Platform fromPlatform(String value) {
        for (Platform platform : Platform.values()) {
            if (platform.name().equals(value)) {
                return platform;
            }
        }
        throw new IllegalArgumentException(INVALID_VALUE_INPUT + PLATFORM_LIST);
    }
}
