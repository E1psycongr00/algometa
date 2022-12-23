package com.lhgpds.algometa.configuration.jackson.date;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import java.time.LocalDateTime;

public class LocalDateTimeModule extends SimpleModule {

    @Override
    public String getModuleName() {
        return super.getModuleName();
    }

    @Override
    public void setupModule(SetupContext context) {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());

        SimpleDeserializers simpleDeserializers = new SimpleDeserializers();
        simpleDeserializers.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        context.addSerializers(simpleSerializers);
        context.addDeserializers(simpleDeserializers);
    }
}
