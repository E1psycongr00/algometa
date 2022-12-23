package com.lhgpds.algometa.internal.problem.domain.converter;

import com.lhgpds.algometa.internal.problem.domain.vo.code.SpendTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SpendTimeConverter implements AttributeConverter<SpendTime, String> {


    @Override
    public String convertToDatabaseColumn(SpendTime spendTime) {
        return spendTime == null ? null : spendTime.getStringFormatTime();
    }

    @Override
    public SpendTime convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new SpendTime(dbData);
    }
}
