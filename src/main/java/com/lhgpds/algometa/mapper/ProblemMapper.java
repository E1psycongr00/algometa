package com.lhgpds.algometa.mapper;

import com.lhgpds.algometa.controller.problem.dto.RequestAddProblem;
import com.lhgpds.algometa.controller.problem.dto.ResponseProblemId;
import com.lhgpds.algometa.internal.problem.domain.entity.Problem;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProblemMapper {

    ProblemMapper instance = Mappers.getMapper(ProblemMapper.class);


    Problem toEntity(ProblemDto problemDto);

    ProblemDto toDto(Problem problem);

    ProblemDto toDto(RequestAddProblem requestAddProblem);

    ResponseProblemId toResponseProblemId(ProblemDto problemDto);
}
