package com.my.security.browser.support;

/**
 * 
 * @author josephvitt
 *
 */
public class SimpleResponse {
	
	public SimpleResponse(Object content) {
		this.content = content;
	}

	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	
}
