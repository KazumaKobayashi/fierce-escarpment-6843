package com.example.exception;

/**
 * グループが見つからなかった場合の例外
 * @author G012C1205
 *
 */
public class GroupNotFoundException extends Exception{
	/**
	 * Default serializable id
	 */
	private static final long serialVersionUID = 1L;
	
	public GroupNotFoundException(String message){
		super(message);
	}
}