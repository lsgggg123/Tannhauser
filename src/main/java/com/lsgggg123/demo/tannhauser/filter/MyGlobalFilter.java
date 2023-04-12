package com.lsgggg123.demo.tannhauser.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
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
    
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        String path = request.getPath().toString();
        if (path.startsWith("/163") || path.startsWith("/b")) {
            return chain.filter(exchange);
        } else {
           return response.writeWith(HTTP_MESSAGE_ENCODER.encode(Mono.just(RET_MAP),
                    response.bufferFactory(),
                    ResolvableType.forClass(Map.class),
                    MediaType.APPLICATION_JSON,
                    Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())));
        }
    }
}