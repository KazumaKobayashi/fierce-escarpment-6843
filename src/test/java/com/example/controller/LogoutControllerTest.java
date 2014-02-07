package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.util.StatusCodeUtil;
import com.jayway.jsonpath.JsonPath;

/**
 * LogoutControllerのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.controller.LogoutController
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:testContext.xml")
public class LogoutControllerTest extends AbstractControllerTest {
	private String id = LogoutControllerTest.class.getSimpleName();
	private String email = "example@example.com";
	private String password = "kazumakobayashi";

	private String token;

	@Before
	public void setup() throws Exception {
		super.setup();
		// ユーザ登録
		mockMvc.perform(post("/register")
						.param("id", id)
						.param("email", email)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: Successコードと比較
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.user.id").value(id))
			.andExpect(jsonPath("$.user.name").value(id))
			.andExpect(jsonPath("$.user.email").value(email));
		// ログイントークン発行
		MvcResult result = mockMvc.perform(post("/login")
						.param("id", id)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andReturn();
		token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
	}

	/**
	 * ログアウトテスト
	 *
	 * @throws Exception
	 */
	@Test
	public void ログアウトする() throws Exception {
		mockMvc.perform(delete("/logout")
						.param("id", id)
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}
}
