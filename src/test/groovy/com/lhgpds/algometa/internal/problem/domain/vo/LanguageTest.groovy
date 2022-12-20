package com.lhgpds.algometa.internal.problem.domain.vo

import com.lhgpds.algometa.internal.problem.domain.vo.code.Language
import spock.lang.Specification

class LanguageTest extends Specification {

    def "언어 문자열 입력 유효성 테스트 성공"() {
        expect:
        Language.fromLanguage(input)

        where:
        input << ["JAVASCRIPT", "PYTHON", "JAVA", "CPP", "KOTLIN", "GO", "SCALA", "RUBY", "CSharp"]

    }

    def "언어 문자열 입력 유효성 테스트 실패시 예외 출력"() {
        when:
        Language.fromLanguage(input)

        then:
        thrown(IllegalArgumentException.class)

        where:
        input << ["ARRAy", "SlingWindows1"]
    }
}
