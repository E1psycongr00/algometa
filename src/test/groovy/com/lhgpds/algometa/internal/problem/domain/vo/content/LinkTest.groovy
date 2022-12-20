package com.lhgpds.algometa.internal.problem.domain.vo.content

import spock.lang.Specification

class LinkTest extends Specification {

    def "Link 유효성 테스트 성공"() {
        expect:
        Link.from("www.google.com")
    }

    def "Link 유효성 테스트 실패"() {
        when:
        Link.from(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["", "a" * 300]
    }
}
