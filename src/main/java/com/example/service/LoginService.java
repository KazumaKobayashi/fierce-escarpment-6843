package com.example.service;

import com.example.jackson.Response;

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
	public Response doLogin(String userId, String password);
}
