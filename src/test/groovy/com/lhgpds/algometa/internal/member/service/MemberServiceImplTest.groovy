package com.lhgpds.algometa.internal.member.service

import com.lhgpds.algometa.exception.common.DuplicateException
import com.lhgpds.algometa.exception.common.NotFoundException
import com.lhgpds.algometa.internal.member.entity.Member
import com.lhgpds.algometa.internal.member.entity.vo.Role
import com.lhgpds.algometa.internal.member.repository.MemberRepository
import com.lhgpds.algometa.internal.member.service.dto.MemberDto
import spock.lang.Specification

class MemberServiceImplTest extends Specification {

    def memberRepository = Mock(MemberRepository.class)
    def memberService = new MemberServiceImpl(memberRepository)

    def "join - 중복 예외"() {
        given:
        def input = MemberDto.builder()
                .email("hello@naver.com")
                .build()
        memberRepository.existsByEmail(_ as String) >> true

        when:
        memberService.join(input)

        then:
        0 * memberRepository.save(_) // memberRepository.save 가 실행되어서는 안됨
        def e = thrown(DuplicateException)
        e.message == "이미 회원이 존재합니다"
    }

    def "join - 회원가입 완료시 권한은 반드시 Anonymous 여야 함"() {
        given:
        def input = MemberDto.builder()
                .email("hello@naver.com")
                .build()
        memberRepository.existsByEmail(_ as String) >> false

        when:
        def result = memberService.join(input)

        then:
        result.role == Role.GHOST
    }


    def "findByEmail - 회원이 존재하지 않는 경우"() {
        given:
        def input = MemberDto.builder()
                .email("helloworld@naver.com")
                .build()
        memberRepository.findByEmail(_ as String) >> Optional.empty()
        when:
        memberService.findByEmail(input)
        then:
        def e = thrown(NotFoundException)
        e.message == "회원이 존재하지 않습니다"
    }

    def "findByEmail - 출력 형식"() {
        given:
        def input = MemberDto.builder()
                .email("helloworld@naver.com")
                .build()
        def entity = Member.builder()
                .id(1L)
                .email("hello@naver.com")
                .nickname("hh")
                .role(Role.USER).build()

        memberRepository.findByEmail(_ as String) >> Optional.of(entity)

        when:
        def output = memberService.findByEmail(input)
        then:
        output.role != null
        output.email != null
        output.id != null
    }

    def "findById - 회원이 존재하지 않는 경우"() {
        given:
        def input = MemberDto.builder()
                .id(1L)
                .build()
        memberRepository.findById(_ as Long) >> Optional.empty()
        when:
        memberService.findById(input)
        then:
        def e = thrown(NotFoundException)
        e.message == "회원이 존재하지 않습니다"
    }

    def "findById - 출력 형식"() {
        given:
        def input = MemberDto.builder()
                .id(1L)
                .build()
        def entity = Member.builder()
                .id(1L)
                .email("hello@naver.com")
                .nickname("hh")
                .role(Role.USER).build()

        memberRepository.findById(_ as Long) >> Optional.of(entity)

        when:
        def output = memberService.findById(input)
        then:
        output.role != null
        output.email != null
        output.id != null
    }

    def "updateProfile - 회원이 존재하지 않는 경우"() {
        given:
        def userInfo = MemberDto.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .build()
        def request = MemberDto.builder()
                .nickname("hello")
                .image("image")
                .build()
        memberRepository.findById(_ as Long) >> Optional.empty()
        when:
        memberService.updateProfile(userInfo, request)

        then:
        def e = thrown(NotFoundException)
        e.message == "회원이 존재하지 않습니다"
    }

    def "updateProfile - 프로필 수정이 완료된 경우"() {
        given:
        def userInfo = MemberDto.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .build()
        def request = MemberDto.builder()
                .nickname("hello")
                .image("image")
                .build()
        def entity = Member.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .nickname(request.getNickname())
                .image(request.getImage())
                .role(Role.GHOST)
                .build()
        memberRepository.findById(_ as Long) >> Optional.of(entity)
        when:
        def output = memberService.updateProfile(userInfo, request)

        then:
        output.role == Role.USER
        output.id == entity.getId()
        output.email == entity.getEmail()
        output.image == entity.getImage()
    }

    def "updateProfile - 닉네임을 요청하지 않은 경우"() {
        given:
        def userInfo = MemberDto.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .build()
        def request = MemberDto.builder()
                .image("image")
                .build()
        def entity = Member.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .nickname("beforeName")
                .image(request.getImage())
                .role(Role.GHOST)
                .build()
        memberRepository.findById(_ as Long) >> Optional.of(entity)
        when:
        def output = memberService.updateProfile(userInfo, request)

        then:
        output.role == Role.USER
        output.id == entity.getId()
        output.nickname != null
        output.email == entity.getEmail()
        output.image == entity.getImage()
    }

    def "updateImage - 회원이 존재하지 않는 경우"() {
        given:
        def userInfo = MemberDto.builder().id(1L).build()
        def request = MemberDto.builder().image("1234").build()
        memberRepository.findById(_ as Long) >> Optional.empty()

        when:
        memberService.updateImage(userInfo, request)

        then:
        thrown(NotFoundException)
    }

    def "updateImage - 정상 출력 검증"() {
        given:
        def userInfo = MemberDto.builder().id(1L).build()
        def request = MemberDto.builder().image("1234").build()
        memberRepository.findById(_ as Long) >> Optional.of(Member.builder()
                .email("hello@naver.com")
                .nickname("asasdf")
                .id(1L)
                .image("h")
                .role(Role.GHOST).build())

        when:
        def output = memberService.updateImage(userInfo, request)

        then:
        output.id == 1L
        output.role == Role.USER

    }
}
