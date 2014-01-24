package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * メールアドレスの不正チェッククラス
 *
 * @author Kazuki Hasegawa
 */
public final class EmailValidator {
	private EmailValidator() {}

	// Eメールのパターン
	private static Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	/**
	 * 不正チェック
	 * 不正であれば true、不正でなければ falseを返す
	 *
	 * @param email
	 * @return
	 */
	public static boolean validate(final String email) {
		if (email == null) {
			return true;
		}
		Matcher matcher = pattern.matcher(email);
		return !matcher.matches();
	}
}
