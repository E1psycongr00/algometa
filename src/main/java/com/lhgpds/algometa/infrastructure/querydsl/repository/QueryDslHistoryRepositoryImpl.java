package com.lhgpds.algometa.infrastructure.querydsl.repository;

import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.problem.application.dto.HistoryDto;
import com.lhgpds.algometa.internal.problem.domain.entity.QHistory;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslHistoryRepositoryImpl implements QueryDslHistoryRepository {

    private final JPAQueryFactory queryFactory;
    private final QHistory history = QHistory.history;

    @Override
    public List<HistoryDto> findHistoriesByProblemId(PageCondition pageCondition,
        ProblemId problemId) {
        return queryFactory.select(Projections.constructor(HistoryDto.class,
                history.historyId,
                history.problemId,
                history.code,
                history.createdAt,
                history.modifiedAt
            ))
            .from(history)
            .where(history.problemId.eq(problemId))
            .orderBy(history.createdAt.desc())
            .offset(pageCondition.getOffsetSize())
            .limit(pageCondition.getTakeSize())
            .fetch();
    }
}
