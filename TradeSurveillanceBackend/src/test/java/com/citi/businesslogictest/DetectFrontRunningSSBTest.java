package com.citi.businesslogictest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.citi.bean.FrontRunningScenario;
import com.citi.bean.TradeForDataGen;
import com.citi.businesslogic.DetectFrontRunning;

/**
 * JUnit test for Detection algorithm on Scenario 2 - Sell, Sell, Buy
 * @author Khyati
 *
 */

class DetectFrontRunningSSBTest {
	
	private List<TradeForDataGen> tradeList = new ArrayList<TradeForDataGen>();
	private List<FrontRunningScenario> detectedTrades = new ArrayList<>();
	
	private TradeForDataGen initializeData(String type,String t,String securityName,String securityType, int quantity, double price, String traderName, String brokerName) {
		/**
		 * Method to initialize TradeForDataGen objects
		 * to store in tradeList
		 */
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
	
	@Test
	void testFrontRunning_SSB_ES_ES_ES(){
		/**
		 * Test for <Equity shares, Equity shares, Equity shares> combination in Scenario 2
		 */
		TradeForDataGen firmOrderPast = initializeData("Sell","2020-10-05 9:05:38","Walmart", "ES",100, 10075.6, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:05:42","Walmart", "ES",5000, 10075.2, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Buy","2020-10-05 9:05:50","Walmart", "ES",100, 10069.3, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR2-SSB");
	}
	
	@Test
	void testFrontRunning_SSB_Fut_ES_Fut(){
		/**
		 * Test for <Futures, Equity shares, Futures> combination in Scenario 2
		 */
		TradeForDataGen firmOrderPast = initializeData("Sell","2020-10-05 9:34:58","Apple", "Futures", 200, 1002, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:35:12","Apple", "ES",12000, 1000.06, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Buy","2020-10-05 9:35:34","Apple", "Futures",200, 993.89, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR2-SSB");
	}
	
	@Test
	void testFrontRunning_SSB_Call_ES_Call(){
		/**
		 * Test for <Call option, Equity shares, Call option> combination in Scenario 2
		 */
		TradeForDataGen firmOrderPast = initializeData("Sell","2020-10-05 9:34:58","Apple", "Call", 200, 1352.8, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:35:12","Apple", "ES",12000, 1351.3, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Buy","2020-10-05 9:35:34","Apple", "Call",200, 1343.8, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR2-SSB");
	}
	
	@Test
	void testFrontRunning_SSB_ES_Fut_ES(){
		/**
		 * Test for <Equity shares, Futures, Equity shares> combination in Scenario 2
		 */
		TradeForDataGen firmOrderPast = initializeData("Sell","2020-10-05 10:14:12","Facebook", "ES", 150, 862.33, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 10:14:23","Facebook", "Futures",15000, 861, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture1 = initializeData("Buy","2020-10-05 10:15:02","Facebook", "ES",120, 854.2, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture1);
		TradeForDataGen firmOrderFuture2 = initializeData("Buy","2020-10-05 10:15:05","Facebook", "ES",30, 856, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture2);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR2-SSB");
	}
	
	@Test
	void testFrontRunning_SSB_Fut_Fut_Fut(){
		/**
		 * Test for <Futures, Futures, Futures> combination in Scenario 2
		 */
		TradeForDataGen firmOrderPast = initializeData("Sell","2020-10-05 9:05:38","Apple", "Futures", 400, 1202.8, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:05:42","Apple", "Futures",10000, 1201.3, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Buy","2020-10-05 9:05:50","Apple", "Futures",400, 1195.8, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR2-SSB");	
	}
	
	@Test
	void testFrontRunning_SSB_Call_Fut_Call(){
		/**
		 * Test for <Call option, Futures, Call option> combination in Scenario 2
		 */
		TradeForDataGen firmOrderPast = initializeData("Sell","2020-10-05 9:34:58","Apple", "Call", 400, 1751.8, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:35:12","Apple", "Futures",10000, 1751.3, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Buy","2020-10-05 9:35:34","Apple", "Call",400, 1746.8, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR2-SSB");
	}

}
