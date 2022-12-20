package com.lhgpds.algometa.internal.problem.domain.vo.content;

import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Title implements PrimitiveWrapper {

    private static final String TITLE_FORMAT = "^.{1,20}$";
    private static final String ERROR_TITLE_FORMAT_MESSAGE = "타이틀은 1 ~ 20까지 문자만 입력 가능합니다.";
    private String title;

    protected Title() {
    }

    public Title(String title) {
        validateTitleFormat(title);
        this.title = title;
    }

    private void validateTitleFormat(String title) {
        Matcher matcher = Pattern.compile(TITLE_FORMAT).matcher(title);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ERROR_TITLE_FORMAT_MESSAGE);
        }
    }

    public static Title from(String title) {
        return new Title(title);
    }

    @Override
    public String toString() {
        return title;
    }
}
