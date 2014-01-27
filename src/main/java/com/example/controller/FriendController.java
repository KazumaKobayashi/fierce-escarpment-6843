package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.jackson.Response;
import com.example.model.FriendRelation;
import com.example.model.LoginToken;
import com.example.service.FriendService;
import com.example.service.LoginService;

/**
 * フレンド関係のコントローラ
 *
 * @author Kazuki Hasegawa
 */
@RequestMapping("/friends")
@Controller
public class FriendController {
	@Autowired
	private FriendService friendService;
	@Autowired
	private LoginService loginService;

	/**
	 * フレンド申請
	 *
	 * @param id
	 * @param token
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/add", method = RequestMethod.POST)
	public void addFriend(
			@PathVariable("id") String id,
			@RequestParam("token") String token,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			LoginToken lToken = loginService.getLoginTokenByToken(token);
			friendService.create(lToken.getUserId(), id);
			res.setStatusCode(0);
		} catch (FriendRelationExistsException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		} catch (UserNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}

		// 返却する値を設定する
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * フレンド申請を許可
	 *
	 * @param id
	 * @param token
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/approve", method=RequestMethod.PUT)
	public void approve(
			@PathVariable("id") String id,
			@RequestParam("token") String token,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			LoginToken lToken = loginService.getLoginTokenByToken(token);
			FriendRelation relation = friendService.getFriendRelation(lToken.getUserId(), id);
			if (relation != null && !StringUtils.equals(relation.getPk().getId1(), lToken.getUserId())) {
				friendService.allow(lToken.getUserId(), id);
				res.setStatusCode(0);
			} else {
				// 自分で出した申請を許可させるわけにはいかない
				// TODO: 正しいエラーコードを設定のこと
				res.setStatusCode(-1);
			}
		} catch (FriendRelationNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}

		// 返却する値を設定する
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * フレンド申請を却下
	 *
	 * @param id
	 * @param token
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/unapprove", method=RequestMethod.DELETE)
	public void unapprove(
			@PathVariable("id") String id,
			@RequestParam("token") String token,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			LoginToken lToken = loginService.getLoginTokenByToken(token);
			friendService.forbid(lToken.getUserId(), id);
			res.setStatusCode(0);
		} catch (FriendRelationNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}

		// 返却する値を設定する
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}
}
