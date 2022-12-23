package com.lhgpds.algometa.infrastructure.querydsl.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.lhgpds.algometa.configuration.JpaAuditingConfiguration;
import com.lhgpds.algometa.configuration.QueryDslConfiguration;
import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.common.page.Pages;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemSearchCondition;
import com.lhgpds.algometa.internal.problem.domain.entity.Problem;
import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Status;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import com.lhgpds.algometa.internal.problem.repository.ProblemRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({QueryDslConfiguration.class, JpaAuditingConfiguration.class})
class QueryDslProblemRepositoryImplTest {

    @Autowired
    ProblemRepository problemRepository;

    @Test
    void queryTest() {
        Code code = Code.of("a", null, "00:12:24", Status.FAIL, Difficulty.EASY);
        Content content = Content.of("title", "link", "mainText");
        for (int i = 0; i < 10; ++i) {
            problemRepository.save(
                Problem.builder()
                    .problemId(ProblemId.nextProblemId())
                    .content(content)
                    .code(code)
                    .platform(Platform.BACKJOON)
                    .category(Set.of(Category.QUEUE))
                    .memberId(1L)
                    .build());
        }
        PageCondition pageCondition = new PageCondition(2, 3);
        Pages<ProblemDto> list = problemRepository.findProblemsByCondition(pageCondition,
            new ProblemSearchCondition(null, null, null));
        assertThat(list.getTotalPages()).isGreaterThan(0);
    }

}