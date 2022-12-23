package com.lhgpds.algometa.infrastructure.querydsl.repository;

import static com.querydsl.core.group.GroupBy.groupBy;

import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.common.page.Pages;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemSearchCondition;
import com.lhgpds.algometa.internal.problem.domain.entity.QProblem;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Language;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslProblemRepositoryImpl implements QueryDslProblemRepository {

    private final JPAQueryFactory queryFactory;
    private final QProblem problem = QProblem.problem;
    private final EnumPath<Category> category = Expressions.enumPath(Category.class, "category");

    @Override
    public Pages<ProblemDto> findProblemsByCondition(PageCondition pageCondition,
        ProblemSearchCondition problemSearchCondition) {
        List<ProblemDto> problemDtos = queryFactory
            .selectFrom(problem)
            .leftJoin(problem.category, category)
            .where(
                eqProblemId(problemSearchCondition.getProblemId()),
                eqLanguage(problemSearchCondition.getLanguage()),
                eqDifficulty(problemSearchCondition.getDifficulty()))
            .orderBy(problem.createdAt.desc())
            .transform(
                groupBy(problem)
                    .list(Projections.constructor(ProblemDto.class,
                        problem.problemId,
                        problem.content,
                        problem.code,
                        GroupBy.set(category),
                        problem.platform,
                        problem.memberId,
                        problem.createdAt,
                        problem.modifiedAt))
            );
        Long count = queryFactory.select(problem.count())
            .where(
                eqProblemId(problemSearchCondition.getProblemId()),
                eqLanguage(problemSearchCondition.getLanguage()),
                eqDifficulty(problemSearchCondition.getDifficulty())
            )
            .fetchOne();
        count = count == null ? 0 : count;
        return Pages.of(problemDtos, pageCondition, count);
    }

    private BooleanExpression eqProblemId(ProblemId problemId) {
        if (problemId == null) {
            return null;
        }
        return problem.problemId.eq(problemId);
    }

    private BooleanExpression eqLanguage(Language language) {
        if (language == null) {
            return null;
        }
        return problem.code.lang.eq(language);
    }

    private BooleanExpression eqDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            return null;
        }
        return problem.code.difficulty.eq(difficulty);
    }
}
