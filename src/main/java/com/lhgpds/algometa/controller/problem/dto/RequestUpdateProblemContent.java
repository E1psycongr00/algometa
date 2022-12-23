package com.lhgpds.algometa.controller.problem.dto;

import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestUpdateProblemContent {

    @NotNull
    Content content;
}
