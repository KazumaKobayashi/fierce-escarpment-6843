package com.example.service;

import com.example.jackson.Response;

public interface UserService {
	public Response regist(String username, String password);
	public Response getUser(String id);
	public Response getUsers();
}
