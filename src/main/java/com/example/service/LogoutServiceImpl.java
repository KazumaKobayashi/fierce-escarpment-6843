package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenNotFoundException;
import com.example.model.LoginToken;

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
	public void deleteToken(String id) throws LoginTokenNotFoundException, InvalidPasswordException {
		LoginToken token = loginService.getLoginToken(id);
		if (token == null) {
			throw new LoginTokenNotFoundException("LoginToken not found.");
		}
		// トークンの削除
		em.remove(token);
	}
}
