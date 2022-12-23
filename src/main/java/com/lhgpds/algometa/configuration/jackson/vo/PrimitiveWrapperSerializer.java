package com.lhgpds.algometa.configuration.jackson.vo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import java.io.IOException;

public class PrimitiveWrapperSerializer extends StdSerializer<PrimitiveWrapper> {

    protected PrimitiveWrapperSerializer() {
        this(null);
    }

    protected PrimitiveWrapperSerializer(Class<PrimitiveWrapper> t) {
        super(t);
    }

    @Override
    public void serialize(PrimitiveWrapper value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {

        if (StringUtils.isNumeric(value.toString())) {
            gen.writeNumber(value.toString());
            return;
        }
        gen.writeString(value.toString());
    }
}
