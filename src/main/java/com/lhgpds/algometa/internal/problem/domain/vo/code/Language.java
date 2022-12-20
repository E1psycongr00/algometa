package com.lhgpds.algometa.internal.problem.domain.vo.code;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    JAVASCRIPT("Javascript"),
    PYTHON("Python"),
    JAVA("Java"),
    CPP("C++"),
    KOTLIN("Kotlin"),
    GO("Go"),
    SCALA("Scala"),
    RUBY("Ruby"),
    CSharp("C#");

    private static final String INVALID_VALUE_INPUT = "잘못된 입력입니다. 언어 목록에 포함된 값을 입력해주세요.";
    private static final String INVALID_LANGUAGE_INPUT = Arrays.toString(Language.values());
    private final String details;

    public static Language fromLanguage(String value) {
        for (Language language : Language.values()) {
            if (language.name().equals(value)) {
                return language;
            }
        }
        throw new IllegalArgumentException(INVALID_VALUE_INPUT + INVALID_LANGUAGE_INPUT);
    }
}
