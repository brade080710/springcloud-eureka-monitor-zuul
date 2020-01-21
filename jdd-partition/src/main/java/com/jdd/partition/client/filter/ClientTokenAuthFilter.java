package com.jdd.partition.client.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultCode;
import com.jdd.partition.util.ResponseUtil;


/**
 * @author 刘军伟
 * function：过滤器
 * 加密解密:敏感信息，具体到接口里加解密
 * 是否携带token：
 *
 */
@WebFilter(urlPatterns = "/client/token/*", filterName = "clientTokenAuthFilter")
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
		HttpServletResponse resp = (HttpServletResponse)response ;
		HttpServletRequest req = (HttpServletRequest)request;
		String authorization = req.getHeader("Authorization");
		if (StringUtils.isEmpty(authorization)) {
			ResponseUtil.returnJson(resp,Result.genFailResult("参数非法", ResultCode.PARAMETER_ERROR));
			return;
		}
		String decodeStr = StringUtils.substringAfterLast(authorization, "JddBasic ");
		logger.info("decodeStr={}",decodeStr);
		final Decoder decoder = Base64.getDecoder();
		String authorStr = new String(decoder.decode(decodeStr.getBytes("UTF-8")),"UTF-8");
		logger.info("authorStr={}",authorStr);
		if (StringUtils.isEmpty(authorStr)) {
			ResponseUtil.returnJson(resp,Result.genFailResult("参数非法", ResultCode.PARAMETER_ERROR));
			return;
		}
		if (!authorStr.equals(clientId+":"+clientSecret)) {
			ResponseUtil.returnJson(resp,Result.genFailResult("参数非法", ResultCode.PARAMETER_ERROR));
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("ClientTokenAuthFilter过滤器销毁了");
	}

}
