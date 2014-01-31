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
import com.example.exception.InvalidPasswordException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.service.CoordinateService;
import com.example.service.FriendService;
import com.example.service.LoginService;
import com.example.service.UserService;

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
			// TODO: ここらへんをどうにかしてまとめたい
			if (StringUtils.equals(userId, token.getUserId())) {
				// ログイントークンが自分自身ならば友達関係を追加
				res.addObjects("relating_users", friendService.getRelatingList(userId));
				res.addObjects("related_users", friendService.getRelatedList(userId));
				res.addObjects("friend_users", friendService.getFriendList(userId));
				// TODO: 正しいステータスコードを設定のこと
				res.setStatusCode(0);
				res.addObjects("user", user);
			} else if (friendService.isFriend(userId, token.getUserId())) {
				// TODO: 正しいステータスコードを設定のこと
				res.setStatusCode(0);
				res.addObjects("user", user);
			} else {
				// TODO: 正しいエラーコードを設定のこと(FriendRelationNotFoundExceptionと同じ)
				res.setStatusCode(-1);
			}
		} else {
			// TODO: 正しいエラーコードを設定のこと(UserNotFoundExceptionと同じ)
			res.setStatusCode(-1);
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
				// TODO: 正しいステータスコードを設定のこと
				res.setStatusCode(0);
				res.addObjects("user", user);
			} else {
				// ログイントークンが不正だった場合
				// TODO: 正しいエラーコードを設定のこと
				res.setStatusCode(-1);
			}
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
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
				// TODO: 正しいステータスコードを設定のこと
				res.setStatusCode(0);
			} else {
				// ログイントークンが不正だった場合
				// TODO: 正しいエラーコードを設定のこと
				res.setStatusCode(-1);
			}
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
		} catch (InvalidPasswordException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
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
				// TODO: 正しいステータスコードを設定のこと
				res.setStatusCode(0);
			} else {
				// ログイントークンが不正だった場合
				// TODO: 正しいエラーコードを設定のこと
				res.setStatusCode(-1);
			}
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
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
			res.setStatusCode(0);
			res.addObjects("meter", Math.round(meter));
			res.addObjects("kilometer", meter / 1000.0);
		} catch (CoordinateNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}

		// 返却する値
		response.getWriter().print(res.getResponseJson());
	}
}
