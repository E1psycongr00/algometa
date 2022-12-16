package com.lhgpds.algometa.internal.auth.oauth2.service;

import com.lhgpds.algometa.internal.auth.oauth2.OAuth2Attribute;
import com.lhgpds.algometa.internal.member.domain.vo.Role;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * OAuth2UserService 인터페이스를 구현하고 OAuth2 공급자로부터 사용자 정보를 로드하는 데 사용되는 클래스
 */
@Slf4j
@Service
public class CustomOAuth2UserServiceImpl implements
    OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    /**
     * > OAuth2에 로그인 시 Resource Owner의 정보를 principal 형태로 바꾸어 반환
     *
     * @param userRequest 로그인을 시도하는 사용자의 정보가 포함된 요청 개체
     * @return DefaultOAuth2User 개체
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2Attribute oAuth2Attribute =
            OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        log.info("{}", oAuth2Attribute);

        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(Role.GHOST.getRoleName())),
            memberAttribute, "email");
    }
}
