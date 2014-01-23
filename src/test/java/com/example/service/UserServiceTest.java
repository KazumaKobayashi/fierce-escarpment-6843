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
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.util.PasswordUtil;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * UserServiceのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.UserService
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class UserServiceTest {
	@Autowired
	private UserService service;

	private String id = UserServiceTest.class.getName();
	private String password = "kazumekobayashi";

	@Before
	public void setup() throws UserExistsException {
		// ユーザの作成
		service.create(id, password);
	}

	/**
	 * ユーザ情報取得テスト
	 * ※ ユーザ名はデフォルトだとユーザIDと同じ
	 */
	@Test
	public void ユーザ情報を取得する() {
		User user = service.getUser(id);
		assertThat(user, is(notNullValue()));
		assertThat(user.getId(), is(id));
		assertThat(user.getUsername(), is(id));
		assertThat(user.getPassword(), is(PasswordUtil.getPasswordHash(id, password)));
		assertThat(user.getToken(), is(nullValue()));
	}

	/**
	 * ユーザ情報更新テスト
	 * ※ ユーザ名はデフォルトだとユーザIDと同じ
	 *
	 * @throws UserNotFoundException
	 */
	@Test
	public void ユーザ情報を更新する() throws UserNotFoundException {
		User user = service.getUser(id);
		assertThat(user.getUsername(), is(id));

		// 名前を変更する
		String username = "Kazuma";
		service.update(id, username);

		// 再取得
		user = service.getUser(id);
		assertThat(user.getUsername(), is(username));
	}

	/**
	 * パスワード変更テスト
	 *
	 * @throws UserNotFoundException
	 * @throws InvalidPasswordException
	 */
	@Test
	public void ユーザのパスワードを変更する() throws UserNotFoundException, InvalidPasswordException {
		User user = service.getUser(id);
		assertThat(user.getPassword(), is(PasswordUtil.getPasswordHash(id, password)));

		// パスワードを変更する
		String newPassword = "kazukihasegawa";
		service.changePassword(id, password, newPassword);

		// 再取得
		user = service.getUser(id);
		assertThat(user.getPassword(), is(PasswordUtil.getPasswordHash(id, newPassword)));
	}
}
