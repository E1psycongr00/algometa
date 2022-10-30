package com.lhgpds.algometa.configuration.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.lhgpds.algometa.controller.auth.dto.TokenDto
import com.lhgpds.algometa.exception.common.DuplicateException
import com.lhgpds.algometa.internal.auth.jwt.service.JwtTokenService
import com.lhgpds.algometa.internal.member.entity.vo.Role
import com.lhgpds.algometa.internal.member.service.MemberServiceImpl
import com.lhgpds.algometa.internal.member.service.dto.MemberDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import spock.lang.Specification

class OAuth2SuccessHandlerTest extends Specification {

    MockHttpServletRequest httpServletRequest
    MockHttpServletResponse httpServletResponse

    def tokenService = Mock(JwtTokenService)
    def memberService = Mock(MemberServiceImpl)
    def objectMapper = new ObjectMapper()
    OAuth2SuccessHandler oAuth2SuccessHandler

    void setup() {
        httpServletRequest = new MockHttpServletRequest()
        httpServletRequest.addHeader("Accept", "application/json")
        httpServletRequest.setContentType(MediaType.APPLICATION_JSON_VALUE)
        oAuth2SuccessHandler = new OAuth2SuccessHandler(tokenService, objectMapper, memberService)

        httpServletResponse = new MockHttpServletResponse()
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE)
    }

    def "OnAuthenticationSuccess - oAuth 인증에 성공하고 DB에 데이터가 없는 경우"() {
        given:
        Map<String, Object> attributes = new HashMap<>()
        attributes.put("email", "helloworld@naver.com")
        OAuth2User oAuth2User = new DefaultOAuth2User(Collections.singletonList(
                new SimpleGrantedAuthority(Role.GHOST.roleName)), attributes, "email")
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, "")

        memberService.join(_ as MemberDto) >> MemberDto.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .role(Role.GHOST)
                .build()
        tokenService.generateToken(_ as Authentication) >> TokenDto.builder()
                .accessToken("1234")
                .refreshToken("5678")
                .build()

        when:
        oAuth2SuccessHandler.onAuthenticationSuccess(httpServletRequest,
                httpServletResponse, authentication)

        then: "http 상태 CREATED 및 responseBody가 tokenDto 타입인지 확인"
        httpServletResponse.getStatus() == HttpStatus.CREATED.value()
        objectMapper.readValue(httpServletResponse.getContentAsString(), TokenDto.class)

    }

    def "OnAuthenticationSuccess - oAuth 인증에 성공하고 DB에 데이터가 이미 존재하는 경우"() {
        given:
        Map<String, Object> attributes = new HashMap<>()
        attributes.put("email", "helloworld@naver.com")
        OAuth2User oAuth2User = new DefaultOAuth2User(Collections.singletonList(
                new SimpleGrantedAuthority(Role.GHOST.roleName)), attributes, "email")
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, "")

        memberService.join(_ as MemberDto) >> { MemberDto memberDto ->
            throw new DuplicateException("이미 회원이 존재합니다")
        }
        memberService.findByEmail(_ as MemberDto) >> MemberDto.builder()
                .id(1L)
                .email("helloworld@naver.com")
                .role(Role.GHOST)
                .build()
        tokenService.generateToken(_ as Authentication) >> TokenDto.builder()
                .accessToken("1234")
                .refreshToken("5678")
                .build()

        when:
        oAuth2SuccessHandler.onAuthenticationSuccess(httpServletRequest,
                httpServletResponse, authentication)

        then: "http 상태 OK 및 responseBody가 tokenDto 타입인지 확인"
        httpServletResponse.getStatus() == HttpStatus.OK.value()
        objectMapper.readValue(httpServletResponse.getContentAsString(), TokenDto.class)

    }
}
