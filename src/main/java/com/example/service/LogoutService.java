package com.example.service;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenNotFoundException;

/**
 * ログアウト関係のサービス
 *
 * @author Kazuki Hasegawa
 */
public interface LogoutService {
	/**
	 * ログアウトをする
	 * 実際の挙動は、LoginTokenを削除する
	 *
	 * @param id
	 * @param password
	 * @throws InvalidPasswordException 
	 */
	public void deleteToken(String id, String password) throws LoginTokenNotFoundException, InvalidPasswordException;
}
