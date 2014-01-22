package com.example.service;

import com.example.exception.UserExistsException;
import com.example.jackson.Response;
import com.example.model.User;

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
	 * @return
	 * @throws UserExistsException 
	 */
	public User doRegist(String userId, String password) throws UserExistsException;

	/**
	 * ユーザを取得する
	 *
	 * @param userId ユーザId
	 * @return
	 */
	public User getUser(String userId);

	/**
	 * ユーザ一覧を取得する
	 *
	 * @return
	 */
	public Response getUsers();
}
