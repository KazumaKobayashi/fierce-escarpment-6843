package com.example.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * パスワード関係のUtilクラス
 *
 * @author Kazuki Hasegawa
 */
public final class PasswordUtil {
	private PasswordUtil() {}

	private static final String ALGORITHM = "SHA-256";
	private static final String SALT = "DA7E1tgaq9lKreJN67ZU";

	/**
	 * パスワードにソルトを追加したデータのハッシュ値を取得、ソルトの生成にはユーザＩＤを入力とする関数を用いる。
	 * @param userId
	 * @param password
	 * @return
	 */
	public static String getPasswordHash(String userId, String password){
		return getHash(password, getSalt(userId) );
	}
 
	/**
	 * 引数で与えた文字列にソルトを連結してハッシュ値を取得
	 * @param target
	 * @param salt
	 * @return
	 */
	private static String getHash(String target, String salt){
		return getHash(target + salt);
	}
 
	/**
	 * 引数で与えた文字列のハッシュ値を取得
	 * @param target
	 * @return
	 */
	private static String getHash(String target){
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(target.getBytes());
			byte[] digest = md.digest();
			StringBuilder builder = new StringBuilder();
			for (byte b : digest) {
				builder.append(Integer.toHexString(b & 0xff));
			}
			hash = builder.toString();
		} catch (NoSuchAlgorithmException e) {
			//LOG.error(e.getLocalizedMessage(), e);
		}
		return hash;
	}
 
	private static String getSalt(String userId){
		return userId + SALT;
	}
}
