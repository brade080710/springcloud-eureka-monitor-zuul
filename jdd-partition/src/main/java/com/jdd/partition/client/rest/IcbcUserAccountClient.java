package com.jdd.partition.client.rest;

import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultCode;
import com.jdd.partition.common.ResultGenerator;
import com.jdd.partition.entity.IcbcUserAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 使用feignClient调用微服务
 */
@FeignClient(name = "ICBC-SERVER-APP")
public interface IcbcUserAccountClient {
	
    @PostMapping(value ="/client/token/icbcUserAccount/getIcbcAccount",
    		consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Result getIcbcAccount(IcbcUserAccount icbcUserAccount);
    
    @Component
    class IcbcUserAccountClientFallback implements IcbcUserAccountClient {

		@Override
		public Result getIcbcAccount(IcbcUserAccount icbcUserAccount) {
			Result result = ResultGenerator.genFailResult("网络不通");
			result.setCode(ResultCode.FORBIDDEN);
			return result;
		}

	}
}
