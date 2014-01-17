package com.example.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
	private Map<String, Object> objects;
	private ObjectMapper mapper;

	public Response() {
		objects = new HashMap<String, Object>();
		mapper = new ObjectMapper();
	}

	public void setStatusCode(Integer statusCode) {
		objects.put("code", statusCode);
	}

	public void addObjects(String key, Object object) {
		objects.put(key, object);
	}

	public String convert() {
		try {
			return mapper.writeValueAsString(objects);
		} catch (IOException e) {
			return new String("");
		}
	}
}
