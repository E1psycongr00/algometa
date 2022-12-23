package com.lhgpds.algometa.infrastructure.querydsl.repository;

import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.problem.application.dto.HistoryDto;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import java.util.List;

public interface QueryDslHistoryRepository {

    List<HistoryDto> findHistoriesByProblemId(PageCondition pageCondition, ProblemId problemId);
}
