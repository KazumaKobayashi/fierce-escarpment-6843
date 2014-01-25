package com.example.exception;

/**
 * ログイントークンが存在しない例外
 *
 * @author Kazuki Hasegawa
 */
public class LoginTokenNotFoundException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public LoginTokenNotFoundException(String message) {
		super(message);
	}
}
