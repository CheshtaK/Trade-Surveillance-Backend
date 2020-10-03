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

import org.junit.jupiter.api.Test;

/**
 * JUnit test for Detection algorithm on Scenario 3 - Buy, Sell, Sell
 * @author Khyati
 *
 */

class DetectFrontRunningBSS {
	
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
	void testFrontRunning_BSS_Put_ES_Put(){
		/**
		 * Test for <Put option, Equity shares, Put option> combination in Scenario 3
		 */
		TradeForDataGen firmOrderPast = initializeData("Buy","2020-10-05 9:05:38","Facebook", "Put",130, 18444.31, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:05:42","Facebook", "ES",9500, 18444.43, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Sell","2020-10-05 9:05:50","Facebook", "Put",130, 18500.31, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR3-BSS");
	}
	
	@Test
	void testFrontRunning_BSS_Put_Fut_Put(){
		/**
		 * Test for <Put option, Put option, Put option> combination in Scenario 3
		 */
		TradeForDataGen firmOrderPast = initializeData("Buy","2020-10-05 9:05:38","Facebook", "Put",130, 18444.31, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("Sell","2020-10-05 9:05:42","Facebook", "Futures",9500, 18444.43, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("Sell","2020-10-05 9:05:50","Facebook", "Put",130, 18500.31, "Citi Global Markets", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		detectedTrades = tester.detectFrontRunning(tradeList);
		assertEquals(detectedTrades.size(), 1);
		assertEquals(detectedTrades.get(0).getScenario(), "FR3-BSS");
	}
}
