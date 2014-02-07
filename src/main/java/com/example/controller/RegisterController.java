package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jackson.Response;
import com.example.service.UserService;

/**
 * 登録用のコントローラ
 *
 * @author Kazuki Hasegawa
 * @author Kazuma Kobayashi
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
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public void register(
			@RequestParam("id") String userId,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			HttpServletResponse response) throws IOException {

		Response res = userService.regist(userId, email, password);

		// レスポンスの設定
		response.getWriter().print(res.getResponseJson());
	}
}