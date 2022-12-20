package com.lhgpds.algometa.internal.problem.domain.vo.content;

import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class MainText implements PrimitiveWrapper {

    private String text;

    public MainText(String text) {
        this.text = text;
    }

    protected MainText() {
    }

    public static MainText from(String input) {
        return new MainText(input);
    }

    @Override
    public String toString() {
        return text;
    }
}
