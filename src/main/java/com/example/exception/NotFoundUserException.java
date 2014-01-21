package com.example.exception;

/**
 *  ユーザが見つからなかった場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class NotFoundUserException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;


	public NotFoundUserException(String message) {
		super(message);
	}
}
