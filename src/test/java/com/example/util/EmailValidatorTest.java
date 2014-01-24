package com.example.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * メールアドレスのチェックテスト
 *
 * @author Kazuki Hasegawa
 */
@RunWith(JUnit4.class)
public class EmailValidatorTest {
	/**
	 * 不正でないメールアドレスのチェック
	 */
	@Test
	public void 不正でないメールアドレスのチェック() {
		assertThat(EmailValidator.validate("example@exampl.com"), is(false));
	}

	/**
	 * 不正なメールアドレスのチェック
	 */
	@Test
	public void 不正なメールアドレスのチェック() {
		// null
		assertThat(EmailValidator.validate(null), is(true));
		// 空文字
		assertThat(EmailValidator.validate(""), is(true));
		// 記号あり
		assertThat(EmailValidator.validate("$$$$@example.com"), is(true));
		// 日本語あり
		assertThat(EmailValidator.validate("はげ@example.com"), is(true));
	}
}
