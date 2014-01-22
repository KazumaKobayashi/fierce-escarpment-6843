package com.example.exception;

/**
 *  ユーザが見つからなかった場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class UserNotFoundException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
