package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.exception.InvalidPasswordException;
import com.example.exception.LoginTokenNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.LoginToken;
import com.example.service.CoordinateService;
import com.example.service.LogoutService;
import com.example.util.StatusCodeUtil;

/**
 * ログアウトコントローラ
 * @author Kazuma Kobayashi
 * @author Kazuki Hasegawa
 */
@RequestMapping("/logout")
@Controller
public class LogoutController {
	private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

	@Autowired
	private LogoutService logoutService;
	@Autowired
	private CoordinateService coordinateService;

	/**
	 * ログアウトする
	 *
	 * @param password
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public void logout(
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");
		try {
			String id = token.getUserId();
			logoutService.deleteToken(id);
			request.getSession().removeAttribute("token");
			res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			try {
				coordinateService.update(id, null, null);
			} catch (UserNotFoundException e) {
				// 別にこれが成功しなくてもよい
				logger.warn("Faild coordinate update.");
			}
		} catch (LoginTokenNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
			logger.error(e.toString());
		} catch (InvalidPasswordException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
			logger.error(e.toString());
		}
		// レスポンスを設定
		response.getWriter().print(res.getResponseJson());
	}
}
