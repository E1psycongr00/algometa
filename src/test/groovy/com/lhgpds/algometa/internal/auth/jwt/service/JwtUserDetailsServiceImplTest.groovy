package com.lhgpds.algometa.internal.auth.jwt.service

import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser
import com.lhgpds.algometa.internal.member.domain.entity.Member
import com.lhgpds.algometa.internal.member.domain.vo.Email
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink
import com.lhgpds.algometa.internal.member.domain.vo.Role
import com.lhgpds.algometa.internal.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

class JwtUserDetailsServiceImplTest extends Specification {

    def memberRepository = Mock(MemberRepository.class)
    def userDetailService = new JwtUserDetailsServiceImpl(memberRepository)

    def "LoadUserByUsername - 회원이 DB 내에 존재하지 않는 경우"() {
        given:
        memberRepository.findByEmail(_ as Email) >> Optional.empty()

        when:
        userDetailService.loadUserByUsername("hello@naver.com")

        then:
        thrown(UsernameNotFoundException)
    }

    def "LoadUserByUsername - 회원이 DB 내에 존재하는 경우"() {
        given:
        Member output = Member.builder()
                .id(1L)
                .email(Email.from("helloworld@naver.com"))
                .image(ImageLink.from("hh"))
                .role(Role.GHOST)
                .build()

        memberRepository.findByEmail(_ as Email) >> Optional.of(output)

        when:
        def principals = userDetailService.loadUserByUsername("helloworld@naver.com")

        then:
        principals in AlgoUser
    }
}
