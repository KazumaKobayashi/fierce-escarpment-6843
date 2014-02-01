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
import com.example.exception.LoginTokenNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.service.CoordinateService;
import com.example.service.LogoutService;

/**
 * ログアウトコントローラ
 *
 * @author Kazuki Hasegawa
 */
@RequestMapping("/logout")
@Controller
public class LogoutController {

	@Autowired
	private LogoutService logoutService;
	@Autowired
	private CoordinateService coordinateService;

	/**
	 * ログアウトする
	 *
	 * @param id
	 * @param password
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public void logout(
			@RequestParam("id") String id,
			@RequestParam("password") String password,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			logoutService.deleteToken(id, password);
			request.getSession().removeAttribute("token");
			res.setStatusCode(0);
			try {
				coordinateService.update(id, null, null);
			} catch (UserNotFoundException e) {
				// 別にこれが成功しなくてもよい
				// TODO: ロガーの設定
				e.printStackTrace();
			}
		} catch (LoginTokenNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		} catch (InvalidPasswordException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}
		// レスポンスを設定
		response.getWriter().print(res.getResponseJson());
	}
}
