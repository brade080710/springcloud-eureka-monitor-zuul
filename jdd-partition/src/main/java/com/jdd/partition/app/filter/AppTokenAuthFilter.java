package com.jdd.partition.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jdd.partition.common.JddException;
import com.jdd.partition.common.RedisKeys;
import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultCode;
import com.jdd.partition.common.TokenUser;
import com.jdd.partition.util.ParameterRequestWrapper;
import com.jdd.partition.util.ResponseUtil;
/**
 * @author 刘军伟 function：过滤器 加密解密:敏感信息，具体到接口里加解密 是否携带token：
 *
 */
@Component
public class AppTokenAuthFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AppTokenAuthFilter.class);
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void init(FilterConfig filterConfig) {
		logger.info("过滤器初始化");
	}

	/*
	 * public AppTokenAuthFilter(StringRedisTemplate stringRedisTemplate) { super();
	 * this.stringRedisTemplate = stringRedisTemplate; }
	 */

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(req);
		String accessToken = parameterRequestWrapper.getParameter("accessToken");
		// 获取用户凭证
		if (org.apache.commons.lang.StringUtils.isBlank(accessToken)) {
			ResponseUtil.returnJson(resp, Result.genFailResult("非法请求", ResultCode.UNAUTHORIZED));
			return;
		}

		TokenUser tokenUser = null;
		try {
			String str = stringRedisTemplate.opsForValue().get(RedisKeys.ACESS_TOKEN + accessToken);
			tokenUser = JSON.parseObject(str, TokenUser.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new JddException(ResultCode.SERIAL_ERROR, "序列化异常");
		}
		if (tokenUser == null) {
			ResponseUtil.returnJson(resp, Result.genFailResult("token过期", ResultCode.EXPIRED_TOKEN));
			return;
		}
		parameterRequestWrapper.setAttribute(RedisKeys.TOKEN_ATTR, tokenUser);
		chain.doFilter(parameterRequestWrapper, response);
	}

	@Override
	public void destroy() {
		logger.info("过滤器销毁了");
	}

}
