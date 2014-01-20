package com.example.service;

import com.example.jackson.Response;

public interface LoginService {
	public Response doLogin(String username, String password);
}
