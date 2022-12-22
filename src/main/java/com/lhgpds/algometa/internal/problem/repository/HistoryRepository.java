package com.lhgpds.algometa.internal.problem.repository;

import com.lhgpds.algometa.infrastructure.querydsl.repository.QueryDslHistoryRepository;
import com.lhgpds.algometa.internal.problem.domain.entity.History;
import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, HistoryId>,
    QueryDslHistoryRepository {

    long countByProblemId(ProblemId problemId);


}