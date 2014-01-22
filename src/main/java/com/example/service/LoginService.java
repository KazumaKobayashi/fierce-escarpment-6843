package com.example.service;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.LoginToken;

/**
 * ログイン関係のサービス
 *
 * @author Kazuki Hasegawa
 */
public interface LoginService {

	/**
	 * ログインを行う
	 * 実際の処理は、ログイン認証を得たトークンの発行を行う
	 *
	 * @param userId　ユーザId
	 * @param password ユーザパスワード
	 * @return
	 * @throws LoginTokenExistsException 
	 */
	public LoginToken doLogin(String userId, String password) throws UserNotFoundException, InvalidPasswordException, LoginTokenExistsException;

	/**
	 * ユーザのログイントークンを取得する
	 *
	 * @param userId
	 * @return
	 */
	public LoginToken getLoginToken(String userId);
}
