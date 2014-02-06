package com.example.service;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.EmailExistsException;
import com.example.exception.InvalidEmailException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenExistsException;
import com.example.exception.LoginTokenNotFoundException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.LoginToken;

/**
 * LogoutServiceのテスト
 * @author Kazuma Kobayashi
 * @author Kazuki Hasegawa
 * @see com.example.service.LogoutService
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class LogoutServiceTest {
	@Autowired
	private LogoutService service;
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;

	private String id = LogoutServiceTest.class.getName();
	private String email = "example@example.com";
	private String password = "kazumakobayshi";

	@Before
	public void setup() throws UserExistsException, InvalidEmailException, EmailExistsException {
		// ユーザの作成
		userService.create(id, email, password);
	}

	/**
	 * ログイントークン削除テスト
	 *
	 * @throws UserNotFoundException
	 * @throws InvalidPasswordException
	 * @throws LoginTokenExistsException
	 * @throws LoginTokenNotFoundException
	 */
	@Test
	public void ログイントークンを削除する() throws UserNotFoundException, InvalidPasswordException, LoginTokenExistsException, LoginTokenNotFoundException {
		// ログイントークンの発行
		loginService.createToken(id, password);
		// ログイントークンのチェック
		LoginToken token = loginService.getLoginToken(id);
		assertThat(token, is(notNullValue()));
		assertThat(token.getUserId(), is(id));

		// ログイントークンの削除
		service.deleteToken(id);

		// 再取得
		token = loginService.getLoginToken(id);
		assertThat(token, is(nullValue()));
	}

	/**
	 * ログイントークンが存在しない例外テスト
	 *
	 * @throws LoginTokenNotFoundException
	 * @throws InvalidPasswordException 
	 */
	@Test(expected=LoginTokenNotFoundException.class)
	public void 存在しないログイントークンを削除する() throws LoginTokenNotFoundException, InvalidPasswordException {
		service.deleteToken(id);
	}

	/**
	 * パスワードが間違っている例外テスト
	 *
	 * @throws UserNotFoundException
	 * @throws InvalidPasswordException
	 * @throws LoginTokenExistsException
	 * @throws LoginTokenNotFoundException
	 */
	@Test(expected=InvalidPasswordException.class)
	public void 不正なパスワードでログイントークンを削除する() throws UserNotFoundException,InvalidPasswordException, LoginTokenExistsException, LoginTokenNotFoundException {
		// ログイントークンの発行
		loginService.createToken(id, "aaaaaa"/*password*/);
		// ログイントークンのチェック
		LoginToken token = loginService.getLoginToken(id);
		assertThat(token, is(notNullValue()));
		assertThat(token.getUserId(), is(id));

		// ログイントークンの削除
		service.deleteToken(id);
	}
}
