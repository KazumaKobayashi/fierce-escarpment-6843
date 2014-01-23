package com.example.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenExistsException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.LoginToken;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * LoginServiceのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.LoginService
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class LoginServiceTest {
	@Autowired
	private LoginService service;
	@Autowired
	private UserService userService;

	private String id = LoginServiceTest.class.getName();
	private String password = "kazumakobayashi";

	@Before
	public void setup() throws UserExistsException, UserNotFoundException, InvalidPasswordException, LoginTokenExistsException {
		// ユーザの作成
		userService.create(id, password);
		// ログイントークンの作成
		service.createToken(id, password);
	}

	/**
	 * ログイントークン取得テスト(by ID)
	 */
	@Test
	public void ログイントークンをIDから取得する() {
		// トークンの取得
		LoginToken token = service.getLoginToken(id);
		assertThat(token, is(notNullValue()));
		assertThat(token.getUserId(), is(id));
	}

	/**
	 * ログイントークン取得テスト(by Token)
	 */
	@Test
	public void ログイントークンをトークンから取得する() {
		// 普通にIDから取得
		LoginToken token = service.getLoginToken(id);
		assertThat(token, is(notNullValue()));
		assertThat(token.getUserId(), is(id));

		// トークンから再取得
		String t = token.getToken();
		token = service.getLoginTokenByToken(t);
		assertThat(token, is(notNullValue()));
		assertThat(token.getUserId(), is(id));
	}
}