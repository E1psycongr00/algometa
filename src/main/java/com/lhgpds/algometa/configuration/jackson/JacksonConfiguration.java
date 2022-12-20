package com.lhgpds.algometa.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.lhgpds.algometa.configuration.jackson.date.LocalDateTimeModule;
import com.lhgpds.algometa.configuration.jackson.vo.PrimitiveWrapperModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfiguration {

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper()
            .registerModule(new PrimitiveWrapperModule())
            .registerModule(new LocalDateTimeModule())
            .setPropertyNamingStrategy(new SnakeCaseStrategy());
    }
}
