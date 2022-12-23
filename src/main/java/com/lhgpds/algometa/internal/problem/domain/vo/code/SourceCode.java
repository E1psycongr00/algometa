package com.lhgpds.algometa.internal.problem.domain.vo.code;

import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class SourceCode implements PrimitiveWrapper {

    private String code;

    public SourceCode(String code) {
        this.code = code;
    }

    protected SourceCode() {
    }

    @Override
    public String toString() {
        return code;
    }
}
