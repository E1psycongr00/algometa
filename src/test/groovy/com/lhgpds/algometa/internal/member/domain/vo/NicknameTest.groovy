package com.lhgpds.algometa.internal.member.domain.vo

import spock.lang.Specification

class NicknameTest extends Specification {

    def "nickname 유효성 검증 테스트"() {
        when:
        Nickname nickname = new Nickname(input)

        then:
        def e = thrown(IllegalArgumentException.class)

        where:
        input << ["ab  asq", " asdA가", "asdjfhalkshdkasjdhfkasdfasdfasdfasdf", "ㄱㄴㄷ", "개똥#이"]
    }
}
