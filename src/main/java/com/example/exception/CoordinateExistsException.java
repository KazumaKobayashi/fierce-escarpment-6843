package com.example.exception;

/**
 * 地図情報がすでに存在する場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class CoordinateExistsException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public CoordinateExistsException(String message) {
		super(message);
	}
}
