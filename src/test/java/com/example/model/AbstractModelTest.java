package com.example.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractModelTest {
	protected static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}
}
