package com.spring.component;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.service.IpControlService;


public class IpControlInterceptor extends HandlerInterceptorAdapter {

	private final IpControlService ipControlService;

	public IpControlInterceptor(IpControlService ipControlService) {
		this.ipControlService = ipControlService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String address = request.getRemoteAddr();
		String originAddress = request.getHeader("X-Forwarded-For");

		Optional<String> reason = ipControlService.checkAddress(originAddress == null ? address : originAddress);

		if(reason.isPresent()) {
			response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
			response.getWriter().println(reason.get());
			return false;
		}

		return true;
	}
}
