package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exception.CoordinateNotFoundException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.service.CoordinateService;
import com.example.service.FriendService;
import com.example.service.LoginService;
import com.example.service.UserService;
import com.example.util.StatusCodeUtil;

/**
 * ユーザに関するコントローラ
 *
 * @author Kauzki Hasegawa
 * @author Kazuma Kobaayshi
 */
@RequestMapping("/users")
@Controller
public class UserController {
	@Autowired
	private FriendController friendController;

	@Autowired
	private UserService userService;
	@Autowired
	private CoordinateService coordinateService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private FriendService friendService;

	/**
	 * ユーザの情報を取得
	 *
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/{id}/info", method=RequestMethod.GET)
	public void user(
			@PathVariable("id") String userId,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		User user = userService.getUser(userId);
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		if (user != null) {
			if (StringUtils.equals(userId, token.getUserId())
					|| friendService.isFriend(userId, token.getUserId())) {
				res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
				res.addObjects("user", user);
			} else {
				res.setStatusCode(StatusCodeUtil.getStatusCode(FriendRelationNotFoundException.class));
			}
		} else {
			res.setStatusCode(StatusCodeUtil.getStatusCode(UserNotFoundException.class));
			res.addErrorMessage("User not found.");
		}

		// 返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * ユーザ情報を更新
	 * 自分からのアクセスでなければ登録出来ない
	 *
	 * @param userId
	 * @param email
	 * @param username
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/{id}/info", method=RequestMethod.PUT)
	public void userUpdate(
			@PathVariable("id") String userId,
			@RequestParam("email") String email,
			@RequestParam("name") String username,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		try {
			if (StringUtils.equals(token.getUserId(), userId)) {
				User user = userService.update(userId, email, username);
				res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
				res.addObjects("user", user);
			} else {
				// ログイントークンが不正だった場合
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (UserNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
		}

		// 返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * パスワードの更新を行う
	 * 現在のパスワードと新しいパスワードが必要となる
	 * 自分からのアクセスでなければ更新出来ない
	 *
	 * @param userId
	 * @param currentPassword
	 * @param newPassword
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/{id}/password", method=RequestMethod.PUT)
	public void update(
			@PathVariable("id") String userId,
			@RequestParam("current_password") String currentPassword,
			@RequestParam("new_password") String newPassword,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		try {
			if (StringUtils.equals(token.getUserId(), userId)) {
				userService.changePassword(userId, currentPassword, newPassword);
				res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			} else {
				// ログイントークンが不正だった場合
				response.sendError(HttpServletResponse.SC_NOT_FOUND);			
			}
		} catch (UserNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(UserNotFoundException.class.getClass()));
			res.addErrorMessage(e.toString());
		} catch (InvalidPasswordException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
		}
		// 返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * ユーザの座標を更新する
	 * 自分からのアクセスでなければ更新できない
	 *
	 * @param userId
	 * @param lat
	 * @param lng
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/{id}/coordinate", method=RequestMethod.PUT)
	public void update(
			@PathVariable("id") String userId,
			@RequestParam("lat") Double lat,
			@RequestParam("lng") Double lng,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		lat = lat == null ? 0 : lat;
		lng = lng == null ? 0 : lng;
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		try {
			if (StringUtils.equals(token.getUserId(), userId)) {
				coordinateService.update(userId, lat, lng);
				res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			} else {
				//ログインユーザが不正だった場合
				response.sendError(HttpServletResponse.SC_NOT_FOUND);//自分自身の情報を引き取る等の処理に関してエラーが起きた場合はこれを返す
			}
		} catch (UserNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
		}

		// 返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * 距離を算出してくれるURL
	 * メートルとキロメートルで返してくれる
	 *
	 * @param userId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/coordinate/diff", method=RequestMethod.GET)
	public void getDistanceBetween(
			@PathVariable("id") String userId,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		try {
			double meter = Math.floor(coordinateService.getDistanceBetween(userId, token.getUserId()));
			res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			res.addObjects("meter", Math.round(meter));
			res.addObjects("kilometer", meter / 1000.0);
		} catch (CoordinateNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
		}

		// 返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * フレンド一覧を取得
	 * 実装は、フレンドに処理を流すだけ
	 *
	 * @param userId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/friends", method=RequestMethod.GET)
	public void friends(
			@PathVariable("id") String userId,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		friendController.friends(userId, request, response);
	}

	/**
	 * フレンド申請中一覧を取得
	 * 実装はフレンドに処理を流すだけ
	 *
	 * @param userId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/relating", method=RequestMethod.GET)
	public void relating(
			@PathVariable("id") String userId,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		friendController.relating(userId, request, response);
	}

	/**
	 * フレンド申請待ち一覧を取得
	 * 実装はフレンドに処理を流すだけ
	 *
	 * @param userId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/related", method=RequestMethod.GET)
	public void related(
			@PathVariable("id") String userId,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		friendController.related(userId, request, response);
	}
}
