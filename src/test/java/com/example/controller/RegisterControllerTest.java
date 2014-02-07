package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.User;
import com.example.util.StatusCodeUtil;

/**
 * RegisterControllerのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.controller.RegisterController
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:testContext.xml")
public class RegisterControllerTest extends AbstractControllerTest {
	private String id = RegisterControllerTest.class.getSimpleName();
	private String email = "example@example.com";
	private String password = "kazumakobayashi";

	@Test
	public void  登録テスト() throws Exception {
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
	}

	/**
	 * 不正なユーザIdで登録する
	 *
	 * @throws Exception 
	 */
	@Test
	public void ユーザを登録をする_不正なユーザId() throws Exception {
		mockMvc.perform(post("/register")
					.param("id", id + "----0123")
					.param("email", email)
					.param("password", password))
					.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定のこと
			.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 短いユーザIdで登録する
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_文字列長が短すぎるユーザId() throws Exception {
		mockMvc.perform(post("/register")
				.param("id", RandomStringUtils.randomAlphanumeric(5))
				.param("email", email)
				.param("password", password))
				.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		// TODO: 正しいエラーコードを設定のこと
		.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 長いユーザIdで登録する
	 *
	 * @throws Exception 
	 */
	@Test
	public void ユーザ登録をする_文字列長が長すぎるユーザId() throws Exception {
		mockMvc.perform(post("/register")
				.param("id", RandomStringUtils.randomAlphanumeric(User.USER_ID_MAX_LENGTH + 1))
				.param("email", email)
				.param("password", password))
				.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		// TODO: 正しいエラーコードを設定のこと
		.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * すでに登録されているユーザIdでさらに登録してみる
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_すでに登録されているユーザId() throws Exception {
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

		// もう一回登録
		mockMvc.perform(post("/register")
				.param("id", id)
				.param("email", email)
				.param("password", password))
				.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		// TODO: 正しいエラーコードを設定のこと
		.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 不正なメールアドレスで登録する
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_不正なメールアドレス() throws Exception {
		// もう一回登録
		mockMvc.perform(post("/register")
						.param("id", id)
						.param("email", "あいう" + email)
						.param("password", password))
						.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定のこと
			.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 長すぎるメールアドレスで登録する
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_文字列長が長すぎるメールアドレス() throws Exception {
		// もう一回登録
		mockMvc.perform(post("/register")
						.param("id", id)
						.param("email", RandomStringUtils.randomAlphanumeric(User.EMAIL_MAX_LENGTH) + email)
						.param("password", password))
						.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		// TODO: 正しいエラーコードを設定のこと
		.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * すでに登録されているメールアドレスで登録する
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_すでに使われているメールアドレス() throws Exception {
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

		// もう一回登録
		mockMvc.perform(post("/register")
						.param("id", id + "kazuma")
						.param("email", email)
						.param("password", password))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定のこと
			.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 不正な記号入りのパスワードで登録する
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_不正なパスワード() throws Exception {
		// もう一回登録
		mockMvc.perform(post("/register")
						.param("id", id)
						.param("email", email)
						.param("password", "@"+ password))
						.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定のこと
			.andExpect(jsonPath("$.code").value(-1));
	}

	/**
	 * 長すぎるパスワードで登録する
	 *
	 * @throws Exception
	 */
	@Test
	public void ユーザ登録をする_文字列長が長すぎるパスワード() throws Exception {
		// もう一回登録
		mockMvc.perform(post("/register")
						.param("id", id)
						.param("email", email)
						.param("password", RandomStringUtils.randomAlphabetic(128) + password))
						.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			// TODO: 正しいエラーコードを設定のこと
			.andExpect(jsonPath("$.code").value(-1));
	}
}
