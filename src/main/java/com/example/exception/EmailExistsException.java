package com.example.exception;

/**
 * メールアドレスがすでに登録されている場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class EmailExistsException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public EmailExistsException(String message) {
		super(message);
	}
}
