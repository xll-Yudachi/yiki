package com.yudachi.yiki.common.response;

import lombok.Data;

/**
 * @ClassName Oauth2TokenDto
 * @Description oauth2 token 返回对象
 * @Author Yudachi
 */
@Data
public class Oauth2TokenDto {

    private String token;
    private String refreshToken;
    private int expires;
    private String header;

}
