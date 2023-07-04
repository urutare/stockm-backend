package com.urutare.stockservice.models.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;

@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            // Handle the exception or log an error
            return null;
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            // Handle the exception or log an error
            return null;
        }
    }
}
