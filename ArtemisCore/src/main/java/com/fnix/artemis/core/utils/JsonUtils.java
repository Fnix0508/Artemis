package com.fnix.artemis.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String val, Class<T> valueType) {
        try {
            return objectMapper.readValue(val,  valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String val, TypeReference valueTypeRef) {
        try {
            return objectMapper.readValue(val,  valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
