package com.lhgpds.algometa.configuration.security.filter;

import com.lhgpds.algometa.internal.auth.jwt.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authorization 헤더에서 JWT를 추출하고 구문 분석한 다음 JWT의 사용자 이름을 사용하여 사용자 세부 정보를 로드합니다. 사용자 세부 정보를 로드할 수 있는 경우
 * 필터는 인증 개체를 만들고 SecurityContext에 설정합니다. 사용자 세부 정보를 로드할 수 없는 경우 필터는 클라이언트에 401 Unauthorized 응답을
 * 보냅니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {

        try {
            // 인증 기준: jwt parsing 가능 여부, 토큰 상태, 가져온 username 정보가 실제로 DB에 존재하는지
            // 인증에 성공하면 인증 객체를 SecurityContextHolder 에 저장한다.
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
            if (bearerToken != null) {
                String accessToken = resolveBearerToken(bearerToken);
                Claims claims = tokenService.parseClaims(accessToken);
                UserDetails principals = userDetailsService.loadUserByUsername(
                    claims.getSubject());
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(principals, "",
                        principals.getAuthorities()));
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendAuthenticationException(request, response, e);
        }

    }

    private String resolveBearerToken(String bearerToken) throws IllegalStateException {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        throw new IllegalStateException();
    }

    private void sendAuthenticationException(HttpServletRequest request,
        HttpServletResponse response, Exception cause)
        throws ServletException, IOException {

        // securityHolder 초기화
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(context);

        AuthenticationException authenticationException = new InsufficientAuthenticationException(
            "token 인증 실패", cause);
        authenticationEntryPoint.commence(request, response, authenticationException);
    }


}