package com.example.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * DateUtilのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.util.DateUtil
 */
@RunWith(JUnit4.class)
public class DateUtilTest {

	/**
	 * 指定時間過ぎているのでtrueが返ってくるかのテスト
	 */
	@Test
	public void タイムスタンプから指定時間過ぎているかをチェックする() {
		Calendar cal = Calendar.getInstance();
		// 1時間前に設定する
		cal.add(Calendar.HOUR, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		assertThat(DateUtil.isTimestampBeforeFewHours(ts, 1), is(true));
	}

	/**
	 * 指定時間過ぎていないのでfalseが返ってくるかのテスト
	 */
	@Test
	public void タイムスタンプから指定時間過ぎていないのにチェックする() {
		assertThat(DateUtil.isTimestampBeforeFewHours(new Timestamp(new Date().getTime()), 1), is(false));
	}

	/**
	 * 指定分過ぎているのでtrueが返ってくるかのテスお
	 */
	@Test
	public void タイムスタンプから指定分過ぎているかをチェックする() {
		Calendar cal = Calendar.getInstance();
		// 0分前に設定する
		cal.add(Calendar.MINUTE, -1);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		assertThat(DateUtil.isTimestampBeforeFewMinutes(ts, 1), is(true));
	}

	/**
	 * 指定分過ぎていないのでtrueが返ってくるかのテスト
	 */
	@Test
	public void タイムスタンプから指定分過ぎていないのにチェックする() {
		assertThat(DateUtil.isTimestampBeforeFewMinutes(new Timestamp(new Date().getTime()), 1), is(false));
	}
}
