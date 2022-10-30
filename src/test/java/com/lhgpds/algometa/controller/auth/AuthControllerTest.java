package com.lhgpds.algometa.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
import com.lhgpds.algometa.internal.member.entity.vo.Role;
import com.lhgpds.algometa.internal.member.service.MemberService;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerTest {

    @MockBean
    MemberService memberService;

    @MockBean
    JwtAuthorizationFilter jwtAuthorizationFilter;

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
    @WithMockAlgoUser(email = "hello@naver.com", role = Role.GHOST)
    void setNickName() throws Exception {
        // given:
        RequestUpdateProfile requestDto = RequestUpdateProfile.builder()
            .nickname("hello")
            .build();
        String request = objectMapper.writeValueAsString(requestDto);
        MemberDto output = MemberDto.builder()
            .id(1L)
            .email("hello@naver.com")
            .nickname("hello")
            .build();
        Mockito.doReturn(output).when(memberService).updateProfile(any(), any());

        //expect:
        mockMvc.perform(post("/api/v1/auth/set")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andDo(document("post-auth/set",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("id").description("id")
                )));
    }
}