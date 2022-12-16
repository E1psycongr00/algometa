package com.lhgpds.algometa.internal.member.service;

import com.lhgpds.algometa.exception.common.member.MemberDuplicateException;
import com.lhgpds.algometa.exception.common.member.MemberNotFoundException;
import com.lhgpds.algometa.internal.member.domain.entity.Member;
import com.lhgpds.algometa.internal.member.domain.vo.Role;
import com.lhgpds.algometa.internal.member.repository.MemberRepository;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import com.lhgpds.algometa.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberDto join(MemberDto request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberDuplicateException();
        }
        Member member = Member.createFirstJoinMember(request.getEmail(), request.getNickname());
        memberRepository.save(member);
        return MemberMapper.instance.convertToMemberDto(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDto findByEmail(MemberDto request) {
        Member entity = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(MemberNotFoundException::new);
        return MemberMapper.instance.convertToMemberDto(entity);
    }

    @Transactional
    @Override
    public MemberDto findById(MemberDto request) {
        Member entity = memberRepository.findById(request.getId())
            .orElseThrow(MemberNotFoundException::new);
        return MemberMapper.instance.convertToMemberDto(entity);
    }

    @Transactional
    @Override
    public MemberDto updateProfile(MemberDto userInfo, MemberDto request) {
        Member entity = memberRepository.findById(
                userInfo.getId())
            .orElseThrow(MemberNotFoundException::new);

        if (request.getNickname() != null) {
            entity.setNickname(request.getNickname());
        }
        entity.changeRole(Role.USER);
        return MemberMapper.instance.convertToMemberDto(entity);
    }

    @Transactional
    @Override
    public MemberDto updateImage(MemberDto userInfo, MemberDto request) {
        Member entity = memberRepository.findById(userInfo.getId())
            .orElseThrow(MemberNotFoundException::new);
        entity.setImage(request.getImage());
        entity.changeRole(Role.USER);
        return MemberMapper.instance.convertToMemberDto(entity);
    }
}
