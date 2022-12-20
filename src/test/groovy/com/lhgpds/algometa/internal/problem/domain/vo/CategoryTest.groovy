package com.lhgpds.algometa.internal.problem.domain.vo

import com.lhgpds.algometa.internal.problem.domain.vo.code.Category
import spock.lang.Specification

class CategoryTest extends Specification {

    def "카테고리 문자열 입력 유효성 테스트 성공"() {
        expect:
        Category.fromCategory(input)

        where:
        input << ["ARRAY", "SLIDING_WINDOW"]

    }

    def "카테고리 문자열 입력 유효성 테스트 실패시 예외 출력"() {
        when:
        Category.fromCategory(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["ARRAy", "SlingWindows1"]
    }
}
