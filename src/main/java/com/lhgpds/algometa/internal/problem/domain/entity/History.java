package com.lhgpds.algometa.internal.problem.domain.entity;

import com.lhgpds.algometa.internal.common.entity.DateBaseEntity;
import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class History extends DateBaseEntity {

    @EmbeddedId
    private HistoryId historyId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "history_problem_id"))
    private ProblemId problemId;

    @Embedded
    private Code code;

    private History() {
    }

    @Builder
    public History(HistoryId historyId, ProblemId problemId, Code code) {
        this.historyId = historyId == null ? HistoryId.nextProblemId() : historyId;
        this.problemId = problemId;
        this.code = code;
    }
}
