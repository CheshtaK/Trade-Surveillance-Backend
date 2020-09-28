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

class DetectFrontRunningSBB {
	
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
	void testFrontRunning_SBB_Put_ES_Put(){
		TradeForDataGen firmOrderPast = initializeData("sell","2020-10-05 9:05:38","Apple", "Put",130, 1412.96, "Citi Group", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("buy","2020-10-05 9:05:42","Apple", "ES",9500, 1411.96, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("buy","2020-10-05 9:05:50","Apple", "Put",130, 1409.24, "Citi Group", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		hashmap = tester.detectFrontRunning(tradeList);
		assertEquals("FR4-SBB",hashmap.get(tradeList));
	}
	
	@Test
	void testFrontRunning_SBB_Put_Fut_Put(){
		TradeForDataGen firmOrderPast = initializeData("sell","2020-10-05 9:05:38","Walmart", "Put",250, 1635.56, "Citi Group", "Citi" );
		tradeList.add(firmOrderPast);
		TradeForDataGen clientOrder = initializeData("buy","2020-10-05 9:05:42","Walmart", "Futures",13000, 1635.58, "Client", "Citi" );
		tradeList.add(clientOrder);
		TradeForDataGen firmOrderFuture = initializeData("buy","2020-10-05 9:05:50","Walmart", "Put",250, 1629.03, "Citi Group", "Citi" );
		tradeList.add(firmOrderFuture);
		
		DetectFrontRunning tester = new DetectFrontRunning();
		hashmap = tester.detectFrontRunning(tradeList);
		assertEquals("FR4-SBB",hashmap.get(tradeList));
	}

}
