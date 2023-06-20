package com.yudachi.yiki.common.response;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName GatewayErrorResponse
 * @Description 网关异常返回对象
 * @Author Yudachi
 * @Date 2023/6/6 17:00
 * @Version 1.0
 */
@Data
@ToString
public class GatewayErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
