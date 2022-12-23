package com.lhgpds.algometa.internal.problem.domain.vo.content;

import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Link implements PrimitiveWrapper {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;
    private static final String ERROR_LINK_LENGTH = "링크는 최대 255글자만 허용합니다.";
    private String link;

    public Link(String link) {
        validateLink(link);
        this.link = link;
    }

    public static Link from(String input) {
        return new Link(input);
    }

    private static void validateLink(String link) {
        int linkLength = link.length();
        if (linkLength > MAX_LENGTH || linkLength < MIN_LENGTH) {
            throw new IllegalArgumentException(ERROR_LINK_LENGTH);
        }
    }

    @Override
    public String toString() {
        return link;
    }
}
