package com.example.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.model.LoginToken;
import com.example.service.LoginService;

/**
 * トークンのチェック
 * ログイン後に発行されるトークンがあるかないかで存在を確認する
 *
 * @author Kazuki Hasegawa
 */
@Component
public class TokenFilter extends OncePerRequestFilter {
	@Autowired
	private LoginService loginService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// パラメータの取得
		String token = (String) request.getParameter("token");
		String userId = (String) request.getParameter("id");

		if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(userId)) {
			// ログイントークンの存在確認
			// 存在していない　又は　トークンが等しくなけれBad Request
			LoginToken lToken = loginService.getLoginToken(userId);
			if (lToken == null || !StringUtils.equals(token, lToken.getToken())) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
			chain.doFilter(request, response);
		} else {
			// Tokenがない　又は　ユーザIdがない場合はBad Request
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
