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
		// セッションからトークンを取り出す
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		if (token != null) {
			chain.doFilter(request, response);
			return;
		}

		// パラメータがあった場合歯セッションを作る
		String tokenString = request.getParameter("token");
		if (StringUtils.isNotEmpty(tokenString)) {
			token = loginService.getLoginTokenByToken(tokenString);
			if (token != null) {
				request.getSession().setAttribute("token", token);
				chain.doFilter(request, response);
				return;
			}
		}

		// Tokenが見つからない場合は、Bad request
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
}
