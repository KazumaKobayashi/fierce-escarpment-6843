package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Response doRegist(String userId, String password) {
		Response res = new Response();
		if (isValid(userId, password)
				// TODO: 正しいエラーコード設定のこと
				|| getUser(userId).getStatusCode() != -1) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		} else {
			// 現在時刻のタイムスタンプを取得
			Timestamp now = DateUtil.getCurrentTimestamp();

			// ユーザ登録
			User user = new User();
			user.setId(userId);
			user.setUsername(userId);
			user.setPassword(PasswordUtil.getPasswordHash(userId, password));
			user.setCreatedAt(now);
			user.setUpdatedAt(now);
			em.persist(user);
			// TODO: 正しいサクセスコードを設定のこと
			res.setStatusCode(0);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Response getUser(String userId) {
		Query query = em.createQuery("select u from User u where u.id = :id");
		query.setParameter("id", userId);
		List<User> users = query.getResultList();
		Response res = new Response();
		if (users.size() > 0) {
			// 先頭を取り出す
			User user = users.get(0);
			user.setLat(123.123);
			user.setLng(456.456);
			res.addObjects("user", user);
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
		} else {
			// それ以外は知らない
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		}
		return res;
	}

	@Transactional
	@Override
	public Response getUsers() {
		CriteriaQuery<User> c = em.getCriteriaBuilder().createQuery(User.class);
		c.from(User.class);
		Response res = new Response();
		res.addObjects("users", em.createQuery(c).getResultList());
		// TODO: 正しいサクセスコード指定のこと
		res.setStatusCode(0);
		return res;
	}

	private boolean isValid(String userId, String password) {
		// TODO: 不正文字列チェック(SQL Injection, etc...)
		return false;
	}
}
