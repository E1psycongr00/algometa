package com.lhgpds.algometa.configuration.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhgpds.algometa.controller.auth.dto.TokenDto;
import com.lhgpds.algometa.exception.common.DuplicateException;
import com.lhgpds.algometa.internal.auth.jwt.service.JwtTokenService;
import com.lhgpds.algometa.internal.member.service.MemberService;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import com.lhgpds.algometa.mapper.OAuth2Mapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 이 클래스는 OAuth2 로그인을 성공하면 토큰을 생성하여 클라이언트에 보내는 역할을 수행
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService tokenService;
    private final ObjectMapper objectMapper;

    private final MemberService memberService;

    /**
     * 사용자가 새 사용자인 경우 DB에 새 사용자를 만들고 토큰을 생성합니다. 사용자가 이미 등록되어 있는 경우 토큰만 생성합니다. 그 이후 Client에 응답할 token
     * 정보를 response에 넣어줍니다.
     *
     * @param request        HttpServlet요청
     * @param response       HttpServlet응답
     * @param authentication 인증 프로세스 중에 생성된 인증 개체
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        MemberDto memberDto = OAuth2Mapper.convertMemberDto(oAuth2User);
        try {
            MemberDto dto = memberService.join(memberDto);
            Authentication responseAuthentication = makeResponseAuthentication(dto);
            TokenDto token = tokenService.generateToken(responseAuthentication);
            writeTokenResponse(response, token, true);
            SecurityContextHolder.clearContext();

        } catch (DuplicateException e) {
            log.error(e.getClass().getSimpleName(), e.getMessage());
            MemberDto existedMember = memberService.findByEmail(memberDto);
            Authentication responseAuthentication = makeResponseAuthentication(existedMember);
            TokenDto token = tokenService.generateToken(responseAuthentication);
            writeTokenResponse(response, token, false);
            SecurityContextHolder.clearContext();
        }

    }

    private Authentication makeResponseAuthentication(MemberDto member) {
        UserDetails principals = new User(member.getEmail(), "", Collections.singletonList(
            new SimpleGrantedAuthority(member.getRole().getRoleName())
        ));
        return new UsernamePasswordAuthenticationToken(principals, "");
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDto token, boolean isCreated) {
        response.setContentType("text/html;charset=UTF-8");

        if (isCreated) {
            response.setStatus(HttpStatus.CREATED.value());
        } else {
            response.setStatus(HttpStatus.OK.value());
        }
        createTokenCookies(response, token);
    }

    private void createTokenCookies(HttpServletResponse response, TokenDto token) {
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        Cookie accessTokenCookie = makeCookie("access_token", accessToken);
        Cookie refreshTokenCookie = makeCookie("refresh_token", refreshToken);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private Cookie makeCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(5);
        return cookie;
    }
}
