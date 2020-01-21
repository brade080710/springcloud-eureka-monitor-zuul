package com.jdd.partition.common;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result<String> genSuccessResult() {
        return new Result<String>()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }
    public static <T> Result<T> genSuccessResult(T data,String msg) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMsg(msg)
                .setData(data);
    }
    public static Result<String> genFailResult(String msg) {
        return new Result<String>()
                .setCode(ResultCode.FAIL)
                .setMsg(msg);
    }
}
