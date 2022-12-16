package com.lhgpds.algometa.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhgpds.algometa.annotation.WithMockAlgoUser;
import com.lhgpds.algometa.configuration.security.filter.JwtAuthorizationFilter;
import com.lhgpds.algometa.controller.member.dto.RequestUpdateProfile;
import com.lhgpds.algometa.infra.s3.S3Uploader;
import com.lhgpds.algometa.internal.member.domain.vo.Email;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import com.lhgpds.algometa.internal.member.domain.vo.Nickname;
import com.lhgpds.algometa.internal.member.domain.vo.Role;
import com.lhgpds.algometa.internal.member.service.MemberService;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


@WebMvcTest(MemberController.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerDocsTest {

    private static final String MEMBER_DOMAIN_URI = "/api/v1/members";
    private static final String POST_USER_INFO_URI = MEMBER_DOMAIN_URI + "/info";
    private static final String POST_USER_IMAGE_URI = MEMBER_DOMAIN_URI + "/images";
    private static final String GET_USER_MY_PROFILE_URI = MEMBER_DOMAIN_URI + "/me";

    @MockBean
    MemberService memberService;

    @MockBean
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @MockBean
    S3Uploader s3Uploader;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .addFilters(new CharacterEncodingFilter("UTF-8", true))// 필터 추가
            .alwaysDo(print())
            .build();
    }

    @Test
    void findMyProfile() throws Exception {
        // given:
        MemberDto memberDto = MemberDto.builder()
            .id(1L)
            .nickname(Nickname.from("hello"))
            .image(ImageLink.from("s3"))
            .email(Email.from("hello@naver.com"))
            .role(Role.USER)
            .build();
        Mockito.doReturn(memberDto).when(memberService).findById(any());

        // expect:
        mockMvc.perform(get(MEMBER_DOMAIN_URI + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").exists())
            .andExpect(jsonPath("$.image").exists())
            .andExpect(jsonPath("$.nickname").exists())
            .andDo(document("get-members/{id}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("email").description("email"),
                    fieldWithPath("nickname").description("nickname"),
                    fieldWithPath("image").description("image"))
            ));
    }

    @Nested
    @DisplayName("회원가입시 유저 이미지 입력 API Test")
    class UploadImage {

        private static final String VALID_S3_IMAGE_LINK = "s3.imageLink";

        @Test
        @WithMockAlgoUser
        void responseFormatWhenSuccessTest() throws Exception {
            // given:
            MockMultipartFile file = new MockMultipartFile(
                "images",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
            );
            Mockito.doReturn(ImageLink.from(VALID_S3_IMAGE_LINK)).when(s3Uploader).upload(any(), any());
            MemberDto output = MemberDto.builder().image(ImageLink.from(VALID_S3_IMAGE_LINK)).build();
            Mockito.doReturn(output).when(memberService).updateImage(any(), any());

            // expect:
            mockMvc.perform(multipart(POST_USER_IMAGE_URI).file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.image_link").exists());
        }
    }

    @Nested
    @DisplayName("회원가입시 유저 정보 입력 API test")
    class SetUserInfo {

        private static final String VALID_NICKNAME = "가오가이거1214";

        @Test
        @WithMockAlgoUser
        void responseFormatWhenSuccessTest() throws Exception {
            // given:
            RequestUpdateProfile requestUpdateProfile = RequestUpdateProfile.builder()
                .nickname(VALID_NICKNAME).build();
            MemberDto output = MemberDto.builder()
                .email(Email.from("hello@naver.com"))
                .id(1L)
                .nickname(Nickname.from(VALID_NICKNAME))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
            Mockito.doReturn(output).when(memberService).updateProfile(any(), any());

            // expect
            mockMvc.perform(post(POST_USER_INFO_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestUpdateProfile)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.modified_at").exists());
        }

        @Nested
        @DisplayName("유저 본인 요청 API 테스트")
        class FindMyProfile {

            private static final String VALID_NICKNAME = "가오가이거1214";

            @Test
            @WithMockAlgoUser
            void responseFormatWhenSuccessTest() throws Exception {
                // given:

                // expect
                mockMvc.perform(get(GET_USER_MY_PROFILE_URI))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.email").exists())
                    .andExpect(jsonPath("$.nickname").exists())

                    .andExpect(jsonPath("$.image").exists())
                    .andExpect(jsonPath("$.role").exists())
                    .andExpect(jsonPath("$.created_at").exists())
                    .andExpect(jsonPath("$.modified_at").exists());
            }
        }
    }
}
