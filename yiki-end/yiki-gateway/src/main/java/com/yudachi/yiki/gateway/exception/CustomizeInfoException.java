package com.yudachi.yiki.gateway.exception;

import com.yudachi.yiki.common.code.ResponseCode;
import lombok.Data;

/**
 * @ClassName CustomizeInfoException
 * @Description 自定义全局异常对象
 * @Author Yudachi
 */
@Data
public class CustomizeInfoException extends Exception{
    private int code;
    private String message;

    public CustomizeInfoException(int code, String message){
        this.code = code;
        this.message = message;
    }

    public CustomizeInfoException(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.message = responseCode.getDesc();
    }
}
