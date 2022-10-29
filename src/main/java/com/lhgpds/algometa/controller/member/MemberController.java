package com.lhgpds.algometa.controller.member;


import com.lhgpds.algometa.controller.member.dto.ResponseProfile;
import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    @Secured("ROLE_USER")
    @GetMapping("/me")
    public ResponseEntity<ResponseProfile> findMyProfile(
        @AuthenticationPrincipal AlgoUser algoUser) {
        ResponseProfile responseProfile = MemberMapper.instance.convertToResponseUpdateProfile(
            algoUser.getMemberDto());
        return ResponseEntity.ok(responseProfile);
    }
}
