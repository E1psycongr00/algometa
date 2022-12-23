package com.lhgpds.algometa.internal.problem.domain.vo

import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty
import spock.lang.Specification

class DifficultyTest extends Specification {

    def "난이도 문자열 입력 유효성 테스트 성공"() {
        expect:
        Difficulty.fromDifficulty(input)

        where:
        input << ["EASY", "MIDDLE", "HARD"]
    }

    def "난이도 문자열 입력 유효성 테스트 실패시 예외 출력"() {
        when:
        Difficulty.fromDifficulty(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["", "SlingWindows1"]
    }
}
