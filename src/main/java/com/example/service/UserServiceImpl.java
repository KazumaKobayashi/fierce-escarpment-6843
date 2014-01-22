package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.UserExistsException;
import com.example.jackson.Response;
import com.example.model.User;
import com.example.util.DateUtil;
import com.example.util.PasswordUtil;

/**
 * UserServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.UserService
 */
@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public User doRegist(String userId, String password) throws UserExistsException {
		User user = getUser(userId);
		if (isValid(userId, password)
				|| user != null) {
			throw new UserExistsException("User already exists. Id: " + userId);
		}
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

		return user;
	}

	@Transactional
	@Override
	public User getUser(String userId) {
		TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);
		query.setParameter("id", userId);
		List<User> users = query.getResultList();
		if (!users.isEmpty()) {
			// 先頭を取り出す
			User user = users.get(0);
			return user;
		} else {
			// ユーザが存在しない場合nullを返す
			return null;
		}
	}

	@Transactional
	@Override
	public Response getUsers() {
		CriteriaQuery<User> c = em.getCriteriaBuilder().createQuery(User.class);
		c.from(User.class);
		Response res = new Response();
		res.addObjects("users", em.createQuery(c).getResultList());
		// TODO: 正しいステータスコード指定のこと
		res.setStatusCode(0);
		return res;
	}

	private boolean isValid(String userId, String password) {
		// TODO: 不正文字列チェック(SQL Injection, etc...)
		return false;
	}
}
