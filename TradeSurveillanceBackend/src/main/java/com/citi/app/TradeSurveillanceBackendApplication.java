package com.citi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.citi.controller.TradeController;

@EnableAutoConfiguration
@Configuration
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackageClasses = TradeController.class)
public class TradeSurveillanceBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TradeSurveillanceBackendApplication.class, args);
	}
}