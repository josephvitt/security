package com.my.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="my.security")
public class SecurityProperties {
	
	private BrowserProperties broswer = new BrowserProperties();
	
	private ValidateCodeProperties code = new ValidateCodeProperties();

	public BrowserProperties getBroswer() {
		return broswer;
	}

	public void setBroswer(BrowserProperties broswer) {
		this.broswer = broswer;
	}

	public ValidateCodeProperties getCode() {
		return code;
	}

	public void setCode(ValidateCodeProperties code) {
		this.code = code;
	}
	
	
	
}
