package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenExistsException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.LoginToken;
import com.example.service.LoginService;

/**
 * ログインのコントローラ
 *
 * @author Kazuki Hasegawa
 * @author Kazuma Kobayashi
 */
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
	 * @throws IOException 
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public void login(
			@RequestParam("id") String userId,
			@RequestParam("password") String password,
			HttpServletResponse response) throws IOException{
		Response res = new Response();
		try {
			LoginToken token = loginService.doLogin(userId, password);
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("token", token.getToken());
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.setErrorMessage(e.toString());
		} catch (InvalidPasswordException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.setErrorMessage(e.toString());
		} catch (LoginTokenExistsException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.setErrorMessage(e.toString());
		}
		// レスポンスの設定
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}
}
