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
	 * @param userId ユーザId
	 * @param password パスワード
	 * @return
	 */
    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public String login(
    		@RequestParam("id") String userId,
			@RequestParam("passwd") String password){
    	return loginService.doLogin(userId, password).getResponseJson();
    }
}
