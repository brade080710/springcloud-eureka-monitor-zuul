package com.jdd.partition.app.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jdd.partition.common.JddException;
import com.jdd.partition.common.RedisKeys;
import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultCode;
import com.jdd.partition.common.TokenUser;
import com.jdd.partition.util.DateUtils;
import com.jdd.partition.util.ParameterRequestWrapper;
import com.jdd.partition.util.RSASignature;
import com.jdd.partition.util.ResponseUtil;

/**
 * @author 刘军伟 function：过滤器 加密解密:敏感信息，具体到接口里加解密 是否携带token：
 *
 */
@Component
public class AppSignFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AppSignFilter.class);
    @Value("${jdd.ras.publicKey}")
	private String publicKey;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("过滤器初始化");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(req);
		String timestamp = parameterRequestWrapper.getParameter("timestamp");
		if (StringUtils.isEmpty(timestamp)) {
			ResponseUtil.returnJson(resp, Result.genFailResult("参数错误", ResultCode.PARAMETER_ERROR));
			return;
		}
		Date date = null;
		try {
			date = DateUtils.stringToDate(timestamp, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			ResponseUtil.returnJson(resp, Result.genFailResult("参数错误", ResultCode.PARAMETER_ERROR));
		}
		Long time = new Date().getTime() - date.getTime();
		if (time > 3000000) {
			ResponseUtil.returnJson(resp, Result.genFailResult("请求超时", ResultCode.REQUEST_TIMEOUT));
			return;
		}
		String version = parameterRequestWrapper.getParameter("version");
		if (StringUtils.isEmpty(version)) {
			ResponseUtil.returnJson(resp, Result.genFailResult("版本号错误", ResultCode.VERSION_ERROR));
			return;
		}
		if (!version.equals("3.0.0")) {
			ResponseUtil.returnJson(resp, Result.genFailResult("版本号错误", ResultCode.VERSION_ERROR));
			return;
		}
		// 签名校验
		String sign = parameterRequestWrapper.getParameter("sign");
		if (StringUtils.isEmpty(sign)) {
			ResponseUtil.returnJson(resp, Result.genFailResult("签名不存在", ResultCode.SIGN_ERROR));
			return;
		}
		sign = URLDecoder.decode(parameterRequestWrapper.getParameter("sign"), "UTF-8").replace(' ', '+');
		String reqStr = buildOrderedSignStr(parameterRequestWrapper);
		try {
			if (!RSASignature.doCheck(reqStr, sign, publicKey)) {
				ResponseUtil.returnJson(resp, Result.genFailResult("签名不正确", ResultCode.SIGN_ERROR));
				return;
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException
				| IllegalArgumentException e) {
			logger.error(e.getMessage());
			ResponseUtil.returnJson(resp, Result.genFailResult("签名不正确", ResultCode.SIGN_ERROR));
			return;
		}
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

		request.setAttribute(RedisKeys.TOKEN_ATTR, tokenUser);
		chain.doFilter(parameterRequestWrapper, response);
	}

	@Override
	public void destroy() {
		logger.info("过滤器销毁了");
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	private String buildOrderedSignStr(HttpServletRequest request) {
		Map<String, String> sortedMap = new TreeMap<String, String>();
		boolean hasParam = false;
		StringBuilder sb = new StringBuilder(getRequestPath(request));
		sb.append("?");
		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			if ("sign".equals(paraName))
				continue;
			if (StringUtils.isEmpty(request.getParameter(paraName))) {
				sortedMap.put(paraName, "");
			} else {
				sortedMap.put(paraName, request.getParameter(paraName));
			}
		}
		Set<Entry<String, String>> entries = sortedMap.entrySet();
		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (!StringUtils.isEmpty(name)) {
				if (hasParam) {
					sb.append("&");
				} else {
					hasParam = true;
				}
				sb.append(name).append("=").append(value);
			}

		}
		return sb.toString();
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();
		if (request.getPathInfo() != null) {
			url = url + request.getPathInfo();
		}
		return url;
	}

	public static void main(String[] args) {
	}
}
