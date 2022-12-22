package com.lhgpds.algometa.controller.problem;

import com.lhgpds.algometa.controller.problem.dto.RequestAddProblem;
import com.lhgpds.algometa.controller.problem.dto.RequestUpdateProblemCode;
import com.lhgpds.algometa.controller.problem.dto.RequestUpdateProblemContent;
import com.lhgpds.algometa.controller.problem.dto.ResponseProblemId;
import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.auth.jwt.principal.AlgoUser;
import com.lhgpds.algometa.internal.common.page.Pages;
import com.lhgpds.algometa.internal.member.application.dto.MemberDto;
import com.lhgpds.algometa.internal.problem.application.ProblemService;
import com.lhgpds.algometa.internal.problem.application.dto.HistoryDto;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.mapper.ProblemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Secured("ROLE_USER")
    @PutMapping("/{id}/contents")
    public ResponseEntity<ResponseProblemId> updateProblemContent(
        @AuthenticationPrincipal AlgoUser algoUser,
        @PathVariable(value = "id") ProblemId problemId,
        @RequestBody @Validated RequestUpdateProblemContent requestUpdateProblemContent) {

        MemberDto memberDto = algoUser.getMemberDto();
        ProblemDto problemDto = problemService.updateProblemContent(memberDto.getId(), problemId,
            requestUpdateProblemContent.getContent());
        ResponseProblemId responseProblemId = ProblemMapper.instance.toResponseProblemId(
            problemDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseProblemId);
    }

    @Secured("ROLE_USER")
    @PutMapping("/{id}/code")
    public ResponseEntity<ResponseProblemId> updateProblemCode(
        @AuthenticationPrincipal AlgoUser algoUser,
        @PathVariable(value = "id") ProblemId problemId,
        @RequestBody @Validated RequestUpdateProblemCode requestUpdateProblemCode) {

        MemberDto memberDto = algoUser.getMemberDto();
        ProblemDto problemDto = problemService.updateProblemCode(memberDto.getId(), problemId,
            requestUpdateProblemCode.getCode());
        ResponseProblemId responseProblemId = ProblemMapper.instance.toResponseProblemId(
            problemDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseProblemId);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Pages<HistoryDto>> findHistoryByProblemId(
        @PathVariable(value = "id") ProblemId problemId,
        @RequestParam(value = "pageNumber") int pageNumber,
        @RequestParam(value = "takeSize") int takeSize) {

        PageCondition pageCondition = PageCondition.of(pageNumber, takeSize);
        Pages<HistoryDto> pages = problemService.findHistoryByProblemId(pageCondition,
            problemId);
        return ResponseEntity.status(HttpStatus.OK).body(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> findProblemDtoById(
        @PathVariable(value = "id") ProblemId problemId) {

        ProblemDto problemDto = problemService.findProblemByProblemId(problemId);
        return ResponseEntity.status(HttpStatus.OK).body(problemDto);
    }

}