package com.jdd.partition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FeignConfig {
	@Bean
	public MapFormHttpMessageConverter mapFormHttpMessageConverter(MultipartFileHttpMessageConverter multipartFileHttpMessageConverter) {
	   MapFormHttpMessageConverter mapFormHttpMessageConverter = new MapFormHttpMessageConverter();
	   mapFormHttpMessageConverter.addPartConverter(multipartFileHttpMessageConverter);
	   return mapFormHttpMessageConverter;
	}

	@Bean
	public MultipartFileHttpMessageConverter multipartFileHttpMessageConverter() {
	   return new MultipartFileHttpMessageConverter();
	}

}
