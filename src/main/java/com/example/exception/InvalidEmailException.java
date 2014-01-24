package com.example.exception;

/**
 * 不正なEmailの例外
 *
 * @author Kazuki Hasegawa
 */
public class InvalidEmailException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public InvalidEmailException(String message) {
		super(message);
	}
}
