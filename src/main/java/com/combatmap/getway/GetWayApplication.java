package com.combatmap.getway;


import com.combatmap.getway.route.MysqlRouteDefinitionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//表明自己是客户端
@EnableEurekaClient
@EnableDiscoveryClient
public class GetWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetWayApplication.class, args);
    }

    @Bean
    public RouteDefinitionWriter routeDefinitionWriter() {
        return new InMemoryRouteDefinitionRepository();
    }

    @Bean
    public MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository() {
        return new MysqlRouteDefinitionRepository();
    }

}