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
	 * ログイントークンを作成する
	 * 実際の処理は、ログイン認証を得たトークンの発行を行う
	 *
	 * @param userId　ユーザId
	 * @param password ユーザパスワード
	 * @return
	 * @throws LoginTokenExistsException 
	 */
	public LoginToken createToken(String userId, String password) throws UserNotFoundException, InvalidPasswordException, LoginTokenExistsException;

	/**
	 * ユーザのログイントークンを取得する
	 * ユーザIdを元に取得
	 *
	 * @param userId　ユーザId
	 * @return
	 */
	public LoginToken getLoginToken(String userId);

	/**
	 * ユーザのログイントークンを取得する
	 * ログイントークンを元に取得
	 *
	 * @param token ログイントークン
	 * @return
	 */
	public LoginToken getLoginTokenByToken(String token);
}
