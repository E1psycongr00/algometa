package com.lhgpds.algometa.internal.member.service;

import com.lhgpds.algometa.internal.member.service.dto.MemberDto;

/**
 * 회원 서비스 interface
 * 회원관련 기능을 제공한다.
 */
public interface MemberService {

    MemberDto join(MemberDto memberDto);

    MemberDto findByEmail(MemberDto memberDto);

    MemberDto updateProfile(MemberDto userInfoDto, MemberDto memberDto);
}
