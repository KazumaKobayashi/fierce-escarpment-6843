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

import com.example.exception.FriendRelationNotFoundException;
import com.example.util.StatusCodeUtil;
import com.jayway.jsonpath.JsonPath;

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

	private String token;
	private String otherToken;

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

		// 他ユーザの生成
		// 他のユーザを登録する
		mockMvc.perform(post("/register")
						.param("id", otherId)
						.param("email", otherEmail)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
		result = mockMvc.perform(post("/login")
						.param("id", otherId)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(0))
			.andReturn();
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
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.user.id").value(id))
			.andExpect(jsonPath("$.user.email").value(email))
			.andExpect(jsonPath("$.user.name").value(id));
	}

	/**
	 * 他ユーザ情報取得テスト（友達じゃない版）
	 * 友達でないので失敗しないとおかしい
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザが情報を取得する() throws Exception {
		mockMvc.perform(get("/users/" + id + "/info")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getStatusCode(FriendRelationNotFoundException.class)));
	}

	/**
	 * 他ユーザ情報取得テスト（友達版）
	 *
	 * @throws Exception
	 */
	@Test
	public void 友達が情報を取得する() throws Exception {
		// フレンドを申請する
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請を許可する
		mockMvc.perform(put("/friends/" + id + "/approve")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		mockMvc.perform(get("/users/" + id + "/info")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.user.id").value(id))
			.andExpect(jsonPath("$.user.email").value(email))
			.andExpect(jsonPath("$.user.name").value(id));
	}

	/**
	 * ユーザ情報更新テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ情報を更新する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/info")
						.param("email", email)
						.param("name", id)
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}

	/**
	 * パスワード変更テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void パスワードを変更する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/password")
						.param("current_password", password)
						.param("new_password", "kazukihasegawa")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}

	/**
	 * ユーザの座標更新テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void 座標を更新する() throws Exception {
		mockMvc.perform(put("/users/" + id + "/coordinate")
						.param("lat", "0.0")
						.param("lng", "0.0")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
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
						.param("lat", "35.626303")
						.param("lng", "139.339350")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
		// 座標更新（八王子みなみ野駅）
		mockMvc.perform(put("/users/" + otherId + "/coordinate")
						.param("lat", "35.631364")
						.param("lng", "139.330975")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// otherIdのユーザとの距離を求める距離取得
		mockMvc.perform(get("/users/" + otherId + "/coordinate/diff")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
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
						.param("email", email)
						.param("name", id)
						.param("token", otherToken))
			.andExpect(status().isNotFound())
			.andExpect(content().contentType("application/json"));
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
						.param("current_password", password)
						.param("new_password", "kazuma")
						.param("token", otherToken))
			.andExpect(status().isNotFound())
			.andExpect(content().contentType("application/json"));
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
						.param("lat", "0.0")
						.param("lng", "0.0")
						.param("token", otherToken))
			.andExpect(status().isNotFound())
			.andExpect(content().contentType("application/json"));
	}

	/**
	 * フレンド取得テスト
	 * 実際はリダイレクトがうまくいくかのテスト
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンドを取得() throws Exception {
		mockMvc.perform(get("/users/" + id + "/friends")
							.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}

	/**
	 * 他のユーザのフレンド取得テスト
	 * 実際はリダイレクトがうまくいくかのテスト
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザのフレンドを取得() throws Exception {
		mockMvc.perform(get("/users/" + id + "/friends")
							.param("token", otherToken))
			.andExpect(status().isNotFound());
	}

	/**
	 * 他のユーザのフレンド申請中一覧取得テスト
	 * 実際はリダイレクトがうまくいくかのテスト
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザのフレンド申請中一覧を取得() throws Exception {
		mockMvc.perform(get("/users/" + id + "/relating")
							.param("token", otherToken))
			.andExpect(status().isNotFound());
	}

	/**
	 * 他のユーザのフレンド申請待ち一覧取得テスト
	 * 実際はリダイレクトがうまくいくかのテスト
	 *
	 * @throws Exception
	 */
	@Test
	public void 他のユーザのフレンド申請待ち一覧を取得() throws Exception {
		mockMvc.perform(get("/users/" + id + "/related")
							.param("token", otherToken))
			.andExpect(status().isNotFound());
	}
}
