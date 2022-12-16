package com.lhgpds.algometa.internal.member.domain.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {

    private static final String NICKNAME_FORMAT = "^[a-zA-Z가-힣][a-zA-Z가-힣\\d]{2,9}$";
    private static final String INVALID_NICKNAME_FORMAT_MESSAGE = "닉네임은 첫글자 숫자 없이 영문자, 숫자, 한글, _, -로 3 ~ 12 길이만 입력 가능합니다.";

    @EqualsAndHashCode.Include
    private String nickname;


    public Nickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    public static Nickname from(String nickname) {
        return new Nickname(nickname);
    }

    private static void validateNickname(String nickname) {
        Matcher matcher = Pattern.compile(NICKNAME_FORMAT).matcher(nickname);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(INVALID_NICKNAME_FORMAT_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return nickname;
    }
}
