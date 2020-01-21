package com.jdd.partition.common;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public final class ResultCode {
	public static final int SUCCESS = 200;//成功
	public static final int FAIL = 400;//失败
	public static final int UNAUTHORIZED = 401;//未认证（签名错误）
	public static final int EXPIRED_TOKEN = 402;//token失效
	public static final int FORBIDDEN = 403;//禁止访问
	public static final int NOT_FOUND = 404;//接口不存在
	public static final int DUPLICATE_LOGIN = 405;//重复登录
	public static final int INVALID_CLIENT = 406;//无效客户端
	public static final int PARAMETER_ERROR = 407;//参数错误
	public static final int REQUEST_TIMEOUT = 408;//请求超时
	public static final int SIGN_ERROR = 409;//签名错误
	public static final int API_NOT_FOUND = 410;//api接口不存在
	public static final int VERSION_ERROR = 411;//版本错误
	public static final int SERIAL_ERROR = 412;//序列化异常
	public static final int INTERNAL_SERVER_ERROR = 500;//服务器内部错误
}
