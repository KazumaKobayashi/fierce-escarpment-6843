package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;

	/**
	 * ユーザ認証を行う
	 * 成功すればaccessTokenを返す
	 *
	 * @param username
	 * @param password
	 * @return
	 */
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(
    		@RequestParam("name") String username,
			@RequestParam("passwd") String password){
    	return loginService.doLogin(username, password).getResponseJson();
    }
}
//外部からの受付　
