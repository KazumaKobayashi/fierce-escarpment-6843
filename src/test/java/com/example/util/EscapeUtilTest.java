package com.example.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * EscapeUtilのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.util.EscapeUtil
 */
@RunWith(JUnit4.class)
public class EscapeUtilTest {
	/**
	 * SQLとして不正な文字列をエスケープする
	 */
	@Test
	public void SQLエスケープする() {
		assertThat(EscapeUtil.escapeSQL("''"), is("''''"));
		assertThat(EscapeUtil.escapeSQL("\"\""), is("\"\"\"\""));
		assertThat(EscapeUtil.escapeSQL("\\\\"), is(StringUtils.EMPTY));
	}
}
