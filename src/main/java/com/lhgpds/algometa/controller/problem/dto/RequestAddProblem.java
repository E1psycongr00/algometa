package com.lhgpds.algometa.controller.problem.dto;


import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestAddProblem {

    @NotNull
    private Content content;

    @NotNull
    private Code code;

    @NotNull
    private Set<Category> categories;

    @NotNull
    private Platform platform;
}
