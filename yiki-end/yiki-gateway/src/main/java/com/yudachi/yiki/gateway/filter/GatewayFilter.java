package com.yudachi.yiki.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.yudachi.yiki.common.Constants.RouteContants;
import com.yudachi.yiki.common.code.ResponseCode;
import com.yudachi.yiki.common.response.YikiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关拦截器
 */
@Component
public class GatewayFilter implements GlobalFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
 
    /**
     * 全局拦截器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse resp = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        PathContainer pathContainer = request.getPath().pathWithinApplication();

        //获取请求的url
        String path = request.getURI().getPath();

        //静态资源访问,接口通过判断url中是否含有指定的字符来判断是否放行,在这里设置url的白名单
        if (isSkip(pathContainer.toString())) {
            ServerHttpRequest.Builder mutate = request.mutate();
            return chain.filter(exchange.mutate().request(mutate.build()).build());
        }

        String token = request.getHeaders().getFirst("Authorization");
        if (ObjectUtils.isEmpty(token)) {
            return unAuth(resp, "缺失令牌,鉴权失败");
        }
        String realToken = token.replace("bearer ", "");

        String key = "access:" + realToken;
        String value = redisTemplate.opsForValue().get(key);

        if(ObjectUtils.isEmpty(realToken)){
            return unAuth(resp,"请求未授权");
        }

        if(ObjectUtils.isEmpty(value)){
            return unAuth(resp,"令牌已失效");
        }
        ServerHttpRequest.Builder mutate = request.mutate();
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private boolean isSkip(String path) {
        return RouteContants.getDefaultWhiteRoute().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }


    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String result = "";
        result = JSON.toJSONString(YikiResponse.createResponse(ResponseCode.AUTHORIZATIONERROR.getCode(), msg));
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}