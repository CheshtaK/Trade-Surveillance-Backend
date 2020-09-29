package com.tradesurveil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.tradesurveil.controller.TradeController;

@SpringBootApplication
@ComponentScan(basePackageClasses = TradeController.class)
public class TradeSurveillanceBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TradeSurveillanceBackendApplication.class, args);
	}
}