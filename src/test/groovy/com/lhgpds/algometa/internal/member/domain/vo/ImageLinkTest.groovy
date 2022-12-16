package com.lhgpds.algometa.internal.member.domain.vo

import spock.lang.Specification

class ImageLinkTest extends Specification {

    def "이미지 링크 최소 최대 길이 검증"() {
        when:
        ImageLink imageLink = new ImageLink(input)

        then:
        def e = thrown(IllegalArgumentException.class)

        where:
        input << ["", "1" * 266]
    }
}
