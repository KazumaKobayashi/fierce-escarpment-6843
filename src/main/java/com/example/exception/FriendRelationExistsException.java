package com.example.exception;

/**
 * フレンド関係がすでに存在する場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class FriendRelationExistsException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public FriendRelationExistsException(String message) {
		super(message);
	}
}
