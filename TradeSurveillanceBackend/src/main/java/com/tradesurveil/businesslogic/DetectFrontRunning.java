/**
 * 
 */
package com.tradesurveil.businesslogic;

import com.tradesurveil.bean.TradeForDataGen;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Khyati
 *
 */

public class DetectFrontRunning {
	// Hashmap to store detected front-running transactions along with the scenario
	private HashMap<List<TradeForDataGen>, String> hashmap = new HashMap<>();
	private List<Integer> indexListLargeClients = new ArrayList<>();
	
	//HARDCODED AND TO BE UPDATED
	private double thresholdSecurityValue = 5000d;
	private long thresholdTimeMilliseconds = 60000;
	
	
	private void findLargeClientTrades(List<TradeForDataGen> tradeList) {
		/**
		 * Finds indices of large client trades in the tradeList
		 * */
		for(int i = 0; i < tradeList.size(); i++) {
			TradeForDataGen currTrade = tradeList.get(i);
			if(!currTrade.getTraderName().equals("Citi Group") && 
					currTrade.getQuantity() * currTrade.getPrice() >= thresholdSecurityValue)
				indexListLargeClients.add(i);
		}
	}
	
	private List<Integer> getFutureFirmTrades(List<TradeForDataGen> tradeList, int idxPast, int idxClient){
		/**
		 * Returns indices of firm trades made within thresholdTimeMilliseconds from the past firm trade
		 * and of the opposite nature (buy or sell) to the past firm trade
		 * */
		List<Integer> futureTradesIndex = new ArrayList<>();
		for(int i = idxClient + 1; 
				i < tradeList.size() && 
				tradeList.get(i).getTimestamp().getTime() - tradeList.get(idxPast).getTimestamp().getTime() < thresholdTimeMilliseconds; 
				i++) {
			if(tradeList.get(idxPast).getTraderName().equals(tradeList.get(i).getTraderName()) && //check if it is a firm trade
					tradeList.get(idxPast).getSecurityName().equals(tradeList.get(i).getSecurityName()) && //check if trade is made on same security as past firm trade
					!tradeList.get(idxPast).getType().equals(tradeList.get(i).getType()) //check if opposite nature to past firm trade
					)
				futureTradesIndex.add(i);
		}
		return futureTradesIndex;
	}
	
	
	public HashMap<List<TradeForDataGen>, String> detectFrontRunning(List<TradeForDataGen> tradeList){
		/** Returns detected front-running trades along with the type of scenario 
		 * 1-BBS, 2-SSB, 3-BSS, 4-SBB
		 * */
		TradeForDataGen clientOrder, firmOrderPast, firmOrderFuture;
		findLargeClientTrades(tradeList);
		for(int i: indexListLargeClients) {
			clientOrder = tradeList.get(i);
			
			for(int idxPast = i-1; 
					idxPast >= 0 && clientOrder.getTimestamp().getTime() - tradeList.get(idxPast).getTimestamp().getTime() < thresholdTimeMilliseconds; 
					idxPast--) {
				
				firmOrderPast = tradeList.get(idxPast);
				
				// check if a firm order is placed for the same corporation as the client trade
				if(firmOrderPast.getTraderName().equals("Citi Group") &&
						firmOrderPast.getSecurityName().equals(clientOrder.getSecurityName())) {
					
					List<Integer> futureTradesIndex = getFutureFirmTrades(tradeList, idxPast, i);
					
					if(futureTradesIndex.size() == 0) continue; // no front-running in this case, continue to check 
					
					List<TradeForDataGen> temp = new ArrayList<>();
					String FirmSecurityTypePast = firmOrderPast.getSecurityType();
					String ClientSecurityType = clientOrder.getSecurityType();
					
					//Scenario 1 - BBS
					if(firmOrderPast.getType().equals("buy") &&
							clientOrder.getType().equals("buy")) {
						
						for(int idxFuture: futureTradesIndex) {
							firmOrderFuture = tradeList.get(idxFuture);
							String FirmSecurityTypeFuture = firmOrderFuture.getSecurityType();
							if((FirmSecurityTypePast.equals("ES") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("ES")) ||
									(FirmSecurityTypePast.equals("Futures") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("Futures")) ||
									(FirmSecurityTypePast.equals("Call") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("Call")) ||
									(FirmSecurityTypePast.equals("ES") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("ES")) ||
									(FirmSecurityTypePast.equals("Futures") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("Futures")) ||
									(FirmSecurityTypePast.equals("Call") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("Call"))
									) {
								temp.add(firmOrderFuture);							
							}		
						}
						if(temp.size() != 0) {
							temp.add(0, firmOrderPast);	
							temp.add(1, clientOrder);
							hashmap.put(temp, "FR1-BBS");	
						}
					}
					//Scenario 2 - SSB
					else if(firmOrderPast.getType().equals("sell") &&
							clientOrder.getType().equals("sell")) {
						
						for(int idxFuture: futureTradesIndex) {
							firmOrderFuture = tradeList.get(idxFuture);
							String FirmSecurityTypeFuture = firmOrderFuture.getSecurityType();
							
							if((FirmSecurityTypePast.equals("ES") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("ES")) ||
									(FirmSecurityTypePast.equals("Futures") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("Futures")) ||
									(FirmSecurityTypePast.equals("Call") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("Call")) ||
									(FirmSecurityTypePast.equals("ES") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("ES")) ||
									(FirmSecurityTypePast.equals("Futures") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("Futures")) ||
									(FirmSecurityTypePast.equals("Call") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("Call"))
									) {
								temp.add(firmOrderFuture);							
							}		
						}
						if(temp.size() != 0) {
							temp.add(0, firmOrderPast);	
							temp.add(1, clientOrder);
							hashmap.put(temp, "FR2-SSB");	
						}	
					}
					//Scenario 3 - BSS
					else if(firmOrderPast.getType().equals("buy") &&
							clientOrder.getType().equals("sell")) {
						
						for(int idxFuture: futureTradesIndex) {
							firmOrderFuture = tradeList.get(idxFuture);
							String FirmSecurityTypeFuture = firmOrderFuture.getSecurityType();
							
							if((FirmSecurityTypePast.equals("Put") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("Put")) ||
									(FirmSecurityTypePast.equals("Put") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("Put"))
									) {
								temp.add(firmOrderFuture);							
							}		
						}
						if(temp.size() != 0) {
							temp.add(0, firmOrderPast);	
							temp.add(1, clientOrder);
							hashmap.put(temp, "FR3-BSS");	
						}	
					}
					//Scenario 4 - SBB
					else if(firmOrderPast.getType().equals("sell") &&
							clientOrder.getType().equals("buy")) {
						
						for(int idxFuture: futureTradesIndex) {
							firmOrderFuture = tradeList.get(idxFuture);
							String FirmSecurityTypeFuture = firmOrderFuture.getSecurityType();
							
							if((FirmSecurityTypePast.equals("Put") && ClientSecurityType.equals("ES") && FirmSecurityTypeFuture.equals("Put")) ||
									(FirmSecurityTypePast.equals("Put") && ClientSecurityType.equals("Futures") && FirmSecurityTypeFuture.equals("Put"))
									) {
								temp.add(firmOrderFuture);							
							}		
						}
						if(temp.size() != 0) {
							temp.add(0, firmOrderPast);	
							temp.add(1, clientOrder);
							hashmap.put(temp, "FR4-SBB");	
						}
					}					
				}
			}
		}
		return hashmap;
	}
}

