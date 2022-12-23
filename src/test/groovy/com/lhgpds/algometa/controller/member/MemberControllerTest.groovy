package com.lhgpds.algometa.controller.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.lhgpds.algometa.annotation.WithMockAlgoUser
import com.lhgpds.algometa.configuration.security.filter.JwtAuthorizationFilter
import com.lhgpds.algometa.controller.member.dto.RequestUpdateProfile
import com.lhgpds.algometa.infrastructure.s3.S3Uploader
import com.lhgpds.algometa.internal.member.domain.vo.Email
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink
import com.lhgpds.algometa.internal.member.domain.vo.Nickname
import com.lhgpds.algometa.internal.member.application.MemberService
import com.lhgpds.algometa.internal.member.application.dto.MemberDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController.class)
class ControllerTest extends Specification {

    class Uri {
        private static final String MEMBER_DOMAIN_URI = "/api/v1/members"
        private static final String POST_USER_INFO_URI = MEMBER_DOMAIN_URI + "/info"
        private static final String POST_USER_IMAGE_URI = MEMBER_DOMAIN_URI + "/images"
    }

    class Request {
        private static final String LOW_SIZE_NICKNAME = "A1"
        private static final String FIRST_IS_NUMBER_NICKNAME = "1A가오가이거V"
        private static final String VALID_NICKNAME = "가오가이거1214"
        private static final String EXCEED_SIZE_NICKNAME = "rkasasdaafaasd12"

        private static final String INVALID_MULTIPART_KEY = "file"
        private static final String VALID_MULTIPART_KEY = "images"
        private static final String PNG = MediaType.IMAGE_PNG
    }


    @MockBean
    MemberService memberService

    @MockBean
    JwtAuthorizationFilter jwtAuthorizationFilter

    @MockBean
    S3Uploader s3Uploader

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    WebApplicationContext ctx


    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build()
    }

    @WithMockAlgoUser
    def "setUserInfo - 제약조건 및 응답 상태 테스트"() {
        given:
        RequestUpdateProfile requestUpdateProfile = RequestUpdateProfile.builder()
                .nickname(nickname).build()
        memberService.updateProfile(_ as MemberDto, _ as MemberDto) >> MemberDto.builder()
                .id(1L).build()

        expect:
        mockMvc.perform(post(Uri.POST_USER_INFO_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUpdateProfile)))
                .andExpect(status().is(statusResult))

        where:
        nickname                         | statusResult
        Request.LOW_SIZE_NICKNAME        | 400
        Request.FIRST_IS_NUMBER_NICKNAME | 400
        Request.EXCEED_SIZE_NICKNAME     | 400
        Request.VALID_NICKNAME           | 201
    }

    @WithMockAlgoUser
    def "setUserImage - 제약조건 및 응답 상태 테스트"() {
        given:
        def b = new byte[byteSize]
        def file = new MockMultipartFile(
                keyName,
                originalFileName,
                mediaType,
                b
        )
        memberService.updateImage(_ as MemberDto, _ as MemberDto) >> MemberDto.builder()
                .nickname(Nickname.from("asdfa"))
                .email(Email.from("asdf@naver.com"))
                .image(ImageLink.from("s3s3"))
                .build()

        expect:
        mockMvc.perform(multipart(Uri.POST_USER_IMAGE_URI)
                .file(file))
                .andExpect(status().is(statusResult))

        where:
        keyName                       | originalFileName | mediaType   | byteSize || statusResult
        Request.INVALID_MULTIPART_KEY | "h.png"          | Request.PNG | 50       || 400
        Request.VALID_MULTIPART_KEY   | "h.png"          | Request.PNG | 500      || 201
    }
}
