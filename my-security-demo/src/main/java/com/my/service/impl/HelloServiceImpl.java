/**
 * 
 */
package com.my.service.impl;

import org.springframework.stereotype.Service;

import com.my.service.HelloService;

/**
 * @author zx
 *
 */
@Service
public class HelloServiceImpl implements HelloService {

	/* (non-Javadoc)
	 * @see com.imooc.service.HelloService#greeting(java.lang.String)
	 */
	@Override
	public String greeting(String name) {
		System.out.println("greeting");
		return "hello "+name;
	}

}
