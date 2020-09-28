package com.tradesurveil.businesslogictest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tradesurveil.bean.TradeForDataGen;
import com.tradesurveil.businesslogic.DetectFrontRunning;

import org.junit.jupiter.api.Test;

class DetectFrontRunningBSS {
	
	private List<TradeForDataGen> tradeList = new ArrayList<TradeForDataGen>();
	private HashMap<List<TradeForDataGen>, String> hashmap = new HashMap<>();
	
	private TradeForDataGen initializeData(String type,String t,String securityName,String securityType, int quantity, double price, String traderName, String brokerName) {
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
		TradeForDataGen firmOrderPast = initializeData("buy","2020-10-05 9:05:38","Facebook", "Put",130, 18444.31, "Citi Group", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("sell","2020-10-05 9:05:42","Facebook", "ES",9500, 18444.43, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("sell","2020-10-05 9:05:50","Facebook", "Put",130, 18500.31, "Citi Group", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		hashmap = tester.detectFrontRunning(tradeList);
		assertEquals("FR3-BSS",hashmap.get(tradeList));
	}
	
	@Test
	void testFrontRunning_BSS_Put_Fut_Put(){
		TradeForDataGen firmOrderPast = initializeData("buy","2020-10-05 9:05:38","Facebook", "Put",130, 18444.31, "Citi Group", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("sell","2020-10-05 9:05:42","Facebook", "Futures",9500, 18444.43, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("sell","2020-10-05 9:05:50","Facebook", "Put",130, 18500.31, "Citi Group", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		hashmap = tester.detectFrontRunning(tradeList);
		assertEquals("FR3-BSS",hashmap.get(tradeList));
	}
}
