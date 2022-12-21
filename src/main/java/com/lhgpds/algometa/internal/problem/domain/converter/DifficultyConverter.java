package com.lhgpds.algometa.internal.problem.domain.converter;

import com.lhgpds.algometa.internal.problem.domain.vo.code.Difficulty;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DifficultyConverter implements AttributeConverter<Difficulty, String> {

    @Override
    public String convertToDatabaseColumn(Difficulty difficulty) {
        return difficulty == null ? null : difficulty.name();
    }

    @Override
    public Difficulty convertToEntityAttribute(String dbData) {
        return dbData == null? null : Difficulty.fromDifficulty(dbData);
    }
}
