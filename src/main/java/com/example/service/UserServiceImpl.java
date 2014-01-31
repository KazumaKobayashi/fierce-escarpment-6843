package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.CoordinateExistsException;
import com.example.exception.EmailExistsException;
import com.example.exception.InvalidEmailException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.util.EmailValidator;
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
	public User create(String userId, String email, String password) throws UserExistsException, InvalidEmailException, EmailExistsException {
		User user = em.find(User.class, userId);
		if (user != null) {
			throw new UserExistsException("User already exists. Id: " + userId);
		}
		if (EmailValidator.validate(email)) {
			throw new InvalidEmailException("Invalid email.");
		}
		if (getUserByEmail(email) != null) {
			throw new EmailExistsException("Email already exists. Email: " + email);
		}
		// エスケープ処理
		userId = EscapeUtil.escapeSQL(userId);
		password = EscapeUtil.escapeSQL(password);

		// 現在時刻のタイムスタンプを取得
		Timestamp now = DateUtil.getCurrentTimestamp();

		// ユーザ登録
		user = new User();
		user.setId(userId);
		user.setEmail(email);
		user.setUsername(userId);
		user.setPassword(PasswordUtil.getPasswordHash(userId, password));
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		em.persist(user);
		try {
			coordinateService.create(userId, null, null);
		} catch (CoordinateExistsException e) {
			// 起こりえるわけがないけど念の為に
			// TODO: ロガーの追加
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// 起こりえるわけがないけど念の為に
			// TODO: ロガーの追加
			e.printStackTrace();
		}
		return user;
	}

	@Transactional
	@Override
	public User update(String userId, String email, String username) throws UserNotFoundException {
		User user = em.find(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("User not found. Id: " + userId);
		}

		if (!EmailValidator.validate(email) && getUserByEmail(email) == null) {
			// 不正でない　かつ　登録されているものでなければ
			user.setEmail(email);
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

	@Transactional
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

	@Transactional
	@Override
	public User getUserByEmail(String email) {
		TypedQuery<User> query = em.createQuery("select u from User u where u.email = :email", User.class);
		query.setParameter("email", email);
		List<User> users = query.getResultList();
		if (!users.isEmpty()) {
			// 先頭を取り出す
			User user = users.get(0);
			return user;
		} else {
			return null;
		}
	}
}
