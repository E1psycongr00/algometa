package com.lhgpds.algometa.configuration.security.filter

import com.lhgpds.algometa.configuration.security.handler.DefaultAuthenticationEntryPoint
import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser
import com.lhgpds.algometa.internal.auth.jwt.service.JwtTokenService
import com.lhgpds.algometa.internal.member.entity.vo.Role
import com.lhgpds.algometa.internal.member.service.dto.MemberDto
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

import javax.servlet.FilterChain

class JwtAuthorizationFilterTest extends Specification {

    def AUTHORIZATION_HEADER = "Authorization"

    MockHttpServletRequest httpServletRequest
    MockHttpServletResponse httpServletResponse

    def jwtTokenService = Mock(JwtTokenService)
    def userDetailsService = Mock(UserDetailsService)
    def authenticationEntryPoint = Mock(DefaultAuthenticationEntryPoint)
    def filterChain = Mock(FilterChain)
    JwtAuthorizationFilter jwtAuthorizationFilter

    void setup() {
        httpServletRequest = new MockHttpServletRequest()
        httpServletRequest.addHeader("Accept", "application/json")
        httpServletRequest.setContentType(MediaType.APPLICATION_JSON_VALUE)
        jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtTokenService,
                userDetailsService, authenticationEntryPoint)

        httpServletResponse = new MockHttpServletResponse()
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE)
    }

    def "doFilterInternal - jwt가 bearer로 들어오지 않은 경우"() {
        given:
        httpServletRequest.addHeader(AUTHORIZATION_HEADER, "1234")
        jwtTokenService.parseClaims(_ as String) >> {
            String token -> throw new JwtException(_ as String)
        }

        when:
        jwtAuthorizationFilter.doFilterInternal(httpServletRequest,
                httpServletResponse, filterChain)

        then: "예외시 authenticaitonEntryPoint가 무조껀 호출되어야함"
        1 * authenticationEntryPoint.commence(httpServletRequest,
                httpServletResponse, _ as AuthenticationException)
    }

    def "doFilterInternal - jwt 파싱 실패"() {
        given:
        httpServletRequest.addHeader(AUTHORIZATION_HEADER, "Bearer 1234")
        jwtTokenService.parseClaims(_ as String) >> {
            String token -> throw new JwtException(_ as String)
        }

        when:
        jwtAuthorizationFilter.doFilterInternal(httpServletRequest,
                httpServletResponse, filterChain)

        then: "예외시 authenticaitonEntryPoint가 무조껀 호출되어야함"
        1 * authenticationEntryPoint.commence(httpServletRequest,
                httpServletResponse, _ as AuthenticationException)
    }

    def "doFilterInternal - jwt 회원 인증 실패"() {
        given:
        httpServletRequest.addHeader(AUTHORIZATION_HEADER, "Bearer 1234")
        userDetailsService.loadUserByUsername(_ as String) >> {
            String email -> throw new UsernameNotFoundException(_ as String)
        }

        when:
        jwtAuthorizationFilter.doFilterInternal(httpServletRequest,
                httpServletResponse, filterChain)

        then: "예외시 authenticaitonEntryPoint가 무조껀 호출되어야함"
        1 * authenticationEntryPoint.commence(httpServletRequest,
                httpServletResponse, _ as AuthenticationException)
    }

    def "doFilterInternal - jwt 토큰으로부터 회원 인증 성공"() {
        given:
        httpServletRequest.addHeader(AUTHORIZATION_HEADER, "Bearer 1234")
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(Role.USER.roleName)
        )
        MemberDto memberDto = MemberDto.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .build()
        jwtTokenService.parseClaims(_ as String) >> Jwts.claims(new HashMap<String, Object>())
        userDetailsService.loadUserByUsername(_) >> new AlgoUser(
                "helloworld@naver.com", "", authorities, memberDto
        )

        when:
        jwtAuthorizationFilter.doFilterInternal(httpServletRequest,
                httpServletResponse, filterChain)

        then: "예외시 authenticaitonEntryPoint가 무조껀 호출 X 그리고 SecurityContext가 비어있어야함"
        0 * authenticationEntryPoint.commence(httpServletRequest,
                httpServletResponse, _ as AuthenticationException)
        SecurityContextHolder.getContext().getAuthentication() != null
    }
}
