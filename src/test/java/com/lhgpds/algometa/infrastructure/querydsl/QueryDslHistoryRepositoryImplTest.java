package com.lhgpds.algometa.infrastructure.querydsl;

import com.lhgpds.algometa.configuration.JpaAuditingConfiguration;
import com.lhgpds.algometa.configuration.QueryDslConfiguration;
import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.problem.application.dto.HistoryDto;
import com.lhgpds.algometa.internal.problem.domain.entity.History;
import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Status;
import com.lhgpds.algometa.internal.problem.repository.HistoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({QueryDslConfiguration.class, JpaAuditingConfiguration.class})
class QueryDslHistoryRepositoryImplTest {

    @Autowired
    private HistoryRepository historyRepository;

    @Test
    void test() {
        ProblemId problemId = ProblemId.nextProblemId();
        Code code = Code.of("a", null, "00:12:24", Status.FAIL, Difficulty.EASY);
        List<History> histories = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            histories.add(
                new History(HistoryId.nextProblemId(), problemId, code));
        }
        historyRepository.saveAll(histories);
        PageCondition pageCondition = new PageCondition(2, 3);

        List<HistoryDto> list = historyRepository.findHistoriesByProblemId(pageCondition,
            problemId);

        list.forEach(
            System.out::println
        );
    }
}