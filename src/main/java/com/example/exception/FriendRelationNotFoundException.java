package com.example.exception;

/**
 * フレンド関係が存在しない場合の例外
 *
 * @author Kazuki Hasegawa
 */
public class FriendRelationNotFoundException extends Exception {
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;

	public FriendRelationNotFoundException(String message) {
		super(message);
	}
}
