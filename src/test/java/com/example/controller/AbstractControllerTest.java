package com.example.controller;

import java.util.UUID;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.filter.ResponseFilter;
import com.example.filter.TokenFilter;

public class AbstractControllerTest {
	@Autowired
	protected WebApplicationContext wac;
	@Autowired
	private TokenFilter tokenFilter;
	@Autowired
	private ResponseFilter responseFilter;

	protected MockMvc mockMvc;
	protected MockHttpSession mockSession;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders
					.webAppContextSetup(wac)
					.addFilter(tokenFilter, "/users/*")
					.addFilter(tokenFilter, "/groups/*")
					.addFilter(tokenFilter, "/logout")
					.addFilter(responseFilter)
					.build();
		mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
	}
}
