package com.jdd.zuul.common;


import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final int SUCCESS_CODE = 200;
    private static final int FAIL_CODE = 400;
    public Result<T> setCode(final int resultCode) {
        this.code = resultCode;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static Result<String> genSuccessResult() {
        return new Result<String>()
                .setCode(SUCCESS_CODE)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>()
                .setCode(SUCCESS_CODE)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result<String> genFailResult(String msg,int code) {
        return new Result<String>()
                .setCode(code)
                .setMsg(msg);
    }
}