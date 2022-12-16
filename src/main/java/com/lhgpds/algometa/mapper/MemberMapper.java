package com.lhgpds.algometa.mapper;

import com.lhgpds.algometa.controller.common.dto.ResponseCommon;
import com.lhgpds.algometa.controller.member.dto.RequestUpdateProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseMyProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseUploadImage;
import com.lhgpds.algometa.internal.member.domain.entity.Member;
import com.lhgpds.algometa.internal.member.domain.vo.Email;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import com.lhgpds.algometa.internal.member.domain.vo.Nickname;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    MemberMapper instance = Mappers.getMapper(MemberMapper.class);

    Member convertToEntity(MemberDto memberDto);

    MemberDto convertToMemberDto(Member entity);

    MemberDto convertToMemberDto(RequestUpdateProfile entity);

    ResponseProfile convertToResponseUpdateProfile(MemberDto memberDto);

    ResponseCommon convertToResponseCommon(MemberDto memberDto);

    ResponseUploadImage convertToResponseUploadImage(MemberDto memberDto);

    ResponseMyProfile convertToResponseMyProfile(MemberDto memberDto);

    default String fromEmail(Email email) {
        return email.toString();
    }

    default Email toEmail(String email) {
        return Email.from(email);
    }

    default String fromNickname(Nickname nickname) {
        return nickname == null ? null : nickname.toString();
    }

    default Nickname toNickname(String nickname) {
        return nickname == null ? null : Nickname.from(nickname);
    }

    default String fromImageLink(ImageLink imageLink) {
        return imageLink == null ? null : imageLink.toString();
    }

    default ImageLink toImageLink(String imageLink) {
        return imageLink == null ? null : ImageLink.from(imageLink);
    }
}
