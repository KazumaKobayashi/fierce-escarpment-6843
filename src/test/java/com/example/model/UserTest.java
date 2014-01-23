/**
 * 
 */
package com.example.model;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.example.AbstractTest;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Kazuki Hasegawa
 */
@RunWith(JUnit4.class)
public class UserTest extends AbstractTest {
	@Test
	public void JSON文字列変換テスト() throws IOException {
		String id = "Kazuma";
		Double lat = 45.1235, lng = 41.345;

		Coordinate coord = new Coordinate();
		coord.setLng(lng);
		coord.setLat(lat);
		User user = new User();
		user.setId(id);
		user.setUsername(id);
		user.setCoord(coord);

		String json = mapper.writeValueAsString(user);
		StringBuilder builder = new StringBuilder("{\"username\":\"");
		builder.append(id);
		builder.append("\",\"lat\":");
		builder.append(lat);
		builder.append(",\"lng\":");
		builder.append(lng);
		builder.append(",\"id\":\"");
		builder.append(id);
		builder.append("\"}");
		assertThat(json, is(builder.toString()));
	}
}
