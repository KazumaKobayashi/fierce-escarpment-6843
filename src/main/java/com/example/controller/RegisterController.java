package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exception.UserExistsException;
import com.example.jackson.Response;
import com.example.model.User;
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
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public void register(
			@RequestParam("id") String userId,
			@RequestParam("password") String password,
			HttpServletResponse response) throws IOException {

		Response res = new Response();
		try {
			User user = userService.create(userId, password);
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("user", user);
		} catch (UserExistsException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		}
		// レスポンスの設定
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}
}