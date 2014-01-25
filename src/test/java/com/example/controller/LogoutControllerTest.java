package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
	private String id = LogoutControllerTest.class.getName();
	private String email = "example@example.com";
	private String password = "kazumakobayashi";

	private String token = StringUtils.EMPTY;

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
			.andExpect(jsonPath("$.code").value(0))
			.andExpect(jsonPath("$.user.id").value(id))
			.andExpect(jsonPath("$.user.name").value(id))
			.andExpect(jsonPath("$.user.email").value(email));
		// ログイントークン発行
		MvcResult result = mockMvc.perform(post("/login")
						.param("id", id)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0))
			.andReturn();
		// ログイントークン取得
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
						.param("password", password)
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}
}
