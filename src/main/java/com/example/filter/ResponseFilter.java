package com.example.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.service.LoginService;

/**
 * Responseに必要な設定を施す
 *
 * @author Kazuki Hasegawa
 */
@Component
public class ResponseFilter extends OncePerRequestFilter {
	@Autowired
	private LoginService loginService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		response.setContentType("application/json");
		chain.doFilter(request, response);
	}
}
