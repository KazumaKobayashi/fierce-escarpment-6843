package com.example.service;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.CoordinateExistsException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.util.DateUtil;
import com.example.util.EscapeUtil;
import com.example.util.PasswordUtil;

/**
 * UserServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.UserService
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CoordinateService coordinateService;

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public User create(String userId, String password) throws UserExistsException {
		User user = em.find(User.class, userId);
		if (user != null) {
			throw new UserExistsException("User already exists. Id: " + userId);
		}
		// エスケープ処理
		userId = EscapeUtil.escape(userId);
		userId = EscapeUtil.escape(password);

		user = new User();
		// 現在時刻のタイムスタンプを取得
		Timestamp now = DateUtil.getCurrentTimestamp();

		// ユーザ登録
		user = new User();
		user.setId(userId);
		user.setUsername(userId);
		user.setPassword(PasswordUtil.getPasswordHash(userId, password));
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		em.persist(user);
		try {
			coordinateService.create(userId, 0.0, 0.0);
		} catch (CoordinateExistsException e) {
			// 起こりえるわけがないけど念の為に
			// TODO: ロガーの追加
			e.printStackTrace();
		}
		return user;
	}

	@Transactional
	@Override
	public User update(String userId, String username) throws UserNotFoundException {
		User user = em.find(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("User not found. Id: " + userId);
		}

		if (StringUtils.isNotBlank(username)) {
			user.setUsername(username);
		}
		em.persist(user);
		return user;
	}

	@Transactional
	@Override
	public User getUser(String userId) {
		return em.find(User.class, userId);
	}

	@Override
	public User changePassword(String userId, String currentPassword, String newPassword) throws UserNotFoundException, InvalidPasswordException {
		User user = em.find(User.class, userId);
		// ユーザ存在確認
		if (user == null) {
			throw new UserNotFoundException("User not found. Id: " + userId);
		}
		// パスワードの妥当性チェック
		if (!StringUtils.equals(PasswordUtil.getPasswordHash(userId, currentPassword), user.getPassword())) {
			throw new InvalidPasswordException("Invalid password. Id:" + userId);
		}
		String password = PasswordUtil.getPasswordHash(userId, newPassword);
		user.setPassword(password);
		em.persist(user);
		return user;
	}
}
