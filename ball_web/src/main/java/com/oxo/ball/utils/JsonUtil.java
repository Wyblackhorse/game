package com.oxo.ball.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flooming
 */
public class JsonUtil {
    public static String toJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public static <T> T fromJson(String str, Class<T> cls) throws IOException {
        return new ObjectMapper().readValue(str, cls);
    }

    public static <T> List<T> fromJsonToList(String str, Class<T> cls) throws IOException {
        return new ObjectMapper().readValue(str,createType(cls));
    }

    public static JavaType createType(Class cls){
        return new ObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, cls);
    }
}
