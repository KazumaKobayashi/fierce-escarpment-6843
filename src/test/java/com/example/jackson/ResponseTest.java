package com.example.jackson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.example.model.AbstractModelTest;
import com.jayway.jsonassert.JsonAssert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

/**
 * Responseのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.jackson.Response
 */
@RunWith(JUnit4.class)
public class ResponseTest extends AbstractModelTest {

	@Test
	public void エラーメッセージの存在確認テスト() {
		Response res = new Response();
		// この時点ではないはず
		assertThat(res.hasErrors(), is(false));

		res.addErrorMessage("エラー");
		assertThat(res.hasErrors(), is(true));
	}

	/**
	 * エラーメッセージのテスト
	 */
	@Test
	public void エラーメッセージテスト() {
		String err1 = "エラーです。";
		String err2 = "パスワードが違います。";
		Response res = new Response();
		res.setStatusCode(0);
		res.addErrorMessage(err1);
		res.addErrorMessage(err2);

		JsonAssert
			.with(res.getResponseJson())
			.assertThat("$.code", is(0))
			.assertThat("$.msg", hasItems(err1, err2));
	}
}
