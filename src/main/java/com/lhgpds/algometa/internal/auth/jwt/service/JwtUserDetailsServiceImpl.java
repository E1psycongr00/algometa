package com.lhgpds.algometa.internal.auth.jwt.service;

import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.internal.member.domain.entity.Member;
import com.lhgpds.algometa.internal.member.domain.vo.Email;
import com.lhgpds.algometa.internal.member.repository.MemberRepository;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import com.lhgpds.algometa.mapper.MemberMapper;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * username으로 부터 회원이 존재하는지 여부를 확인하고 존재하는 경우, 유저정보 객체를 포함한 principal 리턴
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(new Email(username))
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디로 로그인 시도를 했습니다"));
        MemberDto memberDto = MemberMapper.instance.convertToMemberDto(member);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(memberDto.getRole().getRoleName()));
        return new AlgoUser(username, "", authorities, memberDto);
    }
}
