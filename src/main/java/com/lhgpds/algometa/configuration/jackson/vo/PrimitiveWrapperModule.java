package com.lhgpds.algometa.configuration.jackson.vo;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

public class PrimitiveWrapperModule extends SimpleModule {

    @Override
    public String getModuleName() {
        return super.getModuleName();
    }

    @Override
    public void setupModule(SetupContext context) {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(PrimitiveWrapper.class, new PrimitiveWrapperSerializer());
        context.addSerializers(simpleSerializers);
    }
}
