package com.lhgpds.algometa.internal.auth.jwt.service

import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser
import com.lhgpds.algometa.internal.member.entity.Member
import com.lhgpds.algometa.internal.member.entity.vo.Role
import com.lhgpds.algometa.internal.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

class JwtUserDetailsServiceImplTest extends Specification {

    def memberRepository = Mock(MemberRepository.class)
    def userDetailService = new JwtUserDetailsServiceImpl(memberRepository)

    def "LoadUserByUsername - 회원이 DB 내에 존재하지 않는 경우"() {
        given:
        memberRepository.findByEmail(_ as String) >> Optional.empty()

        when:
        userDetailService.loadUserByUsername("hello")

        then:
        thrown(UsernameNotFoundException)
    }

    def "LoadUserByUsername - 회원이 DB 내에 존재하는 경우"() {
        given:
        Member output = Member.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .image("hh")
                .role(Role.GHOST)
                .build()

        memberRepository.findByEmail(_ as String) >> Optional.of(output)

        when:
        def principals = userDetailService.loadUserByUsername("helloworld")

        then:
        principals in AlgoUser
    }
}
