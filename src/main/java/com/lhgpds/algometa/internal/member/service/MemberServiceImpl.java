package com.lhgpds.algometa.internal.member.service;

import com.lhgpds.algometa.exception.common.DuplicateException;
import com.lhgpds.algometa.exception.common.NotFoundException;
import com.lhgpds.algometa.internal.member.entity.Member;
import com.lhgpds.algometa.internal.member.entity.vo.Role;
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
    public MemberDto join(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicateException("이미 회원이 존재합니다");
        }
        memberDto.setRole(Role.GHOST);
        Member memberEntity = MemberMapper.instance.convertToEntity(memberDto);
        memberRepository.save(memberEntity);
        return MemberMapper.instance.convertToMemberDto(memberEntity);
    }

    @Transactional
    @Override
    public MemberDto findByEmail(MemberDto memberDto) {
        Member entity = memberRepository.findByEmail(memberDto.getEmail())
            .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다"));
        return MemberMapper.instance.convertToMemberDto(entity);
    }

    @Transactional
    @Override
    public MemberDto updateProfile(MemberDto userInfoDto, MemberDto requestDto) {
        Member entity = memberRepository.findById(
                userInfoDto.getId())
            .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다"));

        if (requestDto.getNickname() != null) {
            entity.setNickname(requestDto.getNickname());
        }
        if (requestDto.getImage() != null) {
            entity.setImage(requestDto.getImage());
        }
        entity.setRole(Role.USER);
        return MemberMapper.instance.convertToMemberDto(entity);
    }
}
