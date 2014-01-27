package com.example.exception;

/**
 * 地図情報が存在しない例外
 *
 * @author Kazuki Hasegawa
 */
public class CoordinateNotFoundException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public CoordinateNotFoundException(String message) {
		super(message);
	}
}
