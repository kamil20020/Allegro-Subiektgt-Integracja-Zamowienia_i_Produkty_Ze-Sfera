package pl.kamil_dywan.external.allegro.own.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.OffsetDateTime;

public class JavaTimeObjectMapper extends ObjectMapper {

    public JavaTimeObjectMapper(){

        super();

        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());

        registerModule(new JavaTimeModule());
        registerModule(simpleModule);
    }
}
