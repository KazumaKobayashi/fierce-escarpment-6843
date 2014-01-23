package com.example.service;

import com.example.exception.InvalidPasswordException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;

/**
 * ユーザ関係のサービス
 *
 * @author Kazuki Hasegawa
 */
public interface UserService {

	/**
	 * ユーザを作成する
	 *
	 * @param userId
	 * @param password
	 * @return
	 * @throws UserExistsException
	 */
	public User create(String userId, String password) throws UserExistsException;

	/**
	 * ユーザ情報を更新する
	 *
	 * @param userId
	 * @param username
	 * @param password
	 * @return
	 * @throws UserNotFoundException
	 */
	public User update(String userId, String username) throws UserNotFoundException;

	/**
	 * ユーザを取得する
	 *
	 * @param userId ユーザId
	 * @return
	 */
	public User getUser(String userId);

	/**
	 * ユーザのパスワードを変更する
	 *
	 * @param userId
	 * @param currentPassword
	 * @param newPassword
	 * @return
	 * @throws UserNotFoundException
	 */
	public User changePassword(String userId, String currentPassword, String newPassword) throws UserNotFoundException, InvalidPasswordException;

}
