package com.example.exception;

/**
 * 不正なパスワードが入力された場合の例外
 *
 * @author Kazuki Hasegawa
 *
 */
public class InvalidPasswordException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String message) {
		super(message);
	}
}
