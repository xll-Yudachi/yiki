package com.yudachi.yiki.common.response;

import com.yudachi.yiki.common.code.ResponseCode;

import java.io.Serializable;
 
//用于封装数据的类，加泛型实现序列化接口
public class YikiResponse<T> implements Serializable {
    private int code;
    private String msg;
    private T data;
    //编写不同的构造方法，更改为private，禁止外部创建该类的对象
    private YikiResponse(int code) {
        this.code = code;
    }
 
    private YikiResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
 
    private YikiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }
 
    private YikiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //提供get方法，后续要返回json数据，没有get方法无法返回数据
    public int getcode() {
        return code;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public T getData() {
        return data;
    }
    //在内部提供静态方法供外部访问
    public static <T> YikiResponse<T> createSuccess() {
        return new YikiResponse<>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> YikiResponse<T> createSuccessMsg(String msg) {
        return new YikiResponse<>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> YikiResponse<T> createSuccessData(T data) {
        return new YikiResponse<>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> YikiResponse<T> createSuccessMsgData(String msg, T data) {
        return new YikiResponse<>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> YikiResponse<T> createErrorMsg(String errorMsg) {
        return new YikiResponse<>(ResponseCode.ERROR.getCode(),errorMsg);
    }

    public static <T> YikiResponse<T> createResponse(int code, String msg){
        return new YikiResponse<>(code, msg);
    }

    public static <T> YikiResponse<T> createResponse(int code, String msg, T data){
        return new YikiResponse<>(code, msg, data);
    }
 
}