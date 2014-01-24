package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exception.InvalidPasswordException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.User;
import com.example.service.CoordinateService;
import com.example.service.UserService;

/**
 * ユーザに関するコントローラ
 *
 * @author Kauzki Hasegawa
 * @author Kazuma Kobaayshi
 */
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private CoordinateService coordinateService;

	/**
	 * ユーザの情報を取得
	 *
	 * @param userId
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/users/{id}/info", method=RequestMethod.GET)
	public void user(@PathVariable("id") String userId, HttpServletResponse response) throws IOException {
		Response res = new Response();
		User user = userService.getUser(userId);
		if (user != null) {
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("user", user);
		} else {
			// TODO: 正しいエラーコードを設定のこと(UserNotFoundExceptionと同じ)
			res.setStatusCode(-1);
		}
		// 返却する値
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * ユーザ情報を更新
	 *
	 * @param userId
	 * @param username
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/users/{id}/info", method=RequestMethod.PUT)
	public void userUpdate(
			@PathVariable("id") String userId,
			@RequestParam("email") String email,
			@RequestParam("name") String username,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			User user = userService.update(userId, email, username);
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("user", user);
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		}
		// 返却する値
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * パスワードの更新を行う
	 * 現在のパスワードと新しいパスワードが必要となる
	 *
	 * @param userId
	 * @param currentPassword
	 * @param newPassword
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/users/{id}/password", method=RequestMethod.PUT)
	public void update(
			@PathVariable("id") String userId,
			@RequestParam("current_password") String currentPassword,
			@RequestParam("new_password") String newPassword,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			userService.changePassword(userId, currentPassword, newPassword);
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		} catch (InvalidPasswordException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		}
		// 返却する値
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * ユーザの座標を更新する
	 *
	 * @param userId
	 * @param lat
	 * @param lng
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/users/{id}/coordinate", method=RequestMethod.PUT)
	public void update(
			@PathVariable("id") String userId,
			@RequestParam("lat") Double lat,
			@RequestParam("lng") Double lng,
			HttpServletResponse response) throws IOException {
		lat = lat == null ? 0 : lat;
		lng = lng == null ? 0 : lng;
		Response res = new Response();
		coordinateService.update(userId, lat, lng);
		// TODO: 正しいステータスコードを設定のこと
		res.setStatusCode(0);
		// 返却する値
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}
}
