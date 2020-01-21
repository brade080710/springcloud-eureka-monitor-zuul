package com.jdd.partition.util;


import com.jdd.partition.common.JddException;
import com.jdd.partition.common.RedisKeys;
import com.jdd.partition.common.ResultCode;
import com.jdd.partition.common.TokenUser;

import javax.servlet.http.HttpServletRequest;

public class TokenUserUtils {

	public static TokenUser getUserFromRequest(HttpServletRequest req) {
		if (req.getAttribute(RedisKeys.TOKEN_ATTR)==null) {
			throw new JddException(ResultCode.EXPIRED_TOKEN, "token不存在");
		}
		TokenUser tokenUser = (TokenUser)req.getAttribute(RedisKeys.TOKEN_ATTR);
		return tokenUser;
	}
}
