package com.example.util;

import java.util.HashMap;
import java.util.Map;

import com.example.exception.CoordinateExistsException;
import com.example.exception.CoordinateNotFoundException;
import com.example.exception.EmailExistsException;
import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.GroupNotFoundException;
import com.example.exception.InvalidEmailException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenExistsException;
import com.example.exception.LoginTokenNotFoundException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;

/**
 * ステータスコードのUtilクラス
 *
 * @author Kazuma Kobayashi
 * @author Kazuki Hasegawa
 */
public final class StatusCodeUtil {
	private StatusCodeUtil() {}
	private static final Map<Class<?>, Integer> codes;

	static {
		codes = new HashMap<Class<?>, Integer>();
		codes.put(CoordinateExistsException.class, 101);
		codes.put(CoordinateNotFoundException.class,102);
		codes.put(EmailExistsException.class,111);
		codes.put(FriendRelationExistsException.class,121);
		codes.put(FriendRelationNotFoundException.class,122);
		codes.put(GroupNotFoundException.class,131);
		codes.put(InvalidEmailException.class,112);
		codes.put(InvalidPasswordException.class,141);
		codes.put(LoginTokenExistsException.class,151);
		codes.put(LoginTokenNotFoundException.class,152);
		codes.put(UserExistsException.class,161);
		codes.put(UserNotFoundException.class,162);
		
	}

	/**
	 * エラーコードを取得
	 *
	 * @param klass
	 * @return
	 */
	public static Integer getStatusCode(Class<?> klass) {
		if (klass != null && codes.containsKey(klass)) {
			return codes.get(klass);
		}
		return -1;
	}

	/**
	 * 成功時のステータスコード
	 *
	 * @return
	 */
	public static Integer getSuccessStatusCode() {
		return 0;
	}
}
