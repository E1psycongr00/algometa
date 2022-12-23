package com.lhgpds.algometa.controller.problem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
import com.lhgpds.algometa.configuration.web.WebConfiguration;
import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.common.page.Pages;
import com.lhgpds.algometa.internal.problem.application.ProblemService;
import com.lhgpds.algometa.internal.problem.application.dto.HistoryDto;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Language;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Status;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
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
@Import({JacksonConfiguration.class, WebConfiguration.class})
class ProblemControllerDocsTest {

    private static final String PROBLEM_URI = "/api/v1/problems";
    private static final String POST_ADD_PROBLEM_URI = PROBLEM_URI;
    private static final String PUT_UPDATE_CONTENT_URI = PROBLEM_URI + "/%s/contents";
    private static final String PUT_UPDATE_CODE_URI = PROBLEM_URI + "/%s/code";
    private static final String GET_HISTORY_BY_PROBLEM_ID_URI = PROBLEM_URI + "/%s/history/";
    private static final String GET_PROBLEM_URI = PROBLEM_URI + "/%s";

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

    private ProblemDto makeProblemDto() {
        Code code = Code.of("asdf", Language.JAVA, "00:12:24", Status.FAIL, Difficulty.EASY);
        Content content = Content.of("title", "link", "mainmainText");
        return ProblemDto.builder()
            .problemId(ProblemId.nextProblemId())
            .code(code)
            .memberId(1L)
            .content(content)
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .platform(Platform.BACKJOON)
            .category(new LinkedHashSet<>(List.of(Category.ARRAY)))
            .build();
    }

    private HistoryDto makeHistoryDto(ProblemId problemId) {
        Code code = Code.of("asdf", Language.JAVA, "00:12:24", Status.FAIL, Difficulty.EASY);
        return HistoryDto.builder()
            .historyId(HistoryId.nextProblemId())
            .code(code)
            .problemId(problemId)
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
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

        ProblemDto problemDto = makeProblemDto();
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

    @Test
    @WithMockAlgoUser
    void updateContentTest() throws Exception {
        // given:
        ProblemId problemId = ProblemId.nextProblemId();
        String input = "{\n"
            + "    \"content\": {\n"
            + "        \"title\": \"title\",\n"
            + "        \"link\": \"link\",\n"
            + "        \"main_text\": \"text text\"\n"
            + "    }\n"
            + "}";

        ProblemDto problemDto = makeProblemDto();
        Mockito.doReturn(problemDto).when(problemService)
            .updateProblemContent(anyLong(), any(), any());

        mockMvc.perform(put(String.format(PUT_UPDATE_CONTENT_URI, problemId)).contentType(
                MediaType.APPLICATION_JSON).content(input))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.created_at").exists())
            .andExpect(jsonPath("$.modified_at").exists())
            .andExpect(jsonPath("$.problem_id").exists())
            .andDo(document("put-problems/{id}/contents",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("created_at").description("생성 날짜"),
                    fieldWithPath("modified_at").description("수정 날짜"),
                    fieldWithPath("problem_id").description("문제 id"))
            ));
    }

    @Test
    @WithMockAlgoUser
    void updateCodeTest() throws Exception {
        // given:
        String input = "{\n"
            + "    \"code\": {\n"
            + "        \"source_code\": \"System.out.println()\",\n"
            + "        \"lang\": \"JAVA\",\n"
            + "        \"spend_time\": \"00:05:24\",\n"
            + "        \"status\":\"SUCCESS\",\n"
            + "        \"difficulty\": \"HARD\"\n"
            + "    }\n"
            + "}";

        ProblemDto problemDto = makeProblemDto();
        Mockito.doReturn(problemDto).when(problemService)
            .updateProblemCode(anyLong(), any(), any());

        mockMvc.perform(
                put(String.format(PUT_UPDATE_CODE_URI, ProblemId.nextProblemId())).contentType(
                    MediaType.APPLICATION_JSON).content(input))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.created_at").exists())
            .andExpect(jsonPath("$.modified_at").exists())
            .andExpect(jsonPath("$.problem_id").exists())
            .andDo(document("put-problems/{id}/code",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("created_at").description("생성 날짜"),
                    fieldWithPath("modified_at").description("수정 날짜"),
                    fieldWithPath("problem_id").description("문제 id"))
            ));
    }

    @Test
    @WithMockAlgoUser
    void findHistoryByProblemIdTest() throws Exception {
        // given:
        ProblemId problemId = ProblemId.nextProblemId();
        HistoryDto historyDto = makeHistoryDto(problemId);
        Pages<HistoryDto> pages = Pages.of(List.of(historyDto), PageCondition.of(1, 3), 1);
        Mockito.doReturn(pages).when(problemService).findHistoryByProblemId(any(), any());

        mockMvc.perform(get(String.format(GET_HISTORY_BY_PROBLEM_ID_URI, problemId)
                + "?page_number=1&take_size=3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.page_number").exists())
            .andExpect(jsonPath("$.take_size").exists())
            .andExpect(jsonPath("$.total_pages").exists())
            .andDo(document("get-problems/{id}/history",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @Test
    @WithMockAlgoUser
    void findProblemByProblemIdTest() throws Exception {
        // given:
        ProblemDto problemDto = makeProblemDto();
        Mockito.doReturn(problemDto).when(problemService).findProblemByProblemId(problemDto.getProblemId());

        // expect:
        mockMvc.perform(get(String.format(GET_PROBLEM_URI, problemDto.getProblemId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.problem_id").exists())
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.content.title").exists())
            .andExpect(jsonPath("$.content.link").exists())
            .andExpect(jsonPath("$.content.main_text").exists())
            .andExpect(jsonPath("$.code.source_code").exists())
            .andExpect(jsonPath("$.code.lang").exists())
            .andExpect(jsonPath("$.code.spend_time").exists())
            .andExpect(jsonPath("$.code.status").exists())
            .andExpect(jsonPath("$.code.difficulty").exists())
            .andExpect(jsonPath("$.category").exists())
            .andExpect(jsonPath("$.member_id").exists())
            .andExpect(jsonPath("$.created_at").exists())
            .andExpect(jsonPath("$.modified_at").exists())
            .andDo(document("get-problems/{id}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

}