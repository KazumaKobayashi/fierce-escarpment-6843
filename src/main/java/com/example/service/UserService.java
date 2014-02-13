package com.example.service;

import com.example.exception.EmailExistsException;
import com.example.exception.InvalidEmailException;
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
	 * ユーザを登録する
	 *
	 * @param userId
	 * @param email
	 * @param passwod
	 * @return
	 */
	public Response regist(String userId, String email, String password);

	/**
	 * ユーザを作成する
	 *
	 * @param userId
	 * @param password
	 * @return
	 * @throws UserExistsException
	 * @throws InvalidEmailException 
	 * @throws EmailExistsException 
	 */
	public User create(String userId, String email, String password);

	/**
	 * ユーザ情報を更新する
	 *
	 * @param userId
	 * @param username
	 * @param password
	 * @return
	 */
	public Response update(String userId, String email, String username);

	/**
	 * ユーザを取得する
	 *
	 * @param userId ユーザId
	 * @return
	 */
	public User getUser(String userId);

	/**
	 * Eメールアドレスを使用してユーザを取得する
	 *
	 * @param email
	 * @return
	 */
	public User getUserByEmail(String email); 

	/**
	 * ユーザのパスワードを変更する
	 *
	 * @param userId
	 * @param currentPassword
	 * @param newPassword
	 * @return
	 */
	public Response changePassword(String userId, String currentPassword, String newPassword);

}
