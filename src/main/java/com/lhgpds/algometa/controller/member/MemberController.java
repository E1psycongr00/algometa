package com.lhgpds.algometa.controller.member;

import com.lhgpds.algometa.controller.common.dto.ResponseCommon;
import com.lhgpds.algometa.controller.member.dto.RequestUpdateProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseMyProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseProfile;
import com.lhgpds.algometa.controller.member.dto.ResponseUploadImage;
import com.lhgpds.algometa.infra.s3.S3Uploader;
import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import com.lhgpds.algometa.internal.member.service.MemberService;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import com.lhgpds.algometa.mapper.MemberMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestControllerAdvice
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final S3Uploader s3Uploader;
    private final MemberService memberService;

    /**
     * 회원가입시 user 정보를 setting 하는 API. user 정보를 수정하는 API로 사용할 수 있다.
     *
     * @param algoUser             user principal
     * @param requestUpdateProfile 요청 body
     * @return ResponseEntity<ResponseCommon>
     */
    @Secured("ROLE_GHOST")
    @PostMapping("/info")
    public ResponseEntity<ResponseCommon> setUserInfo(@AuthenticationPrincipal AlgoUser algoUser,
        @RequestBody @Validated RequestUpdateProfile requestUpdateProfile) {
        MemberDto userInfo = algoUser.getMemberDto();
        MemberDto requestDto = MemberMapper.instance.convertToMemberDto(requestUpdateProfile);
        MemberDto responseDto = memberService.updateProfile(userInfo, requestDto);
        ResponseCommon response = MemberMapper.instance.convertToResponseCommon(responseDto);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * 회원 프로필 사진 업로드 API. 수정할 떄도 사용할 수 있는 API다.
     *
     * @param algoUser      user principla
     * @param multipartFile 프로필 사진 파일
     * @return ResponseEntity<ResponseUploadImage>
     * @throws IOException s3 업로드시 inputstream 예외
     */
    @Secured("ROLE_GHOST")
    @PostMapping("/images")
    public ResponseEntity<ResponseUploadImage> uploadImage(
        @AuthenticationPrincipal AlgoUser algoUser,
        @RequestParam("images") MultipartFile multipartFile)
        throws IOException {
        MemberDto userInfo = algoUser.getMemberDto();
        ImageLink uploadImageLink = s3Uploader.upload(multipartFile, String.valueOf(userInfo.getId()));
        MemberDto requestDto = MemberDto.builder().image(uploadImageLink).build();
        MemberDto responseDto = memberService.updateImage(userInfo, requestDto);
        ResponseUploadImage responseUploadImage = MemberMapper.instance.convertToResponseUploadImage(
            responseDto);
        return ResponseEntity.status(201).body(responseUploadImage);
    }

    /**
     * > ID를 통해 회원 프로필을 조회하는 API
     *
     * @param id PathVariable member id
     * @return ResponseEntity<ResponseProfile>
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseProfile> findProfile(
        @PathVariable Long id) {

        MemberDto requestDto = MemberDto.builder().id(id).build();
        MemberDto responseDto = memberService.findById(requestDto);
        ResponseProfile responseProfile = MemberMapper.instance.convertToResponseUpdateProfile(
            responseDto);
        return ResponseEntity.ok(responseProfile);
    }

    /**
     * 로그인 이후 회원의 정보를 가져오는 API
     *
     * @param algoUser UserPrincipal
     * @return ResponseEntity<ResponseProfile>
     */
    @Secured("ROLE_GHOST")
    @GetMapping("/me")
    public ResponseEntity<ResponseMyProfile> findMyProfile(
        @AuthenticationPrincipal AlgoUser algoUser) {

        MemberDto userInfo = algoUser.getMemberDto();
        ResponseMyProfile response = MemberMapper.instance.convertToResponseMyProfile(userInfo);
        return ResponseEntity.ok(response);
    }
}
