package com.example.service;

import com.example.exception.InvalidPasswordException;
import com.example.exception.NotFoundUserException;
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
	 */
	public LoginToken doLogin(String userId, String password) throws NotFoundUserException, InvalidPasswordException;
}
