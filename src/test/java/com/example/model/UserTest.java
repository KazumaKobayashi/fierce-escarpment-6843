/**
 * 
 */
package com.example.model;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jayway.jsonassert.JsonAssert;

import static org.hamcrest.core.Is.is;

/**
 * ユーザモデルのテスト
 *
 * @author Kazuki Hasegawa
 */
@RunWith(JUnit4.class)
public class UserTest extends AbstractModelTest {
	private String id = UserTest.class.getName();

	/**
	 * UserモデルのJSON文字列変換テスト
	 *
	 * @throws IOException
	 */
	@Test
	public void JSON文字列変換テスト() throws IOException {
		// 必要なデータを作成
		Double lat = 45.1235, lng = 41.345;
		Coordinate coord = new Coordinate();
		coord.setLng(lng);
		coord.setLat(lat);
		User user = new User();
		user.setId(id);
		user.setUsername(id);
		user.setCoord(coord);

		String json = mapper.writeValueAsString(user);
		JsonAssert
			.with(json)
			.assertThat("$.id", is(id))
			.assertThat("$.username", is(id))
			.assertThat("$.lat", is(lat))
			.assertThat("$.lng", is(lng));
	}
}
