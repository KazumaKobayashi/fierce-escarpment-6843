/**
 * 
 */
package com.example.jackson;

import java.util.Map;

import com.example.model.User;

/**
 * @author Kazuki Hasegawa
 */
public class Group {
	private String name;
	private Map<String, User> users;

	public void setName(String name) {
		this.name = name;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public Map<String, User> getUsers() {
		return users;
	}
}
