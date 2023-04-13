package com.lsgggg123.demo.tannhauser.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MyGlobalFilter implements GlobalFilter {
    
    static {
        Map<String, Object> tmp = new HashMap<>();
        tmp.put("code", 200);
        tmp.put("msg", "success");
        RET_MAP = Collections.unmodifiableMap(tmp);
    }
    
    static final Map<String, Object> RET_MAP;
    
    private static final HttpMessageEncoder<Object> HTTP_MESSAGE_ENCODER = new Jackson2JsonEncoder();
    
    static WebClient WEB_CLIENT = WebClient.create().mutate().build();
    
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        // 网关的原生支持的 uri 转发
        String path = request.getPath().toString();
        if (path.startsWith("/163") || path.startsWith("/b")) {
            return chain.filter(exchange);
        }
        
        // 网关做代理, 直接转发响应第三方的内容
        if (path.startsWith("/proxy")) {
            return WEB_CLIENT.get().uri("http://127.0.0.1:8000/index").retrieve().bodyToMono(String.class).map(responseBody -> {
                log.info("map: {}", responseBody);
                return responseBody;
            }).doOnError(Throwable::printStackTrace).onErrorResume(t -> Mono.just("error")).flatMap(responseBody -> {
                response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
                DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes());
                return response.writeWith(Flux.just(buffer));
            });
        }
        
        // 兜底的 json
        return response.writeWith(HTTP_MESSAGE_ENCODER.encode(Mono.just(RET_MAP), response.bufferFactory(),
                    ResolvableType.forClass(Map.class), MediaType.APPLICATION_JSON,
                    Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())));
    }
}