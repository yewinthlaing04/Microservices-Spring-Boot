package com.ye.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    @Bean
    public RouteLocator neoBankRouteConfig(RouteLocatorBuilder builder){

        return builder.routes()
                .route( p -> p.path("/neobank/accounts/**")
                        .filters( f -> f.rewritePath("/neobank/accounts/(?<segment>.*)",
                                "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))   // filter
                        .uri("lb://ACCOUNTS"))
                .route( p -> p.path( "/neobank/loan/**")
                        .filters( f -> f.rewritePath("/neobank/loans/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://LOANS"))
                .route( p -> p.path("/neobank/card/**")
                        .filters( f -> f.rewritePath("/neobank/cards/(?<segment>.*)" , "/${segment}")
                                .addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
                        .uri("lb://CARDS")).build();
    }
}
