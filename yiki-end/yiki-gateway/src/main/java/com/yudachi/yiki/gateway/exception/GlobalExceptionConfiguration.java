package com.yudachi.yiki.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GlobalExceptionConfiguration
 * @Description 全局异常处理类
 * @Author Yudachi
 */
@Configuration
@Slf4j
public class GlobalExceptionConfiguration implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {

        log.error(throwable.toString() + "：" + Arrays.toString(throwable.getStackTrace()));

        Map<String, String> res = new HashMap<>();

        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()){
            return Mono.error(throwable);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        int errorCode;
        String errorMsg;

        if (throwable instanceof CustomizeInfoException){
            errorCode = ((CustomizeInfoException) throwable).getCode();
            errorMsg = throwable.getMessage();
        }else{
            errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            errorMsg = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }

        return response.writeWith(Mono.fromSupplier(()->{
            DataBufferFactory bufferFactory = response.bufferFactory();


            res.put("msg", errorMsg);
            res.put("code", String.valueOf(errorCode));

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(res));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }));
    }
}
