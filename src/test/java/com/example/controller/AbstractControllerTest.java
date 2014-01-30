package com.example.controller;

import java.util.UUID;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AbstractControllerTest {
	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;
	protected MockHttpSession mockSession;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
	}
}
