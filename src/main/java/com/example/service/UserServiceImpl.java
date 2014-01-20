package com.example.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jackson.Response;
import com.example.model.User;
import com.example.util.PasswordUtil;

@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public Response regist(String username, String password) {
		Response res = new Response();
		if (isValid(username, password)
				// TODO: 正しいエラーコード設定のこと
				|| getUser(username).getStatusCode() != -1) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		} else {
			// 登録
			User user = new User();
			user.setUsername(username);
			user.setPassword(PasswordUtil.getPasswordHash(username, password));
			em.persist(user);
			// TODO: 正しいサクセスコードを設定のこと
			res.setStatusCode(0);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Response getUser(String username) {
		Query query = em.createQuery("select u from User u where u.username = :username");
		query.setParameter("username", username);
		List<User> users = query.getResultList();
		Response res = new Response();
		if (users.size() > 0) {
			// 先頭を取り出す
			res.setStatusCode(0);
			com.example.jackson.User userJson = new com.example.jackson.User();
			userJson.setLat(123.123);
			userJson.setLng(456.456);
			userJson.setName(users.get(0).getUsername());
			res.addObjects("user", userJson);
		} else {
			// それ以外は知らない
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
		List<com.example.jackson.User> users = new ArrayList<com.example.jackson.User>();
		for (User user : em.createQuery(c).getResultList()) {
			com.example.jackson.User userJson = new com.example.jackson.User();
			userJson.setLat(123.123);
			userJson.setLng(456.456);
			userJson.setName(user.getUsername());
			users.add(userJson);
		}
		res.addObjects("users", users);
		// TODO: 正しいサクセスコード指定のこと
		res.setStatusCode(0);
		return res;
	}

	private boolean isValid(String username, String password) {
		// TODO: 不正文字列チェック(SQL Injection, etc...)
		return false;
	}
}
