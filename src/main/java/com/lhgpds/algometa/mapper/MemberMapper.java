package com.lhgpds.algometa.mapper;

import com.lhgpds.algometa.controller.auth.dto.TokenDto;
import com.lhgpds.algometa.controller.common.dto.ResponseCommon;
import com.lhgpds.algometa.controller.member.dto.RequestUpdateProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseUploadImage;
import com.lhgpds.algometa.internal.member.entity.Member;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    MemberMapper instance = Mappers.getMapper(MemberMapper.class);

    Member convertToEntity(MemberDto memberDto);

    MemberDto convertToMemberDto(Member entity);

    MemberDto convertToMemberDto(RequestUpdateProfile entity);

    ResponseProfile convertToResponseUpdateProfileWithToken(MemberDto memberDto,
        TokenDto jwtToken);

    ResponseProfile convertToResponseUpdateProfile(MemberDto memberDto);

    ResponseCommon convertToResponseCommon(MemberDto memberDto);

    ResponseUploadImage convertToResponseUploadImage(MemberDto memberDto);
}
