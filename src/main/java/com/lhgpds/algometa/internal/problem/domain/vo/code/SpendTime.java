package com.lhgpds.algometa.internal.problem.domain.vo.code;

import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapper;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class SpendTime implements PrimitiveWrapper {

    private static final int MIN_MINUTE_TIME = 0;
    private static final String ERROR_TIME_FORMAT_MESSAGE = "타임 포맷팅 형식에 맞게 입력해주세요.";
    private static final String TIME_FORMAT_INFO = "HH:MM:SS";

    private LocalTime time;

    private String stringFormatTime;

    public SpendTime(String time) {
        try {
            this.stringFormatTime = time;
            this.time = LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ERROR_TIME_FORMAT_MESSAGE + " " + TIME_FORMAT_INFO);
        }
    }

    @Override
    public String toString() {
        return stringFormatTime;
    }
}
