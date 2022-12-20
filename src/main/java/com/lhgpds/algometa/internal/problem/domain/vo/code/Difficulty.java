package com.lhgpds.algometa.internal.problem.domain.vo.code;


import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Difficulty {

    EASY("쉬움"),
    MIDDLE("중간"),
    HARD("어려움");

    private static final String INVALID_VALUE_INPUT = "잘못된 입력입니다. 난이도 목록에 포함된 값을 입력해주세요.";
    private static final String DIFFICULTY_LIST = Arrays.toString(Difficulty.values());
    private final String details;

    public static Difficulty fromDifficulty(String value) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.name().equals(value)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException(INVALID_VALUE_INPUT + DIFFICULTY_LIST);
    }
}
