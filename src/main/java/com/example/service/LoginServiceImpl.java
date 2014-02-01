package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenExistsException;
import com.example.exception.LoginTokenNotFoundException;
import com.example.exception.UserNotFoundException;
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
	@Autowired
	private UserService userService;
	@Autowired
	private LogoutService logoutService;

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public LoginToken createToken(String userId, String password) throws UserNotFoundException, InvalidPasswordException, LoginTokenExistsException {
		// ユーザの存在チェック
		User user = userService.getUser(userId);
		if (user == null) {
			throw new UserNotFoundException("User not found. Id: " + userId);
		}
		// パスワードの妥当性チェック
		if (!StringUtils.equals(PasswordUtil.getPasswordHash(userId, password), user.getPassword())) {
			throw new InvalidPasswordException("Invalid password. Id:" + userId);
		}
		// ログイントークンの存在チェク
		LoginToken token = getLoginToken(userId);
		if (token != null) {
			if (!DateUtil.isTimestampBeforeFewMinutes(token.getUpdatedAt(), 0)) {
				// 存在する場合はエラー
				throw new LoginTokenExistsException("Login token already exists. Token: " + token.getToken());
			} else {
				// 再発行可能時間になっていれば
				try {
					// ログイントークンを削除する
					logoutService.deleteToken(userId, password);
				} catch (LoginTokenNotFoundException e) {
					// 起こりえるはずがないが一応キャッチする
					e.printStackTrace();
				}
			}
		}

		// ログイントークンが重複しなくなるまで生成し直す
		String randomStr = RandomStringUtils.randomAlphanumeric(10);
		while(getLoginTokenByToken(randomStr) != null) {
			randomStr = RandomStringUtils.randomAlphanumeric(10);
		}

		// ログイントークンの作成
		token = new LoginToken();
		token.setUserId(user.getId());
		token.setToken(randomStr);
		Timestamp now = DateUtil.getCurrentTimestamp();

		token.setCreatedAt(now);
		token.setUpdatedAt(now);
		// ユーザ側にも設定する
		user.setToken(token);
		em.persist(token);

		return token;
	}

	@Transactional
	@Override
	public LoginToken getLoginToken(String userId) {
		return em.find(LoginToken.class, userId);
	}

	@Transactional
	@Override
	public LoginToken getLoginTokenByToken(String token) {
		TypedQuery<LoginToken> query = em.createQuery("select lt from LoginToken lt where lt.token = :token", LoginToken.class);
		query.setParameter("token", token);
		List<LoginToken> tokens = query.getResultList();
		if (!tokens.isEmpty()) {
			// 先頭を取り出す
			LoginToken lToken = tokens.get(0);
			return lToken;
		} else {
			return null;
		}
	}
}
