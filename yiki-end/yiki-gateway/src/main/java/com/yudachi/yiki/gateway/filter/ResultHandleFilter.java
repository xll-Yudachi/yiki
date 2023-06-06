package com.yudachi.yiki.gateway.filter;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 请求结果处理拦截器 - 设置最终返回的结果格式
 */
@Component
@Slf4j
public class ResultHandleFilter implements GlobalFilter, Ordered {

    private static Joiner joiner = Joiner.on("");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        DataBufferFactory bufferFactory = response.bufferFactory();


        // response 装饰器
        ServerHttpResponseDecorator serverHttpResponseDecorator = new ServerHttpResponseDecorator(response){
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                if (getStatusCode().is2xxSuccessful() && body instanceof Flux){
                    String contentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (StringUtils.isNotBlank(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)){

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        //（返回数据内如果字符串过大，默认会切割）解决返回体分段传输
                        return super.writeWith(fluxBody.buffer().map(dataBuffers->{
                            List<String> list = Lists.newArrayList();
                            dataBuffers.forEach(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                list.add(new String(content, StandardCharsets.UTF_8));
                            });
                            String responseData = joiner.join(list);
                            System.out.println("responseData："+responseData);

                            // 结果处理


                            byte[] updateContent = new String(responseData.getBytes(), StandardCharsets.UTF_8).getBytes();
                            response.getHeaders().setContentLength(updateContent.length);
                            return bufferFactory.wrap(updateContent);



                        }));



                    }
                }


                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build());
    }

    @Override
    public int getOrder() {
        // NettyWriteResponseFilter的order是-1，修改response的filter必须在这个之前
        return -2;
    }
}