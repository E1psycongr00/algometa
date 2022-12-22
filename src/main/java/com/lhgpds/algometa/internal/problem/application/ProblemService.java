package com.lhgpds.algometa.internal.problem.application;

import com.lhgpds.algometa.exception.common.problem.ProblemNotFoundException;
import com.lhgpds.algometa.internal.common.page.PageCondition;
import com.lhgpds.algometa.internal.common.page.Pages;
import com.lhgpds.algometa.internal.member.application.dto.MemberDto;
import com.lhgpds.algometa.internal.problem.application.dto.HistoryDto;
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto;
import com.lhgpds.algometa.internal.problem.application.utils.ProblemAuthorityChecker;
import com.lhgpds.algometa.internal.problem.domain.entity.History;
import com.lhgpds.algometa.internal.problem.domain.entity.Problem;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import com.lhgpds.algometa.internal.problem.repository.HistoryRepository;
import com.lhgpds.algometa.internal.problem.repository.ProblemRepository;
import com.lhgpds.algometa.mapper.ProblemMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    public ProblemDto addProblem(MemberDto userDto, ProblemDto problemDto) {
        Problem problem = ProblemMapper.instance.toEntity(problemDto);
        problem.setMemberId(userDto.getId());
        History history = problem.snapShotCode();
        historyRepository.save(history);
        Problem resultProblem = problemRepository.save(problem);
        return ProblemMapper.instance.toDto(resultProblem);
    }

    @Transactional
    public ProblemDto updateProblemContent(long memberId, ProblemId problemId, Content content) {
        Problem problem = getValidProblemEntity(memberId, problemId);
        problem.changeContent(content);
        return ProblemMapper.instance.toDto(problem);
    }

    @Transactional
    public ProblemDto updateProblemCode(long memberId, ProblemId problemId, Code code) {
        Problem problem = getValidProblemEntity(memberId, problemId);
        problem.changeCode(code);
        History history = problem.snapShotCode();
        historyRepository.save(history);
        return ProblemMapper.instance.toDto(problem);
    }

    @Transactional(readOnly = true)
    public Pages<HistoryDto> findHistoryByProblemId(PageCondition pageCondition,
        ProblemId problemId) {
        List<HistoryDto> histories = historyRepository.findHistoriesByProblemId(
            pageCondition, problemId);
        long totalSize = historyRepository.countByProblemId(problemId);
        return Pages.of(histories, pageCondition, totalSize);
    }

    private Problem getValidProblemEntity(long memberId, ProblemId problemId) {
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(ProblemNotFoundException::new);
        ProblemAuthorityChecker.check(memberId, problem);
        return problem;
    }

}
