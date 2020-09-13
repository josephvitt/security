package com.my.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.security.core.properties.LoginType;
import com.my.security.core.properties.SecurityProperties;

@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler/*implements AuthenticationSuccessHandler*/{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.info(authentication.getPrincipal().toString());
		logger.info("登录成功");
		LoginType type = securityProperties.getBroswer().getLoginType();
		logger.info(type.toString());
		if(LoginType.JOSN.equals(securityProperties.getBroswer().getLoginType())) {
			logger.info("hello json");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(authentication));
		} else {
			logger.info("hello redirect");
			super.onAuthenticationSuccess(request,response,authentication);
		}
		
	}

}
