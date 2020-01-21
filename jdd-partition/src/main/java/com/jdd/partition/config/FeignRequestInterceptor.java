package com.jdd.partition.config;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
class FeignRequestInterceptor implements RequestInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);
	@Value("${jdd.springcloud.serivce.clientId}")
	private String clientId;
	@Value("${jdd.springcloud.serivce.clientSecret}")
	private String clientSecret;
	@Override
	public void apply(RequestTemplate template) {
		Encoder encoder= Base64.getEncoder();
		
		String headerValue = null;
		try {
			headerValue = "JddBasic " + encoder.encodeToString((clientId + ":" + clientSecret).getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		template.header("Authorization", headerValue);
	}
	
}