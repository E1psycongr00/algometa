package com.lhgpds.algometa.controller.problem.dto;

import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestUpdateProblemCode {

    @NotNull
    private Code code;
}
