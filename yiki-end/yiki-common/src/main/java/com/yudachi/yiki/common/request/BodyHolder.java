package com.yudachi.yiki.common.request;

import lombok.Data;

/**
 * @Author yudachi
 * @Description post请求体参数及请求的Content-Length
 **/
@Data
public class BodyHolder {
    String bodyString;
    int lenth;
}
