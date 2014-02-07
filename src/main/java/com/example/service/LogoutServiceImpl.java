package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(LogoutServiceImpl.class);

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
			logger.error("LoginToken not found. Id: {}", id);
			throw new LoginTokenNotFoundException("LoginToken not found. Id: " + id);
		}
		// トークンの削除
		em.remove(token);
	}
}
