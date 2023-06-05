package com.yudachi.yiki.gateway.filter;

import com.yudachi.yiki.common.utils.UrlUtils;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * ModifyBodyGlobalFilter的作用是为了自定义添加系统参数，修改请求
 */
@Slf4j
@Component
public class ModifyBodyGlobalFilter implements Ordered, GlobalFilter {

    private final DataBufferFactory dataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        PathContainer pathContainer = request.getPath().pathWithinApplication();
        String path = pathContainer.toString();
        if (UrlUtils.isAuth(path)) {
            if ("/oauth2Api/oauth/token".equals(path)) {
                BodyHolder holder = new BodyHolder();
                // 新建一个ServerHttpRequest装饰器,覆盖需要装饰的方法
                ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        Flux<DataBuffer> body = super.getBody();
                        body.subscribe(buffer -> {

                            byte[] bytes = new byte[buffer.readableByteCount()];
                            buffer.read(bytes); // 不调用read方法，body会乱码
                            try {
                                holder.bodyString = new String(bytes, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            log.info("请求参数Body：" + holder.bodyString);

                        });
                        if (StringUtils.isNotBlank(holder.bodyString)) {
                            try {

                                holder.bodyString += "&grant_type=password&client_id=client_id&client_secret=client_secret";

                                holder.lenth = holder.bodyString.getBytes().length;

                                DataBuffer dataBuffer = dataBufferFactory.allocateBuffer();
                                dataBuffer.write(holder.bodyString.getBytes(StandardCharsets.UTF_8));

                                return Flux.just(dataBuffer);
                            } catch (Exception e) {
                                throw new IllegalStateException(e);
                            }
                        } else {
                            return super.getBody();
                        }
                    }

                    // post请求需要修改请求的 Content-Length， 否则请求会报错
                    @Override
                    public HttpHeaders getHeaders() {

                        // 不能直接使用 HttpHeaders，这个请求头是只读的，不允许修改

                        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(HttpHeaders.CONTENT_LENGTH, Integer.toString(holder.lenth)).build();

                        return newRequest.getHeaders();
                    }
                };
                // 使用修改后的ServerHttpRequestDecorator重新生成一个新的ServerWebExchange
                return chain.filter(exchange.mutate().request(decorator).build());
            }
        }

        ServerHttpRequest.Builder mutate = request.mutate();
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        // 修改参数的过滤器必须放在StripPrefix（order为1）过滤器之前。否则会出现PooledUnsafeDirectByteBuf报错
        return 0;
    }

    private class BodyHolder {
        String bodyString;
        int lenth;
    }
}
