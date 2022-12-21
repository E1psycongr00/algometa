package com.lhgpds.algometa.internal.problem.domain.converter;

import com.lhgpds.algometa.internal.problem.domain.vo.code.Language;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(Language language) {
        return language == null ? null : language.name();
    }

    @Override
    public Language convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Language.fromLanguage(dbData);
    }
}
