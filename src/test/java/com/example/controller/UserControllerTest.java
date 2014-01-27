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
 * @see com.example.controller.UserController
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:testContext.xml")
public class UserControllerTest extends AbstractControllerTest {
	private String id = UserControllerTest.class.getName();
	private String email = "example@example.com";
	private String password = "kazumakobayashi";
	private String otherId = "kazuma";
	private String otherEmail = "kazuma@kazuma.com";

	private String token = StringUtils.EMPTY;
	private String otherToken = StringUtils.EMPTY;

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

		// 他のユーザを登録する
		mockMvc.perform(post("/register")
						.param("id", otherId)
						.param("email", otherEmail)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
		result = mockMvc.perform(post("/login")
						.param("id", otherId)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(0))
			.andReturn();

		// トークン取得する
		otherToken = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
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
						.param("email", email)
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

	/**
	 * 距離算出テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void 距離を算出する() throws Exception {
		// 座標更新（東京工科大学）
		mockMvc.perform(put("/users/" + id + "/coordinate")
						.param("token", token)
						.param("lat", "35.626303")
						.param("lng", "139.339350"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
		// 座標更新（八王子みなみ野駅）
		mockMvc.perform(put("/users/" + otherId + "/coordinate")
						.param("token", otherToken)
						.param("lat", "35.631364")
						.param("lng", "139.330975"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));

		// otherIdのユーザとの距離を求める距離取得
		mockMvc.perform(get("/users/" + otherId + "/coordinate/diff")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0))
			.andExpect(jsonPath("$.meter").value(942))
			.andExpect(jsonPath("$.kilometer").value(0.942));
	}

	/**
	 * ユーザ情報更新失敗テスト
	 * 他のユーザが情報を更新出来るのはおかしい
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザが情報を更新する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/info")
						.param("token", otherToken)
						.param("email", email)
						.param("name", id))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定すること
			.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * パスワード変更失敗テスト
	 * 他のユーザがパスワードを変更出来るのはおかしい
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザがパスワードを変更する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/password")
						.param("token", otherToken)
						.param("current_password", password)
						.param("new_password", "kazuma"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定すること
			.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 座標情報更新失敗テスト
	 * 他のユーザが変更出来るのはおかしい
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザが座標情報を更新する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/coordinate")
						.param("token", otherToken)
						.param("lat", "0.0")
						.param("lng", "0.0"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定すること
			.andExpect(jsonPath("$.code").value(-1));
	}
}
