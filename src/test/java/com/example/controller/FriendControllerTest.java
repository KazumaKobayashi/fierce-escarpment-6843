package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

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

	private MockHttpSession otherSession;

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
		mockMvc.perform(post("/login")
						.param("id", id)
						.param("password", password)
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0))
			.andReturn();

		// 他のユーザを登録する
		mockMvc.perform(post("/register")
						.param("id", otherId)
						.param("email", otherEmail)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
		// 他ユーザのセッションを作成
		otherSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
		mockMvc.perform(post("/login")
						.param("id", otherId)
						.param("password", password)
						.session(otherSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code").value(0))
			.andReturn();
	}

	/**
	 * フレンド申請テスト
	 *
	 * @throws Exception
	 */
	@Test
	public void フレンドを申請する() throws Exception {
		mockMvc.perform(post("/friends/" + otherId + "/add")
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
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
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));

		// フレンド申請を許可する
		mockMvc.perform(put("/friends/" + id + "/approve")
						.session(otherSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
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
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));

		// フレンド申請を許可する
		mockMvc.perform(put("/friends/" + otherId + "/approve")
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(-1));
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
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));

		// フレンド申請を却下する
		mockMvc.perform(delete("/friends/" + id + "/unapprove")
						.session(otherSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
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
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));

		// フレンド申請を却下する
		mockMvc.perform(delete("/friends/" + otherId + "/unapprove")
						.session(mockSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}
}
