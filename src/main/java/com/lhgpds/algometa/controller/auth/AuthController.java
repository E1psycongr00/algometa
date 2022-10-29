package com.lhgpds.algometa.controller.auth;

import com.lhgpds.algometa.controller.common.dto.ResponseCommon;
import com.lhgpds.algometa.controller.member.dto.RequestUpdateProfile;
import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.internal.member.service.MemberService;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import com.lhgpds.algometa.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;


    @Secured("ROLE_GHOST")
    @PostMapping("/set")
    public ResponseEntity<ResponseCommon> setNickName(@AuthenticationPrincipal AlgoUser algoUser,
        @RequestBody RequestUpdateProfile requestUpdateProfile) {
        MemberDto userInfo = algoUser.getMemberDto();
        MemberDto requestDto = MemberMapper.instance.convertToMemberDto(requestUpdateProfile);
        MemberDto responseDto = memberService.updateProfile(userInfo, requestDto);
        ResponseCommon response = MemberMapper.instance.convertToResponseCommon(responseDto);
        return ResponseEntity.ok(response);
    }
}
