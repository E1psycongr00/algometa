package com.lhgpds.algometa.internal.problem.domain.vo

import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty
import com.lhgpds.algometa.internal.problem.domain.vo.code.Status
import spock.lang.Specification

class StatusTest extends Specification {

    def "제출 상태 문자열 입력 유효성 테스트 성공"() {
        expect:
        Status.fromStatus(input)

        where:
        input << ["SUCCESS", "TLE", "MLE", "FAIL"]
    }

    def "제출 상태 문자열 입력 유효성 테스트 실패시 예외 출력"() {
        when:
        Difficulty.fromDifficulty(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["", "SlingWindows1"]
    }
}
