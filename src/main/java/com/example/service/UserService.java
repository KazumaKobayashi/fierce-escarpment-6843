package com.example.service;

import com.example.jackson.Response;

public interface UserService {
	public Response regist(String username, String password);
	public Response getUser(String username);
	public Response getUsers();
}
