package com.tradesurveil.demo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tradesurveil.bean.TradeForDataGen;
import com.tradesurveil.businesslogic.DatasetGenerator;
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
		
		DatasetGenerator datasetGenerator = new DatasetGenerator();
		List<TradeForDataGen> tradeList = new ArrayList<TradeForDataGen>();
		HashMap<List<TradeForDataGen>, String> results = new HashMap<>();
		
//		tradeList = datasetGenerator.generateRandomTrades(90, 100);
		
		TradeForDataGen order1 = initializeData("sell","2020-10-05 10:14:12","Facebook", "ES", 1000, 23, "Citi Group", "Citi" );
		tradeList.add(order1);
		TradeForDataGen order2 = initializeData("sell","2020-10-05 10:14:16","Facebook", "ES", 25000, 23, "Client", "Citi" );
		tradeList.add(order2);
		TradeForDataGen order3 = initializeData("buy","2020-10-05 10:15:10","Facebook", "ES", 800, 21, "Citi Group", "Citi" );
		tradeList.add(order3);
		TradeForDataGen order4 = initializeData("buy","2020-10-05 10:16:12","Facebook", "ES", 3000, 24, "Citi Group", "Citi" );
		tradeList.add(order4);
		TradeForDataGen order5 = initializeData("buy","2020-10-05 10:16:20","Facebook", "Futures", 20000, 23, "Client", "Citi" );
		tradeList.add(order5);
		TradeForDataGen order6 = initializeData("sell","2020-10-05 10:16:25","Facebook", "Futures", 3000, 21, "Citi Group", "Citi" );
		tradeList.add(order6);
		TradeForDataGen order7 = initializeData("buy","2020-10-05 10:16:30","Facebook", "Futures", 5000, 28, "Citi Group", "Citi" );
		tradeList.add(order7);
		TradeForDataGen order8 = initializeData("sell","2020-10-05 10:16:34","Facebook", "Call", 35000, 3, "Client", "Citi" );
		tradeList.add(order8);
		TradeForDataGen order9 = initializeData("buy","2020-10-05 10:16:36","Facebook", "Call", 5000, 4, "Citi Group", "Citi" );
		tradeList.add(order9);
		TradeForDataGen order10 = initializeData("buy","2020-10-05 10:16:45","Facebook", "Put", 1000, 2, "Citi Group", "Citi" );
		tradeList.add(order10);
		TradeForDataGen order11 = initializeData("sell","2020-10-05 10:16:48","Facebook", "Futures", 30000, 30, "Client", "Citi" );
		tradeList.add(order11);
		TradeForDataGen order12 = initializeData("sell","2020-10-05 10:16:50","Facebook", "Put", 1000, 3, "Citi Group", "Citi" );
		tradeList.add(order12);
		TradeForDataGen order13 = initializeData("sell","2020-10-05 10:16:53","Facebook", "Futures", 2300, 31, "Citi Group", "Citi" );
		tradeList.add(order13);
		TradeForDataGen order14 = initializeData("sell","2020-10-05 10:16:56","Facebook", "ES", 23000, 30, "Client", "Citi" );
		tradeList.add(order14);
		TradeForDataGen order15 = initializeData("buy","2020-10-05 10:16:58","Facebook", "Futures", 2250, 33, "Citi Group", "Citi" );
		tradeList.add(order15);
		
//		TradeForDataGen clientOrder = initializeData("buy","2020-10-05 10:14:23","Facebook", "Futures",15000, 854.5, "Client", "Citi" );
//		tradeList.add(clientOrder);
//		TradeForDataGen firmOrderFuture1 = initializeData("sell","2020-10-05 10:15:02","Facebook", "ES",120, 862.33, "Citi Group", "Citi" );
//		tradeList.add(firmOrderFuture1);
//		TradeForDataGen firmOrderFuture2 = initializeData("sell","2020-10-05 10:15:05","Facebook", "ES",30, 861, "Citi Group", "Citi" );
//		tradeList.add(firmOrderFuture2);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		results = tester.detectFrontRunning(tradeList);
		
		for(List<TradeForDataGen> tradeSet: results.keySet()) {
			System.out.println(results.get(tradeSet));
			for(TradeForDataGen trade: tradeSet)
				System.out.println(trade.getType() + "\t" + trade.getTimestamp() + "\t" + trade.getSecurityName() + "\t" + trade.getSecurityType() + "\t" + trade.getBrokerName() + "\t" + trade.getTraderName() + "\t" + trade.getPrice() + "\t" + trade.getQuantity());
		}
		 
	}

}
