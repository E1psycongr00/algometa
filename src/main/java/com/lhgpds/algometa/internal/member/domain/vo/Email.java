package com.lhgpds.algometa.internal.member.domain.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Email {

    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[a-z]{2,4}$";
    private static final String INVALID_EMAIL_FORMAT_MESSAGE = "이메일 형식에 맞게 입력해주세요.";

    @EqualsAndHashCode.Include
    private String email;


    public Email(String email) {
        validateEmailFormat(email);
        this.email = email;
    }

    public static Email from(String email) {
        return new Email(email);
    }

    private static void validateEmailFormat(String email) {
        Matcher matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(INVALID_EMAIL_FORMAT_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return email;
    }
}
