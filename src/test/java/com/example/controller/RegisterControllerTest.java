package com.example.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
	private String id = RegisterControllerTest.class.getName();
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
			// TODO: Successコードと比較
			.andExpect(jsonPath("$.code").value(0))
			.andExpect(jsonPath("$.user.id").value(id))
			.andExpect(jsonPath("$.user.name").value(id))
			.andExpect(jsonPath("$.user.email").value(email));
	}
}
