package com.lhgpds.algometa.internal.problem.domain.vo.code

import spock.lang.Specification

class SpendTimeTest extends Specification {

    def "소요 시간 유효성 검사 성공"() {
        expect:
        new SpendTime("00:24:34")
    }

    def "소요 시간 유효성 검사 성공"() {
        when:
        new SpendTime(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["12:asd1", "46:12:12", "12:00:99", "12:99:00"]
    }
}
