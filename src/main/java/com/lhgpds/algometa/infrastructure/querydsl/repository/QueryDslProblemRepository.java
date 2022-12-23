package com.lhgpds.algometa.infrastructure.querydsl.repository;

import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.common.page.Pages;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemSearchCondition;

public interface QueryDslProblemRepository {

    Pages<ProblemDto> findProblemsByCondition(PageCondition pageCondition,
        ProblemSearchCondition problemSearchCondition);
}
