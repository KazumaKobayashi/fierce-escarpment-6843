package com.example.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 日付や時間関係のUtilクラス
 *
 * @author Kazuki Hasegawa
 */
public final class DateUtil {
	private DateUtil() {}

	/**
	 * 現在時刻のタイムスタンプを取得する　
	 *
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		Date date = new Date();
		return new Timestamp(date.getTime());
	}
}
