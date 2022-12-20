package com.lhgpds.algometa.internal.problem.domain.vo.code;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    SLIDING_WINDOW("슬라이딩윈도우"),
    ARRAY("배열"),
    STRING("문자열"),
    HASH("해쉬"),
    DP("동적 계획법"),
    MATH("수학"),
    GREEDY("탐욕법"),
    GRAPH("그래프"),
    TREE("트리"),
    TWO_POINTER("투포인터"),
    BIT_MANIPULATION("비트연산"),
    STACK("스택"),
    QUEUE("큐"),
    IMPLEMENTATION("구현"),
    UNION_FIND("유니온파인드"),
    GEOMETRY("기하학"),
    SEGMENT_TREE("세그먼트트리"),
    RECURSIVE("재귀"),
    BINARY_SEARCH("이분탐색");

    private static final String INVALID_VALUE_INPUT = "잘못된 입력입니다. 카테고리 목록에 포함된 값을 입력해주세요.";
    private static final String CATEGORY_LIST = Arrays.toString(Category.values());
    private final String details;

    public static Category fromCategory(String value) {
        for (Category category : Category.values()) {
            if (category.name().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException(INVALID_VALUE_INPUT + CATEGORY_LIST);
    }
}
