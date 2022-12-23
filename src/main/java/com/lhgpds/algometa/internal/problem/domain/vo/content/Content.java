package com.lhgpds.algometa.internal.problem.domain.vo.content;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Getter
public class Content {

    private Title title;

    private Link link;

    private MainText mainText;

    protected Content() {
    }

    public static Content of(String title, String link, String mainText) {
        return new Content(new Title(title), new Link(link), new MainText(mainText));
    }
}
