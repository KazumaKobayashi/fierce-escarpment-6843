package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractTest {
	protected static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}
}
