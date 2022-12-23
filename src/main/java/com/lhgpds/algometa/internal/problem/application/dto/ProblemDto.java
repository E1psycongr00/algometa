package com.lhgpds.algometa.internal.problem.application.dto;

import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProblemDto {

    private ProblemId problemId;
    private Content content;
    private Code code;
    private Set<Category> category;
    private Platform platform;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
