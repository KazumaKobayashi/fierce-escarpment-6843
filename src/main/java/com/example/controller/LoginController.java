package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
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
import com.example.util.StatusCodeUtil;

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
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public void login(
			@RequestParam("id") String userId,
			@RequestParam("password") String password,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		Response res = new Response();

		try {
			LoginToken token = loginService.createToken(userId, password);
			request.getSession().setAttribute("token", token);
			res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			res.addObjects("token", token.getToken());
		} catch (UserNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage("ユーザId又はパスワードが異なります。");
		} catch (InvalidPasswordException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage("ユーザId又はパスワードが異なります。");
		} catch (LoginTokenExistsException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage("2重ログインは出来ません。");
		}

		// レスポンスの設定
		response.getWriter().print(res.getResponseJson());
	}
}
