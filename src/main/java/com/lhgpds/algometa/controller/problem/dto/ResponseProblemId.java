package com.lhgpds.algometa.controller.problem.dto;

import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseProblemId {

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private ProblemId problemId;
}
