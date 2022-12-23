package com.lhgpds.algometa.internal.problem.domain.converter;

import com.lhgpds.algometa.internal.problem.domain.vo.Platform;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PlatformConverter implements AttributeConverter<Platform, String> {

    @Override
    public String convertToDatabaseColumn(Platform platform) {
        return platform == null ? null : platform.name();
    }

    @Override
    public Platform convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Platform.fromPlatform(dbData);
    }
}
