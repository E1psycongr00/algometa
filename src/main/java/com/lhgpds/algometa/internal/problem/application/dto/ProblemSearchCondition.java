package com.lhgpds.algometa.internal.problem.application.dto;

import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Language;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProblemSearchCondition {

    private ProblemId problemId;
    private Language language;
    private Difficulty difficulty;
}
