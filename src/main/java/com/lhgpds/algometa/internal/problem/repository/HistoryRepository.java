package com.lhgpds.algometa.internal.problem.repository;

import com.lhgpds.algometa.internal.problem.domain.entity.History;
import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, HistoryId> {

}