package com.lhgpds.algometa.configuration.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhgpds.algometa.exception.ErrorCode;
import com.lhgpds.algometa.exception.dto.ResponseErrorDto;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // TODO JwtAuthorizationFilter가 없어지고 AOP로 구현되면 사라질 예정
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ResponseErrorDto responseErrorDto = ResponseErrorDto.of(ErrorCode.UNAUTHORIZED,
            authException.getMessage(),
            request.getRequestURI());
        String stringResponse = objectMapper.writeValueAsString(responseErrorDto);
        out.println(stringResponse);
    }
}
