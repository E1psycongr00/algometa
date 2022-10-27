package com.lhgpds.algometa.configuration;

import com.lhgpds.algometa.configuration.security.filter.JwtAuthorizationFilter;
import com.lhgpds.algometa.configuration.security.handler.DefaultAuthenticationEntryPoint;
import com.lhgpds.algometa.configuration.security.handler.OAuth2SuccessHandler;
import com.lhgpds.algometa.internal.auth.oauth2.service.CustomOAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final CustomOAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .headers().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(defaultAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .oauth2Login()
            .successHandler(oAuth2SuccessHandler)
            .userInfoEndpoint()
            .userService(oAuth2UserService);
        return http.addFilterBefore(jwtAuthorizationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
