package com.lhgpds.algometa.internal.auth.jwt.service

import com.lhgpds.algometa.controller.auth.dto.TokenDto
import com.lhgpds.algometa.internal.auth.jwt.JwtProperties
import com.lhgpds.algometa.internal.member.domain.vo.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import spock.lang.Specification


@SpringJUnitConfig(Config.class)
@Import(JwtTokenService.class)
class JwtTokenServiceTest extends Specification {

    @Autowired
    JwtTokenService tokenService

    def authorities = Collections.singleton(new SimpleGrantedAuthority(Role.GHOST.roleName))

    def "parseClaims - 잘못된 토큰이 입력된 경우"() {
        given:
        String INVALID_ACCESS_TOKEN = "123123123azxfq313aaaaa"

        when:
        tokenService.parseClaims(INVALID_ACCESS_TOKEN)

        then:
        thrown(JwtException)
    }

    def "generateToken에서 parseClaims 까지 정상적인 토큰 입력 검증"() {
        given:
        String EMAIL = "hello@naver.com"
        UserDetails principal = new User(EMAIL, "", authorities)
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "")

        when:
        TokenDto tokenDto = tokenService.generateToken(authentication)
        String accessToken = tokenDto.getAccessToken()
        Claims result = tokenService.parseClaims(accessToken)

        then:
        result.getSubject() == EMAIL
        result in Claims
    }


    @TestConfiguration
    static class Config {
        @Bean
        static JwtProperties jwtProperties() {
            final JwtProperties properties = new JwtProperties()
            properties.setSecretKey("linkywayteamisgoodlinkywayteamisgoodlinkywayteamisgoodlinkywayteamisgoodlinkywayteamisgoodlinkywayteamisgoodlinkywayteamisgood")
            properties.setTokenPeriod(600000)
            properties.setRefreshTokenPeriod(2592000000)
            return properties
        }
    }
}
