package com.lhgpds.algometa.internal.problem.domain.vo.code;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class Code {

    private SourceCode sourceCode;

    private Language lang;

    private SpendTime spendTime;

    private Status status;

    private Difficulty difficulty;

    private Code() {
    }

    public static Code of(String sourceCode, Language lang, String spendTime, Status status,
        Difficulty difficulty) {
        return new Code(new SourceCode(sourceCode), lang, new SpendTime(spendTime), status,
            difficulty);
    }
}
