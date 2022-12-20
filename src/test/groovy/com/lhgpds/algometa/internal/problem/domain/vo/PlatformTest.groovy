package com.lhgpds.algometa.internal.problem.domain.vo

import spock.lang.Specification

class PlatformTest extends Specification {
    def "플랫폼 문자열 입력 유효성 테스트 성공"() {
        expect:
        Platform.fromPlatform(input)

        where:
        input << ["BACKJOON", "PROGRAMMERS", "LEETCODE", "ETC"]
    }

    def "플랫폼 문자열 입력 유효성 테스트 실패시 예외 출력"() {
        when:
        Platform.fromPlatform(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["", "SlingWindows1"]
    }
}
