package com.yudachi.yiki.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 配置授权服务器
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService yikiUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    // Redis 缓存数据库
    public static final int REDIS_CONNECTION_DATABASE = 1;
    /**
     * 用来配置授权服务器可以为哪些客户端授权,使用哪种授权模式
     *
     * 密码模式：在密码模式中，用户向客户端提供用户名和密码，客户端通过用户名和密码到认证服务器获取令牌
     * （A）用户访问客户端，提供URI连接包含用户名和密码信息给授权服务器
     * （B）授权服务器对客户端进行身份验证
     * （C）授权通过，返回access_token给客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // 客户端id
                .withClient("client_id")
                // 客户端秘钥
                .secret(passwordEncoder.encode("client_secret"))
                // 授权模式
                .authorizedGrantTypes("password", "refresh_token", "client_credentials")
                // access_token的过期时长
                .accessTokenValiditySeconds(43200)
                // refresh_token的过期时长
                .refreshTokenValiditySeconds(86400)
                // 允许授权的范围
                .scopes("all");
    }

    /**
     * 刷新令牌必须配置userDetailsService，用来刷新令牌时的认证
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 配置token的存储方式
        endpoints.tokenStore(tokenStore);
        // endpoints.accessTokenConverter(accessTokenConverter());
        // 配置认证管理器
        endpoints.authenticationManager(authenticationManager);
        // 配置认证数据源，刷新令牌时的认证
        endpoints.userDetailsService(yikiUserDetailsService);
    }

    /**
     * 开启token检查接口
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security.allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("signKey");
        return converter;
    }
}
