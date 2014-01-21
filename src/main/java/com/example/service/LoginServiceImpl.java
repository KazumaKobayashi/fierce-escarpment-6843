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

import com.example.exception.InvalidPasswordException;
import com.example.exception.NotFoundUserException;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.util.DateUtil;
import com.example.util.PasswordUtil;

/**
 * LoginServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.LoginService
 */
@Service
public class LoginServiceImpl implements LoginService {

	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public LoginToken doLogin(String userId, String password) throws NotFoundUserException, InvalidPasswordException {
		Query query = em.createQuery("select u from User u where u.id = :id");
		query.setParameter("id", userId);
		List<User> users = query.getResultList();
		if (users.size() > 0) {
			// 先頭を取り出す
			User user = users.get(0);
			if (StringUtils.equals(PasswordUtil.getPasswordHash(userId, password), user.getPassword())) {
				// 現在時刻のタイムスタンプを取得
				Timestamp now = DateUtil.getCurrentTimestamp();

				// ログイントークンの作成
				LoginToken token = new LoginToken();
				token.setUserId(user.getId());
				token.setToken(RandomStringUtils.randomAlphanumeric(10));
				token.setCreatedAt(now);
				token.setUpdatedAt(now);
				em.persist(token);

				return token;
			} else {
				throw new InvalidPasswordException("Invalid password. Id:" + userId);
			}
		} else {
			throw new NotFoundUserException("Not found user. Id: " + userId);
		}
	}
}
