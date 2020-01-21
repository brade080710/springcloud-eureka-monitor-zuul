package com.jdd.partition.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jdd.partition.app.filter.AppSignFilter;
import com.jdd.partition.app.filter.AppTokenAuthFilter;

import javax.swing.*;


@Configuration
public class FilerConfig {
	@Autowired
	private AppSignFilter appSignFilter;
	@Autowired
	private AppTokenAuthFilter appTokenAuthFilter;
	@Autowired
	private CorsFilter corsFilter;

	@Bean
	public FilterRegistrationBean<AppSignFilter> appSignFilterRegistrationBean() {
		FilterRegistrationBean<AppSignFilter> registration = new FilterRegistrationBean<AppSignFilter>();
		registration.setFilter(appSignFilter);
		registration.addUrlPatterns("/app/sign/*");
		registration.setName("appSignFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<AppTokenAuthFilter> appTokenAuthFilterRegistrationBean() {
		FilterRegistrationBean<AppTokenAuthFilter> registration = new FilterRegistrationBean<AppTokenAuthFilter>();
		registration.setFilter(appTokenAuthFilter);
		registration.addUrlPatterns("/app/token/*");
		registration.setName("appTokenAuthFilter");
		registration.setOrder(2);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(corsFilter);
		registration.addUrlPatterns("/*");
		registration.setName("corsFilter");
		registration.setOrder(0);
		return registration;
	}
}
