package com.my.security.core.validate.code;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.my.security.core.validate.code.sms.SmsCodeSender;

@RestController
public class ValidateCodeController {
	
	public static final String SEESION_KEY = "SEESION_KEY_IMAGE_CODE";

	public static final String SEESION_KEY_PREFIX = null;
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Autowired
	private ValidateCodeGenerator imageCodeGenerator;
	
	@Autowired
	private ValidateCodeGenerator smsCodeGenerator;
	
	@Autowired
	private SmsCodeSender smsCodeSender;
	
	@GetMapping("/code/image")
	public void createCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//生成
		ImageCode imageCode = (ImageCode)imageCodeGenerator.generate(new ServletWebRequest(request));
		//保存在session
		sessionStrategy.setAttribute(new ServletWebRequest(request), SEESION_KEY, imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}
	
	@GetMapping("/code/sms")
	public void createSmsCode(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletRequestBindingException {
		//生成
		ValidateCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
		//保存在session
		sessionStrategy.setAttribute(new ServletWebRequest(request), SEESION_KEY, smsCode);
		//短信服务商发送出去
		String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
		smsCodeSender.send(mobile, smsCode.getCode());
	}


}
