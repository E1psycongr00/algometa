package com.lhgpds.algometa.mapper;

import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class OAuth2Mapper {

    public static MemberDto convertMemberDto(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Long id = null;
        if (attributes.containsKey("id") && !(attributes.get("id") instanceof String)) {
            id = (Long) attributes.get("id");
        }
        return MemberDto.builder()
            .id(id)
            .email((String) attributes.get("email")).build();
    }

    public static OAuth2User convertOAuth2User(MemberDto memberDto) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", memberDto.getId());
        attributes.put("email", memberDto.getEmail());
        attributes.put("nickname", memberDto.getNickname());
        attributes.put("image", memberDto.getImage());
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(memberDto.getRole().getRoleName())),
            attributes, "email");
    }
}
