package com.example.controller;

import com.jayway.jsonpath.JsonPath;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * UserControllerのテスト
 *
 * @author Kazuki Hasegawa
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:testContext.xml")
public class UserControllerTest extends AbstractControllerTest {
	private String id = UserControllerTest.class.getName();
	private String password = "kazumakobayashi";

	private String token = StringUtils.EMPTY;

	@Before
	public void setup() throws Exception {
		super.setup();
		// ユーザ登録
		mockMvc.perform(post("/register")
						.param("id", id)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
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
	 * ユーザ情報取得テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ情報を取得する() throws Exception {
		mockMvc.perform(get("/users/" + id + "/info")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}

	/**
	 * ユーザ情報更新テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ情報を更新する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/info")
						.param("token", token)
						.param("name", id))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}

	/**
	 * パスワード変更テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void パスワードを変更する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/password")
						.param("token", token)
						.param("current_password", password)
						.param("new_password", "kazukihasegawa"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}

	/**
	 * ユーザの座標更新テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void 座標を更新する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/coordinate")
						.param("token", token)
						.param("lat", "0.0")
						.param("lng", "0.0"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}
}
