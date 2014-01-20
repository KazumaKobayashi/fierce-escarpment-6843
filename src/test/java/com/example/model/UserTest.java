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
	public void testWriteValueAsString() throws IOException {
		String name = "Kazuma";
		Double lat = 45.1235, lng = 41.345;

		User user = new User();
		user.setUsername(name);
		user.setLng(lng);
		user.setLat(lat);

		String json = mapper.writeValueAsString(user);
		StringBuilder builder = new StringBuilder("{\"username\":\"");
		builder.append(name);
		builder.append("\",\"lat\":");
		builder.append(lat);
		builder.append(",\"lng\":");
		builder.append(lng);
		builder.append("}");
		assertThat(json, is(builder.toString()));
	}
}
