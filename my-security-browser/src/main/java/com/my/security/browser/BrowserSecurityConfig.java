package com.my.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.my.security.core.authentication.core.SmsCodeAuthenticationSecurityConfig;
import com.my.security.core.properties.SecurityProperties;
import com.my.security.core.validate.code.SmsCodeFilter;
import com.my.security.core.validate.code.ValidateCodeFilter;



@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler myAuthenticationFailHandler;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
		
		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailHandler);
		smsCodeFilter.setSecurityProperties(securityProperties);
		smsCodeFilter.afterPropertiesSet();
		
		http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin()
//			.loginPage("/my-signIn.html")
			.loginPage("/authentication/require")
			.loginProcessingUrl("/authentication/form")
			.successHandler(myAuthenticationSuccessHandler)
			.failureHandler(myAuthenticationFailHandler)
			.and()
			.rememberMe()
			.tokenRepository(persistentTokenRepository())
			.tokenValiditySeconds(securityProperties.getBroswer().getRememberMeSeconds())
			.userDetailsService(userDetailsService)
//		http.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers("/authentication/require",
					securityProperties.getBroswer().getLoginPage(),
					"/code/image").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf().disable()
			.apply(smsCodeAuthenticationSecurityConfig);
	}

}
