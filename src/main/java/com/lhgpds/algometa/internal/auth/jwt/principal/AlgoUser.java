package com.lhgpds.algometa.internal.auth.jwt.principal;


import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class AlgoUser extends User {

    private final MemberDto memberDto;

    public AlgoUser(String username, String password,
        Collection<? extends GrantedAuthority> authorities, MemberDto memberDto) {
        super(username, password, authorities);

        this.memberDto = memberDto;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }
}
