package com.example.util;

import org.apache.commons.lang3.StringUtils;

/**
 * エスケープ処理のUtilクラス
 *
 * @author Kazuki Hasegawa
 */
public final class EscapeUtil {
	private EscapeUtil() {}

	/**
	 * 簡易的なSQLエスケープ処理
	 *
	 * @param str
	 * @return
	 */
	public static String escapeSQL(String str) {
		str = str.replaceAll("'", "''");
		str = str.replaceAll("\"", "\"\"");
		str = str.replaceAll("\\\\", StringUtils.EMPTY);
		return str;
	}
}