package com.lsgggg123.demo.tannhauser.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// @Configuration
public class GlobalFilterConfig {
    @Bean
    @Order(-1)
    public GlobalFilter a1() {
        return new A1Filter();
    }

    @Bean
    @Order(0)
    public GlobalFilter b2() {
        return new B2Filter();
    }

    @Bean
    @Order(1)
    public GlobalFilter c3() {
        return new C3Filter();
    }

    @Slf4j
    public static class A1Filter implements GlobalFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.info("A1Filter 前置逻辑");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> log.info("A1Filter 后置逻辑")));
        }

        @Override
        public int getOrder() {
            return HIGHEST_PRECEDENCE + 100;
        }
    }

    @Slf4j
    public static class B2Filter implements GlobalFilter, Ordered {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.info("B2Filter 前置逻辑");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> log.info("B2Filter 后置逻辑")));
        }

        @Override
        public int getOrder() {
            return HIGHEST_PRECEDENCE + 200;
        }
    }

    @Slf4j
    public static class C3Filter implements GlobalFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.info("C3Filter 前置逻辑");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> log.info("C3Filter 后置逻辑")));
        }

        @Override
        public int getOrder() {
            return HIGHEST_PRECEDENCE + 300;
        }
    }
}