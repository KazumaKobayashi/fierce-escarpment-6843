package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.UserService;

/**
 * 登録用のコントローラ
 *
 * @author Kazuki Hasegawa
 */
@Controller
public class RegisterController {
	@Autowired
	private UserService userService;

	/**
	 * ユーザの登録をする
	 *
	 * @param userId ユーザId
	 * @param password ユーザパスワード
	 * @return
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public String register(
			@RequestParam("id") String userId,
			@RequestParam("passwd") String password) {

		return userService.doRegist(userId, password).getResponseJson();
	}
}