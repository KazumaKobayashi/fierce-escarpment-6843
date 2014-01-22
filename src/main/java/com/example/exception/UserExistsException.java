package com.example.exception;

/**
 * すでにユーザが存在する場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class UserExistsException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public UserExistsException(String message) {
		super(message);
	}
}
