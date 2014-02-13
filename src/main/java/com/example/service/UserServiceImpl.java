package com.example.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.CoordinateExistsException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.User;
import com.example.util.DateUtil;
import com.example.util.EscapeUtil;
import com.example.util.PasswordUtil;
import com.example.util.StatusCodeUtil;

/**
 * UserServiceの実装クラス
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.UserService
 */
@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private CoordinateService coordinateService;

	@PersistenceContext
	EntityManager em;

	// ユーザIdのパターン
	private static Pattern USER_ID_PATTERN = Pattern.compile("^[0-9a-zA-Z]+$");
	// メールアドレスのパターン
	private static Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	// パスワードのパターン
	private static Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z\\-]+$");

	@Transactional
	@Override
	public Response regist(String userId, String email, String password) {
		Response response = new Response();
		if (StringUtils.isBlank(userId)) {
			response.addErrorMessage("ユーザIdを入力してください。");
		} else if (userId.length() > User.USER_ID_MAX_LENGTH || userId.length() < 6) {
			response.addErrorMessage(String.format("ユーザIdは、6文字以上%s以内で入力してください。", User.USER_ID_MAX_LENGTH));
		} else {
			Matcher matcher = USER_ID_PATTERN.matcher(userId);
			if (!matcher.matches()) {
				response.addErrorMessage("不正なユーザIdです。");
			} else if (em.find(User.class, userId) != null) {
				response.addErrorMessage("そのユーザIdはすでに使われています。");
			}
		}

		if (StringUtils.isBlank(email)) {
			response.addErrorMessage("メールアドレスを入力してください。");
		} else if (email.length() > User.EMAIL_MAX_LENGTH) {
			response.addErrorMessage("メールアドレスが長過ぎます。");
		} else {
			Matcher matcher = EMAIL_PATTERN.matcher(email);
			if (!matcher.matches()) {
				response.addErrorMessage("不正なメールアドレスです。");
			} else if (getUserByEmail(email) != null) {
				response.addErrorMessage("そのメールアドレスはすでに使われています。");
			}
		}

		if (StringUtils.isBlank(password)) {
			response.addErrorMessage("パスワードを入力してください。");
		} else if (password.length() > 127) {
			response.addErrorMessage("パスワードが長過ぎます。");
		} else {
			Matcher matcher = PASSWORD_PATTERN.matcher(password);
			if (!matcher.matches()) {
				response.addErrorMessage("不正なパスワードです。");
			}
		}

		if (response.hasErrors()) {
			// TODO: 正しいエラーコードを設定のこと
			response.setStatusCode(-1);
		} else {
			response.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			response.addObjects("user", create(userId, email, password));
		}
		return response;
	}

	@Transactional
	@Override
	public User create(String userId, String email, String password) {
		// エスケープ処理
		userId = EscapeUtil.escapeSQL(userId);
		password = EscapeUtil.escapeSQL(password);

		// 現在時刻のタイムスタンプを取得
		Timestamp now = DateUtil.getCurrentTimestamp();

		// ユーザ登録
		User user = new User();
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
			logger.error(e.getMessage());
		} catch (UserNotFoundException e) {
			// 起こりえるわけがないけど念の為に
			logger.error(e.getMessage());
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

		Matcher matcher = EMAIL_PATTERN.matcher(email);
		System.out.println(matcher.matches());
		if (matcher.matches() && getUserByEmail(email) == null) {
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
	public Response changePassword(String userId, String currentPassword, String newPassword) {
		Response response = new Response();
		User user = em.find(User.class, userId);
		// ユーザ存在確認
		if (user == null) {
			response.setStatusCode(StatusCodeUtil.getStatusCode(UserNotFoundException.class));
			response.addErrorMessage("ユーザが存在しません。");
			logger.error("User not found. Id: {}", userId);
			return response;
		}
		// パスワードの妥当性チェック
		if (!StringUtils.equals(PasswordUtil.getPasswordHash(userId, currentPassword), user.getPassword())) {
			response.setStatusCode(StatusCodeUtil.getStatusCode(InvalidPasswordException.class));
			response.addErrorMessage("現在のパスワードが間違っています。");
			logger.error("Invalid password. Id: {}", userId);
			return response;
		}
		// パスワードの長さチェック
		if (currentPassword.length() > 127) {
			response.setStatusCode(StatusCodeUtil.getStatusCode(InvalidPasswordException.class));
			response.addErrorMessage("新しいパスワードが長過ぎます。");
			logger.error("Invalid password. Id: {}", userId);
			return response;
		}
		response.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
		String password = PasswordUtil.getPasswordHash(userId, newPassword);
		user.setPassword(password);
		em.persist(user);
		return response;
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
