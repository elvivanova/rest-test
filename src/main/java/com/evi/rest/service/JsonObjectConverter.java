package com.evi.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonObjectConverter<T> {
    private ObjectMapper mapper = new ObjectMapper();

    public T getObj(String content, Class<T> clazz) {
        T obj;
        try {
            obj = mapper.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            throw new AssertionError("JsonProcessingException: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new AssertionError("IOException: " + e.getMessage(), e);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }

        return obj;
    }

    public String getJson(T obj) {
        String json;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new AssertionError("JsonProcessingException: " + e.getMessage(), e);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }

        return json;
    }
}
