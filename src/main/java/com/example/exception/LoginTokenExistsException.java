package com.example.exception;

/**
 * ログイントークンがすでに存在していた場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class LoginTokenExistsException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public LoginTokenExistsException(String message) {
		super(message);
	}
}
