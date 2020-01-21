package com.jdd.zuul.common;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {
	private static final Logger logger = LoggerFactory.getLogger(ParameterRequestWrapper.class);
	private Map<String, String[]> params = new HashMap<String, String[]>();
	private byte[] data = null;

	HttpServletRequest orgRequest;

	public ParameterRequestWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
		this.params.putAll(request.getParameterMap());
		try {
			data = StreamUtils.copyToByteArray(request.getInputStream());
			Map requetJsonMap = JSON.parseObject(new String(data, "UTF-8"));
			if (requetJsonMap != null)
				this.params.putAll(requetJsonMap);

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public JSON getRequestBody() throws UnsupportedEncodingException {
		return JSON.parseObject((new String(data, "UTF-8")));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return !isReady();
			}

			@Override
			public boolean isReady() {
				return inputStream.available() > 0;
			}

			@Override
			public void setReadListener(ReadListener listener) {

			}

			@Override
			public int read() throws IOException {
				return inputStream.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return super.getReader();
	}

	@Override
	public String getParameter(String name) {
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			String[] strArr = (String[]) v;
			if (strArr.length > 0) {
				return strArr[0];
			} else {
				return null;
			}
		} else if (v instanceof String) {
			return (String) v;
		} else {
			return v.toString();
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return params;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Vector names = new Vector(params.keySet());
		return names.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			return (String[]) v;
		} else if (v instanceof String) {
			return new String[] { (String) v };
		} else {
			return new String[] { v.toString() };
		}
	}

	public void addAllParameters(Map<String, Object> otherParams) {// 增加多个参数
		for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
			addParameter(entry.getKey(), entry.getValue());
		}
	}

	public void addParameter(String name, Object value) {// 增加参数
		if (value != null) {
			if (value instanceof String[]) {
				params.put(name, (String[]) value);
			} else if (value instanceof String) {
				params.put(name, new String[] { (String) value });
			} else {
				params.put(name, new String[] { String.valueOf(value) });
			}
		}
	}

	/** 简单封装，请根据需求改进 */
	public void addObject(Object obj) {
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getMethods();
		try {
			for (Method method : methods) {
				if (!method.getName().startsWith("get")) {
					continue;
				}
				Object invoke = method.invoke(obj);
				if (invoke == null || "".equals(invoke)) {
					continue;
				}

				String filedName = method.getName().replace("get", "");
				filedName = WordUtils.uncapitalize(filedName);

				if (invoke instanceof Collection) {
					Collection collections = (Collection) invoke;
					if (collections != null && collections.size() > 0) {
						String[] strings = (String[]) collections.toArray();
						addParameter(filedName, strings);
						return;
					}
				}

				addParameter(filedName, invoke);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

}
