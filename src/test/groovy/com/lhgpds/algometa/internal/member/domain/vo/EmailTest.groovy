package com.lhgpds.algometa.internal.member.domain.vo

import spock.lang.Specification

class EmailTest extends Specification {

    def "잘못된 이메일 입력시 예외 발생"() {

        when:
        Email email = new Email(input)

        then:
        def e = thrown(IllegalArgumentException.class)

        where:
        input << ["asdfa", "asdfaq@asdfasdfasdfa.co12", "asdfa.anver.com"]
    }
}
