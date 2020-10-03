package com.citi.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.citi.bean.TradeForDataGen;
import com.citi.bean.WashTradeScenario;


/**
 * @author Khyati
 *
 */
public class DetectWashTrades {
	private Map<String, List<TradeForDataGen>> brokerWiseTrades;
	private List<WashTradeScenario> detectedTrades = new ArrayList<WashTradeScenario>();
	private double thresholdValue = 2000d;

	private void getBrokerWiseFirmTrades(List<TradeForDataGen> tradeList) {
		brokerWiseTrades = new HashMap<String, List<TradeForDataGen>>();
		// find unique brokers from the trade list
		for(TradeForDataGen trade: tradeList) {
			String broker = trade.getBrokerName();
			if(brokerWiseTrades.get(broker) == null) {
				brokerWiseTrades.put(broker, new ArrayList<TradeForDataGen>());
			}
			List<TradeForDataGen> update = brokerWiseTrades.get(broker);
			update.add(trade);
			brokerWiseTrades.put(broker, update);
		}
	}

	private void findMatchingTrades(String broker){
		List<TradeForDataGen> brokerTrades = brokerWiseTrades.get(broker);
		for(int idx = 0; idx < brokerTrades.size() - 1; idx++) {
			TradeForDataGen trade1 = brokerTrades.get(idx);
			for(int idx2 = idx + 1; idx2 < brokerTrades.size(); idx2++) {
				TradeForDataGen trade2 = brokerTrades.get(idx2);
				if(Math.abs(trade1.getPrice()*trade1.getQuantity() - trade2.getPrice()*trade2.getQuantity()) < thresholdValue &&
						!trade2.getType().equals(trade1.getType()) &&
						trade2.getSecurityName().equals(trade1.getSecurityName()) &&
						trade2.getSecurityType().equals(trade1.getSecurityType())
						){
					WashTradeScenario scenario = new WashTradeScenario();
					List<TradeForDataGen> involvedTrades = new ArrayList<>();
					involvedTrades.add(trade1);
					involvedTrades.add(trade2);
					scenario.setInvolvedTrades(involvedTrades);
					detectedTrades.add(scenario);
					break;
				}
			}
		}
	}

	public List<WashTradeScenario> detectWashTrades(List<TradeForDataGen> tradeList){
		getBrokerWiseFirmTrades(tradeList);
		for(String broker: brokerWiseTrades.keySet()) {
			findMatchingTrades(broker);
		}
		return detectedTrades;
	}

	//	public  void main(String[] args) {
	////		DatasetGenerator gen = new DatasetGenerator();
	//		
	//		
	//		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	//		TradeJDBCTemplate tradeJDBCTemplate = (TradeJDBCTemplate)context.getBean("tradeJDBCTemplate");
	//		List<TradeForDataGen> tradeList = tradeJDBCTemplate.getTradeList();
	//		tradeList = tradeJDBCTemplate.getTradeListForWashTrades();
	//		detectWashTrades(tradeList);
	//		for(WashTradeScenario scenario: detectedTrades) {
	//			System.out.println("***************");
	//			for(TradeForDataGen trade: scenario.getInvolvedTrades()) {
	//			
	//				System.out.println(trade.getType() + "\t" + trade.getTimestamp() + "\t" + trade.getSecurityName() + "\t" + trade.getSecurityType() + "\t" + trade.getBrokerName() + "\t" + trade.getTraderName() + "\t" + trade.getPrice() + "\t" + trade.getQuantity());				
	//			}
	//		}
	//	}
}

//&&
//
//((trade1.getType().equals("Buy") && 
//		trade1.getQuantity()*trade1.getPrice() - trade2.getQuantity()*trade2.getPrice() < thresholdValue) ||
//(trade2.getType().equals("Buy") && 
//				trade2.getQuantity()*trade2.getPrice() - trade1.getQuantity()*trade1.getPrice() < thresholdValue))