package com.lhgpds.algometa.internal.member.service

import com.lhgpds.algometa.exception.common.member.MemberDuplicateException
import com.lhgpds.algometa.exception.common.member.MemberNotFoundException
import com.lhgpds.algometa.internal.member.domain.entity.Member
import com.lhgpds.algometa.internal.member.domain.vo.Email
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink
import com.lhgpds.algometa.internal.member.domain.vo.Nickname
import com.lhgpds.algometa.internal.member.domain.vo.Role
import com.lhgpds.algometa.internal.member.repository.MemberRepository
import com.lhgpds.algometa.internal.member.service.dto.MemberDto
import spock.lang.Specification

class MemberServiceImplTest extends Specification {

    def memberRepository = Mock(MemberRepository.class)
    def memberService = new MemberServiceImpl(memberRepository)

    def "join - 중복 예외"() {
        given:
        def input = MemberDto.builder()
                .email(Email.from("hello@naver.com"))
                .build()
        memberRepository.existsByEmail(_ as Email) >> true

        when:
        memberService.join(input)

        then:
        0 * memberRepository.save(_) // memberRepository.save 가 실행되어서는 안됨
        def e = thrown(MemberDuplicateException)
        e.message == "이미 회원이 존재합니다."
    }

    def "join - 회원가입 완료시 권한은 반드시 Anonymous 여야 함"() {
        given:
        def input = MemberDto.builder()
                .email(Email.from("hello@naver.com"))
                .build()
        memberRepository.existsByEmail(_ as Email) >> false

        when:
        def result = memberService.join(input)

        then:
        result.role == Role.GHOST
    }

    def "findByEmail - 회원이 존재하지 않는 경우"() {
        given:
        def input = MemberDto.builder()
                .email(Email.from("helloworld@naver.com"))
                .build()
        memberRepository.findByEmail(_ as Email) >> Optional.empty()
        when:
        memberService.findByEmail(input)
        then:
        def e = thrown(MemberNotFoundException)
        e.message == "이미 회원이 존재합니다"
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
        def e = thrown(MemberNotFoundException)
        e.message == "이미 회원이 존재합니다"
    }

    def "updateProfile - 회원이 존재하지 않는 경우"() {
        given:
        def userInfo = MemberDto.builder()
                .id(1L)
                .email(Email.from("helloworld@naver.com"))
                .build()
        def request = MemberDto.builder()
                .nickname(Nickname.from("hello"))
                .image(ImageLink.from("image"))
                .build()
        memberRepository.findById(_ as Long) >> Optional.empty()
        when:
        memberService.updateProfile(userInfo, request)

        then:
        def e = thrown(MemberNotFoundException)
        e.message == "이미 회원이 존재합니다"
    }

    def "updateProfile - 프로필 수정이 완료된 경우"() {
        given:
        def userInfo = MemberDto.builder()
                .id(1L)
                .email(Email.from("helloworld@naver.com"))
                .build()
        def request = MemberDto.builder()
                .nickname(Nickname.from("hello"))
                .image(ImageLink.from("image"))
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
                .email(Email.from("helloworld@naver.com"))
                .build()
        def request = MemberDto.builder()
                .image(ImageLink.from("image"))
                .build()
        def entity = Member.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .nickname(Nickname.from("beforeName"))
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
        def request = MemberDto.builder().image(ImageLink.from("1234")).build()
        memberRepository.findById(_ as Long) >> Optional.empty()

        when:
        memberService.updateImage(userInfo, request)

        then:
        thrown(MemberNotFoundException)
    }
}
