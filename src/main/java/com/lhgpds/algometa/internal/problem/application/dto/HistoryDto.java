package com.lhgpds.algometa.internal.problem.application.dto;

import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class HistoryDto {

    private HistoryId historyId;

    private ProblemId problemId;

    private Code code;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
