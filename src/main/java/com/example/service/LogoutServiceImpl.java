package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenNotFoundException;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.util.PasswordUtil;

/**
 * LogoutServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.LogoutService
 */
@Service
public class LogoutServiceImpl implements LogoutService {

	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;

	@PersistenceContext
	EntityManager em;

	@Transactional
	@Override
	public void deleteToken(String id, String password) throws LoginTokenNotFoundException, InvalidPasswordException {
		LoginToken token = loginService.getLoginToken(id);
		if (token == null) {
			throw new LoginTokenNotFoundException("LoginToken not found.");
		}
		User user = userService.getUser(id);
		if (!StringUtils.equals(PasswordUtil.getPasswordHash(id, password), user.getPassword())) {
			throw new InvalidPasswordException("Invalid password.");
		}

		// トークンの削除
		em.remove(token);
	}
}
