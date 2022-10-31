package com.lhgpds.algometa.internal.member.service;

import com.lhgpds.algometa.internal.member.service.dto.MemberDto;

/**
 * 회원 서비스 interface
 * 회원관련 기능을 제공한다.
 */
public interface MemberService {

    MemberDto join(MemberDto request);

    MemberDto findByEmail(MemberDto request);

    MemberDto findById(MemberDto request);

    MemberDto updateProfile(MemberDto userInfoDto, MemberDto reqeust);

    MemberDto updateImage(MemberDto userInfo, MemberDto request);
}
