package com.example.service;

import com.example.jackson.Response;

/**
 * ユーザ関係のサービス
 *
 * @author Kazuki Hasegawa
 */
public interface UserService {
	/**
	 * ユーザの登録を行う
	 *
	 * @param userId ユーザId
	 * @param password ユーザパスワード
	 * @return 登録の結果を返す
	 */
	public Response doRegist(String userId, String password);

	/**
	 * ユーザを取得する
	 * 成功した場合は、ユーザの情報をJsonで返却する
	 *
	 * @param userId ユーザId
	 * @return
	 */
	public Response getUser(String userId);

	/**
	 * ユーザ一覧を取得する
	 *
	 * @return
	 */
	public Response getUsers();
}
