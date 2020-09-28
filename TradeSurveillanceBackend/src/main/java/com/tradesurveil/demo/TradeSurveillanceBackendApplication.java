package com.tradesurveil.demo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tradesurveil.bean.TradeForDataGen;
import com.tradesurveil.businesslogic.DetectFrontRunning;

@SpringBootApplication
public class TradeSurveillanceBackendApplication {
	
	private static TradeForDataGen initializeData(String type,String t,String securityName,String securityType, int quantity, double price, String traderName, String brokerName) {
		TradeForDataGen tempTrade = new TradeForDataGen();
		Timestamp timestamp = Timestamp.valueOf(t);
		tempTrade.setType(type);
		tempTrade.setTimestamp(timestamp);
		tempTrade.setQuantity(quantity);
		tempTrade.setBrokerName(brokerName);
		tempTrade.setPrice(price);
		tempTrade.setSecurityName(securityName);
		tempTrade.setTraderName(traderName);
		tempTrade.setSecurityType(securityType);
		return tempTrade;
	}

	public static void main(String[] args) {
//		SpringApplication.run(TradeSurveillanceBackendApplication.class, args);
		
		List<TradeForDataGen> tradeList = new ArrayList<TradeForDataGen>();
		HashMap<List<TradeForDataGen>, String> results = new HashMap<>();
		
		TradeForDataGen firmOrderPast = initializeData("buy","2020-10-05 10:14:12","Facebook", "ES", 150, 854, "Citi Group", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("buy","2020-10-05 10:14:23","Facebook", "Futures",15000, 854.5, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture1 = initializeData("sell","2020-10-05 10:15:02","Facebook", "ES",120, 862.33, "Citi Group", "Citi" );
		tradeList.add(firmOrderFuture1);
		TradeForDataGen firmOrderFuture2 = initializeData("sell","2020-10-05 10:15:05","Facebook", "ES",30, 861, "Citi Group", "Citi" );
		tradeList.add(firmOrderFuture2);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		results = tester.detectFrontRunning(tradeList);
		
		for(List<TradeForDataGen> tradeSet: results.keySet()) {
			System.out.println(results.get(tradeSet));
			for(TradeForDataGen trade: tradeSet)
				System.out.println(trade.getType() + "\t" + trade.getTimestamp() + "\t" + trade.getSecurityName() + "\t" + trade.getSecurityType() + "\t" + trade.getBrokerName() + "\t" + trade.getTraderName() + "\t" + trade.getPrice() + "\t" + trade.getQuantity());
		}
		
	}

}
