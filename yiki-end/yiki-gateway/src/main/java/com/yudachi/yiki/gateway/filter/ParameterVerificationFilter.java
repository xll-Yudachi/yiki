package com.yudachi.yiki.gateway.filter;

import com.yudachi.yiki.common.Constants.MethodType;
import com.yudachi.yiki.common.code.ResponseCode;
import com.yudachi.yiki.common.exception.CustomizeInfoException;
import com.yudachi.yiki.common.request.BodyHolder;
import com.yudachi.yiki.gateway.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ParameterVerificationFilter的作用是为了校验参数，保证请求唯一性
 */
@Slf4j
@Component
public class ParameterVerificationFilter implements Ordered, GlobalFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        PathContainer pathContainer = request.getPath().pathWithinApplication();
        String methodType = request.getMethodValue();
        BodyHolder bodyHolder = new BodyHolder();


        if (MethodType.GET.equals(methodType)){
            bodyHolder.setBodyString(request.getURI().getQuery());
        }else if (MethodType.POST.equals(methodType)){
            Flux<DataBuffer> body = request.getBody();
            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes); // 不调用read方法，body会乱码
                try {
                    bodyHolder.setBodyString(new String(bytes, "utf-8"));
                }catch (Exception e){
                }
            });
        }

        if(StringUtils.isBlank(bodyHolder.getBodyString()) || !UrlUtils.verifyParams(bodyHolder.getBodyString())){
            return Mono.error(new CustomizeInfoException(ResponseCode.NOTONLYONEREQUEST));
        }

        ServerHttpRequest.Builder mutate = request.mutate();
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        // 在修改参数的过滤器（order为0）之前。
        return -1;
    }

}
