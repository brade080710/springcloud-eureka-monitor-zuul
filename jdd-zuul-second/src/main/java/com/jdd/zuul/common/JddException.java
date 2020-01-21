package com.jdd.zuul.common;


    /**
     * @ClassName: JddException
     * @Description: TODO(自定义异常处理类)
     * @author 刘军伟
     * @date 2019年4月26日
     *
     */
    
public class JddException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final int code;
    private final String message;
	public JddException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
}
