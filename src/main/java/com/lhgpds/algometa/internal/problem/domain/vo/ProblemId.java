package com.lhgpds.algometa.internal.problem.domain.vo;

import com.fasterxml.uuid.Generators;
import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ProblemId implements PrimitiveWrapper, Serializable {

    private static final long serialVersionUID = -194445827081148241L;

    @Column(name = "problem_id", columnDefinition = "BINARY(16)")
    private String id;

    protected ProblemId() {
    }

    public ProblemId(UUID id) {
        this.id = id.toString();
    }

    public static ProblemId nextProblemId() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        return new ProblemId(uuid);
    }

    @Override
    public String toString() {
        return id;
    }
}
