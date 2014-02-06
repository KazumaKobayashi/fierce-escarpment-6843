package com.example.controller;

import static org.hamcrest.Matchers.*;
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
 * FriendControllerのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.controller.FriendController
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:testContext.xml")
public class FriendControllerTest extends AbstractControllerTest {
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
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andReturn();
		otherToken = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
	}

	/**
	 * フレンド一覧取得テスト
	 * フレンドを申請しているが承認されていないのでいないはず
	 *
	 * @throws Exception
	 */
	@Test
	public void 自分のフレンドを取得する_0人() throws Exception {
		// フレンドを申請する
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンドを取得する
		mockMvc.perform(get("/friends/" + id + "/")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.friends", hasSize(0)));
	}

	/**
	 * フレンド一覧取得テスト
	 * フレンド申請をしていて承認されているので取得出来るはず
	 *
	 * @throws Exception
	 */
	@Test
	public void 自分のフレンドを取得する_1人() throws Exception {
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

		// フレンドを取得する
		mockMvc.perform(get("/friends/" + id + "/")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.friends", hasSize(1)));
	}

	/**
	 * フレンド一覧取得テスト
	 * 他人のフレンドなので取得出来てはいけない
	 *
	 * @throws Exception
	 */
	@Test
	public void 他人のフレンドを取得する() throws Exception {
		// フレンドを取得する
		mockMvc.perform(get("/friends/" + id + "/")
						.param("token", otherToken))
			.andExpect(status().isNotFound());
	}

	/**
	 * フレンド申請中一覧取得
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請中一覧を取得する_1人() throws Exception {
		// フレンドを申請する
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請中一覧を取得する
		mockMvc.perform(get("/friends/" + id + "/relating")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.relating", hasSize(1)));
	}

	/**
	 * フレンド申請中一覧取得
	 * 承認されているので0人なはず
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請中一覧を取得する_0人() throws Exception {
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

		// フレンド申請中一覧を取得する
		mockMvc.perform(get("/friends/" + id + "/relating")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.relating", hasSize(0)));
	}

	/**
	 * フレンド申請中一覧取得
	 * 他のユーザの申請中一覧は取得出来ないのでNotFoundなはず
	 *
	 * @throws Exception
	 */
	public void 他ユーザのフレンド申請中一覧を取得する() throws Exception {
		// フレンド申請中一覧を取得する
		mockMvc.perform(get("/friends/" + id + "/relating")
						.param("token", otherToken))
			.andExpect(status().isNotFound());
	}

	/**
	 * フレンド申請待ち一覧取得
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請待ち一覧を取得する_1人() throws Exception {
		// フレンドを申請される
		mockMvc.perform(post("/friends/" + id + "/add")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請中一覧を取得する
		mockMvc.perform(get("/friends/" + id + "/related")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.related", hasSize(1)));
	}

	/**
	 * フレンド申請待ち一覧取得
	 * 承認するので0人なはず
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請待ち一覧を取得する_0人() throws Exception {
		// フレンドを申請される
		mockMvc.perform(post("/friends/" + id + "/add")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請を許可する
		mockMvc.perform(put("/friends/" + otherId + "/approve")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請中一覧を取得する
		mockMvc.perform(get("/friends/" + id + "/related")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()))
			.andExpect(jsonPath("$.related", hasSize(0)));
	}

	/**
	 * フレンド申請待ち一覧取得
	 * 他のユーザの申請待ち一覧は取得出来ないのでNotFoundなはず
	 *
	 * @throws Exception
	 */
	public void 他人のフレンド申請待ち一覧を取得する() throws Exception {
		// フレンド申請中一覧を取得する
		mockMvc.perform(get("/friends/" + id + "/related")
						.param("token", otherToken))
			.andExpect(status().isNotFound());
	}

	/**
	 * フレンド申請テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンドを申請する() throws Exception {
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}

	/**
	 * フレンド申請許可テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請を許可する() throws Exception {
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
	}

	/**
	 * フレンド申請自身で許可テスト
	 * 失敗しなければならない
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請を自身許可する() throws Exception {
		// フレンドを申請する
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請を許可する
		mockMvc.perform(put("/friends/" + otherId + "/approve")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getStatusCode(Exception.class)));
	}

	/**
	 * フレンド申請却下テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請を却下する() throws Exception {
		// フレンドを申請する
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請を却下する
		mockMvc.perform(delete("/friends/" + id + "/unapprove")
						.param("token", otherToken))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}

	/**
	 * フレンド申請自身で却下テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンド申請を自身で却下する() throws Exception {
		// フレンドを申請する
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));

		// フレンド申請を却下する
		mockMvc.perform(delete("/friends/" + otherId + "/unapprove")
						.param("token", token))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(StatusCodeUtil.getSuccessStatusCode()));
	}
}
