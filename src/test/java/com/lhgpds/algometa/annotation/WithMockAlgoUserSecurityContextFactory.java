package com.lhgpds.algometa.annotation;

import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.internal.member.domain.vo.Email;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import com.lhgpds.algometa.internal.member.domain.vo.Nickname;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAlgoUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockAlgoUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockAlgoUser customAlgoUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        MemberDto memberDto = MemberDto.builder()
            .id(customAlgoUser.id())
            .email(new Email(customAlgoUser.email()))
            .nickname(new Nickname(customAlgoUser.nickname()))
            .image(new ImageLink(customAlgoUser.image()))
            .role(customAlgoUser.role())
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .build();
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(customAlgoUser.role().getRoleName())
        );
        AlgoUser principals = new AlgoUser(customAlgoUser.email(), "", authorities, memberDto);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principals, "",
            authorities);

        context.setAuthentication(authentication);
        return context;
    }
}
