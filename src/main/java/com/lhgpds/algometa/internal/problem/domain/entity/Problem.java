package com.lhgpds.algometa.internal.problem.domain.entity;

import com.lhgpds.algometa.internal.common.entity.DateBaseEntity;
import com.lhgpds.algometa.internal.problem.domain.vo.HistoryId;
import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Category;
import com.lhgpds.algometa.internal.problem.domain.vo.code.Code;
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Problem extends DateBaseEntity {

    @EmbeddedId
    private ProblemId problemId;

    @Embedded
    private Content content;

    @Embedded
    private Code code;

    private Platform platform;

    @ElementCollection(targetClass = Category.class)
    @Column(name = "category", length = 20)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "algorithm_category", joinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> category = new LinkedHashSet<>();

    private Long memberId;

    protected Problem() {
    }

    @Builder
    public Problem(ProblemId problemId, Content content, Code code, Platform platform,
        Set<Category> category, Long memberId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.problemId = problemId == null ? ProblemId.nextProblemId() : problemId;
        this.content = content;
        this.code = code;
        this.platform = platform;
        this.category = category == null ? new LinkedHashSet<>() : category;
        this.memberId = memberId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void changeContent(Content content) {
        this.content = content;
    }

    public void changeCode(Code code) {
        this.code = code;
    }

    public History snapShotCode() {
        HistoryId historyId = HistoryId.nextProblemId();
        return new History(historyId, problemId, this.code);
    }
}
