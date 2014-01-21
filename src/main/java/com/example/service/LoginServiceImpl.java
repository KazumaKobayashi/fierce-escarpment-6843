package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jackson.Response;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.util.DateUtil;
import com.example.util.PasswordUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Response doLogin(String username, String password) {
		Query query = em.createQuery("select u from User u where u.username = :username");
		query.setParameter("username", username);
		List<User> users = query.getResultList();
		Response res = new Response();
		if (users.size() > 0) {
			// 先頭を取り出す
			User user = users.get(0);
			if (StringUtils.equals(PasswordUtil.getPasswordHash(username, password), user.getPassword())) {
				// 現在時刻のタイムスタンプを取得
				Timestamp now = DateUtil.getCurrentTimestamp();
				LoginToken token = new LoginToken();
				token.setUserId(user.getId());
				token.setToken(RandomStringUtils.randomAlphanumeric(10));
				token.setCreatedAt(now);
				token.setUpdatedAt(now);

				// トークンの保存
				em.persist(token);

				res.addObjects("token", token.getToken());
				res.setStatusCode(0);
			} else {
				res.setStatusCode(-1);
			}
		} else {
			// それ以外は知らない
			res.setStatusCode(-1);
		}
		return res;
	}
}
