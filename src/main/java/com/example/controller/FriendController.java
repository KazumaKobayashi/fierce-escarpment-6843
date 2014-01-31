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
	 * フレンド一覧を取得
	 *
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/", method=RequestMethod.GET)
	public void friends(
			@PathVariable("id") String id,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		System.out.println(id);
		System.out.println(token.getUserId());
		if (StringUtils.equals(token.getUserId(), id)) {
			// TODO: 正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("friends", friendService.getFriendList(id));
		} else {
			// 自身でなければNotFoundを返す
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		// 返却する値を設定する
		response.getWriter().println(res.getResponseJson());
	}

	/**
	 * フレンド申請
	 *
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/add", method = RequestMethod.POST)
	public void addFriend(
			@PathVariable("id") String id,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");
		try {
			friendService.create(token.getUserId(), id);
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
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * フレンド申請を許可
	 *
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/approve", method=RequestMethod.PUT)
	public void approve(
			@PathVariable("id") String id,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");
		try {
			FriendRelation relation = friendService.getFriendRelation(token.getUserId(), id);
			if (relation != null && !StringUtils.equals(relation.getPk().getId1(), token.getUserId())) {
				friendService.allow(token.getUserId(), id);
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
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * フレンド申請を却下
	 *
	 * @param id
	 * @param token
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/unapprove", method=RequestMethod.DELETE)
	public void unapprove(
			@PathVariable("id") String id,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");
		try {
			friendService.forbid(token.getUserId(), id);
			res.setStatusCode(0);
		} catch (FriendRelationNotFoundException e) {
			// TODO: 正しいエラーコードを設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}

		// 返却する値を設定する
		response.getWriter().print(res.getResponseJson());
	}
}
