package com.lhgpds.algometa.internal.member.domain.vo;


import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageLink {

    private static final int MAX_LENGTH = 255;
    private static final int MIN_LENGTH = 1;
    private static final String ERROR_MAX_LINK_LENGTH_MESSAGE = "링크 길이는 최소 1글자에서 최대 255자를 넘을 수 없습니다.";

    @EqualsAndHashCode.Include
    private String link;

    public ImageLink(String link) {
        validateLinkFormat(link);
        this.link = link;
    }

    public static ImageLink from(String imageLink) {
        return new ImageLink(imageLink);
    }

    private static void validateLinkFormat(String link) {
        if (link.length() > MAX_LENGTH || link.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(ERROR_MAX_LINK_LENGTH_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return link;
    }
}
