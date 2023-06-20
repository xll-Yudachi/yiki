package com.yudachi.yiki.common.request;

import lombok.Data;

/**
 * @ClassName OauthTokenRequest
 * @Description Oauth2请求对象
 * @Author Yudachi
 * @Date 2023/6/2 15:13
 * @Version 1.0
 */
@Data
public class Oauth2TokenRequest {
    private String userName;
    private String password;
    private String grant_type;
    private String client_id;
    private String client_secret;
}
