package com.yudachi.yiki.oauth2.controller;

import com.yudachi.yiki.common.response.Oauth2TokenDto;
import com.yudachi.yiki.common.response.YikiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName AuthController
 * @Description 重写 oauth2 相关接口
 * @Author Yudachi
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostMapping("/token")
    public YikiResponse postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

        Oauth2TokenDto token = new Oauth2TokenDto();
        token.setToken(oAuth2AccessToken.getValue());
        token.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
        token.setExpires(oAuth2AccessToken.getExpiresIn());
        token.setHeader("Bearer ");

        return YikiResponse.createSuccessData(token);

    }
}
