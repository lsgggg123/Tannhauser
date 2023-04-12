package com.lsgggg123.demo.tannhauser.config;

import com.lsgggg123.demo.tannhauser.filter.MyGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("route2", p -> p.path("/b/**")
                        .filters(fs -> fs.filter(new MyGatewayFilter()).filters())
                        .uri("https://baidu.com"))
                .build();
    }
}