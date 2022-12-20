package com.lhgpds.algometa.internal.problem.repository;

import com.lhgpds.algometa.internal.problem.domain.entity.Problem;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, ProblemId> {

}