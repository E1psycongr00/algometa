package com.lhgpds.algometa.controller.problem;

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
import com.lhgpds.algometa.configuration.jackson.JacksonConfiguration;
import com.lhgpds.algometa.configuration.security.filter.JwtAuthorizationFilter;
import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Language;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Status;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import com.lhgpds.algometa.internal.problem.application.ProblemService;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(ProblemController.class)
@ExtendWith(RestDocumentationExtension.class)
@Import(JacksonConfiguration.class)
class ProblemControllerDocsTest {

    private static final String PROBLEM_URI = "/api/v1/problems";
    private static final String POST_ADD_PROBLEM_URI = PROBLEM_URI;

    @MockBean
    ProblemService problemService;

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
    @WithMockAlgoUser
    void addProblemTest() throws Exception {
        // given:
        String input = "{\n"
            + "\"content\":{\n"
            + "\"title\":\"프로그래머스\",\n"
            + "\"link\":\"link\",\n"
            + "\"main_text\":\"hello world\"\n"
            + "},\n"
            + "\"code\":{\n"
            + "\"source_code\":\"hello world\",\n"
            + "\"lang\":\"JAVA\",\n"
            + "\"spend_time\":\"00:12:24\",\n"
            + "\"status\":\"SUCCESS\",\n"
            + "\"difficulty\":\"HARD\"\n"
            + "},\n"
            + "\"categories\":[\n"
            + "\"ARRAY\",\n"
            + "\"GEOMETRY\"\n"
            + "],\n"
            + "\"platform\":\"BACKJOON\"\n"
            + "}";

        Code code = Code.of("asdf", Language.JAVA, "00:12:24", Status.FAIL, Difficulty.EASY);
        Content content = Content.of("title", "link", "mainmainText");
        ProblemDto problemDto = ProblemDto.builder()
            .problemId(ProblemId.nextProblemId())
            .code(code)
            .memberId(1L)
            .content(content)
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .platform(Platform.BACKJOON)
            .category(new LinkedHashSet<>(List.of(Category.ARRAY)))
            .build();
        Mockito.doReturn(problemDto).when(problemService).addProblem(any(), any());

        // expect:
        mockMvc.perform(post(POST_ADD_PROBLEM_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.created_at").exists())
            .andExpect(jsonPath("$.modified_at").exists())
            .andExpect(jsonPath("$.problem_id").exists())
            .andDo(document("post-problems",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("created_at").description("생성 날짜"),
                    fieldWithPath("modified_at").description("수정 날짜"),
                    fieldWithPath("problem_id").description("문제 id"))
            ));
    }
}