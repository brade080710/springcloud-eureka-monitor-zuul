package com.jdd.partition.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jdd.partition.common.Result;

public class ResponseUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
	
	public static  <T> void  returnJson(HttpServletResponse response, Result<T> result) {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			writer = response.getWriter();
			writer.print(JSON.toJSONString(result));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			if (writer != null)
				writer.close();
		}
	}

}
