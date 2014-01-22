package com.example.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.example.AbstractTest;
import com.example.model.Coordinate;
import com.example.model.User;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnit4.class)
public class GroupTest extends AbstractTest {
	@Test
	public void testWriteValueAsString() throws IOException {
		String name = "Test";
		Map<String, User> users = new HashMap<String, User>();
		users.put("kazuma01", createUser());
		Group group = new Group();
		group.setName(name);
		group.setUsers(users);
		String json = mapper.writeValueAsString(group);

		StringBuilder builder = new StringBuilder("{\"name\":\"");
		builder.append(name);
		builder.append("\",\"users\":{\"kazuma01\":{\"username\":\"Kazuma\",\"lat\":45.1235,\"lng\":41.345}}}");
		assertThat(json, is(builder.toString()));
		System.out.println(mapper.writeValueAsString(users));
	}

	private User createUser() {
		String name = "Kazuma";
		Double lat = 45.1235, lng = 41.345;
		Coordinate coord = new Coordinate();
		coord.setLng(lng);
		coord.setLat(lat);
		User user = new User();
		user.setUsername(name);
		user.setCoord(coord);
		return user;
	}
}
