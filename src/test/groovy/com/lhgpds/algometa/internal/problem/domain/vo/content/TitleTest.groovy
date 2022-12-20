package com.lhgpds.algometa.internal.problem.domain.vo.content

import spock.lang.Specification

class TitleTest extends Specification {

    def "타이틀 유효성 검사 성공"() {
        expect:
        Title.from(input)

        where:
        input << ["title", "타이틀", "eazy", "헬로 우", "오늘의 여행 #1"]

    }

    def "타이틀 유효성 검사 실패시 예외"() {
        when:
        Title.from(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["", "탑" * 21]
    }
}
