package com.spring.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.entity.User;

public class BlockedUserInterceptor extends HandlerInterceptorAdapter {

	private final UserContext userContext;

	public BlockedUserInterceptor(UserContext userContext) {
		this.userContext = userContext;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		User user = null;

		try {
			user = userContext.getCurrentUser();
		} catch(Exception e) {
			return true;
		}

		if(user != null && !user.getActive() && user.getProfile() != null) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.getWriter().println("user_blocked");
			return false;
		}

		return true;
	}

}
