package com.lhgpds.algometa.controller.problem;

import com.lhgpds.algometa.controller.problem.dto.RequestAddProblem;
import com.lhgpds.algometa.controller.problem.dto.ResponseProblemId;
import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.internal.member.service.dto.MemberDto;
import com.lhgpds.algometa.internal.problem.service.ProblemService;
import com.lhgpds.algometa.internal.problem.service.dto.ProblemDto;
import com.lhgpds.algometa.mapper.ProblemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @Secured("ROLE_USER")
    @PostMapping
    public ResponseEntity<ResponseProblemId> addProblem(@AuthenticationPrincipal AlgoUser algoUser,
        @RequestBody @Validated RequestAddProblem requestAddProblem) {

        MemberDto userDto = algoUser.getMemberDto();
        ProblemDto requestDto = ProblemMapper.instance.toDto(requestAddProblem);
        ProblemDto responseDto = problemService.addProblem(userDto, requestDto);
        ResponseProblemId response = ProblemMapper.instance.toResponseProblemId(responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
