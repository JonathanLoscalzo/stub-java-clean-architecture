package com.atlantis.supermarket.common.parser;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonParser {

    private static ObjectMapper mapper;

    public static <T> T readValue(String str, Class<T> valueType) {
	try {
	    mapper = new ObjectMapper();
	    return (T) mapper.readValue(str, valueType);
	} catch (JsonParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    public static <T> String writeValue(T valueType) {
	mapper = new ObjectMapper();
		
	try {
	    return mapper.writeValueAsString(valueType);
	} catch (JsonGenerationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return null;
    }

    /*
     * public T serialize(T t, Class<T> klass) throws JsonParseException,
     * JsonMappingException, IOException {
     * 
     * ObjectMapper mapper = new ObjectMapper(); String str;
     * 
     * mapper.writeValue(str, (klass) t);
     * 
     * return str;
     * 
     * }
     */
}
