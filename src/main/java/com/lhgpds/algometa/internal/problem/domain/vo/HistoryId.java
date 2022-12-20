package com.lhgpds.algometa.internal.problem.domain.vo;

import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class HistoryId implements PrimitiveWrapper, Serializable {

    private static final long serialVersionUID = -194445827081148241L;

    @Column(name = "history_id")
    private String id;

    protected HistoryId() {
    }

    public HistoryId(String id) {
        this.id = id;
    }

    public static HistoryId nextProblemId() {
        String uuid = UUID.randomUUID().toString();
        return new HistoryId(uuid);
    }

    @Override
    public String toString() {
        return id;
    }
}
