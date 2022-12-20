package com.lhgpds.algometa.internal.problem.application.utils;

import com.lhgpds.algometa.internal.problem.domain.entity.Problem;
import org.springframework.security.access.AccessDeniedException;

public class ProblemAuthorityChecker {

    private static final String NOT_ALLOWED = "해당 유저는 해당 문제에 접근할 권한이 없습니다.";

    public static void check(long memberId, Problem problem) {
        if (memberId != problem.getMemberId()) {
            throw new AccessDeniedException(NOT_ALLOWED);
        }
    }

}
