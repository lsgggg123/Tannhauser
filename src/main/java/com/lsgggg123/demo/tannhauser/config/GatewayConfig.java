/**
 * Alipay.com Inc. Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lsgggg123.demo.tannhauser.config;

import com.lsgggg123.demo.tannhauser.filter.MyGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ls
 * @version : GatewayConfig.java, v 0.1 2021年02月08日 7:44 下午 ls Exp $
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("baidu_route", predicateSpec -> predicateSpec.path("/api").filters(gatewayFilterSpec -> gatewayFilterSpec.filter(new MyGatewayFilter())).uri("https://baidu.com"))
                .build();
    }
}