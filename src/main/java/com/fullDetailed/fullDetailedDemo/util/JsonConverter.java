package com.fullDetailed.fullDetailedDemo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

@Converter
public class JsonConverter<T> implements AttributeConverter<T, String> {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final Class<T> type;

    public JsonConverter(Class<T> type) {
        this.type = type;
    }

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) return null;
        return mapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public T convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return mapper.readValue(dbData, type);
    }
}