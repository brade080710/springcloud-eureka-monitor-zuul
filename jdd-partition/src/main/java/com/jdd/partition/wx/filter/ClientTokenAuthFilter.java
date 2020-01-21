package com.jdd.partition.wx.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


/**
 * @author 刘军伟
 * function：过滤器
 * 加密解密:敏感信息，具体到接口里加解密
 * 是否携带token：
 *
 */
@WebFilter(urlPatterns = "/wx/token/*", filterName = "wxTokenAuthFilter")
public class ClientTokenAuthFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(ClientTokenAuthFilter.class);
	@Value("${jdd.springcloud.serivce.clientId}")
	private String clientId;
	@Value("${jdd.springcloud.serivce.clientSecret}")
	private String clientSecret;
	@Override
	public void init(FilterConfig filterConfig) {
		logger.info("ClientTokenAuthFilter过滤器初始化");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("ClientTokenAuthFilter过滤器销毁了");
	}

}
