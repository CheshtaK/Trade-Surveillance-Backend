package com.citi.businesslogic;
import com.citi.bean.FrontRunningScenario;
import com.citi.bean.TradeForDataGen;
import com.citi.bean.WashTradeScenario;

import java.util.List;
import java.util.ArrayList;

public class DetectWashTrades {
	// Hashmap to store detected front-running transactions along with the scenario
		private List<WashTradeScenario> detectedTrades = new ArrayList<>();
		private List<TradeForDataGen> indexCitiTrades = new ArrayList<>();
				
		private void findCitiTrades(List<TradeForDataGen> tradeList) {
			/**
			 * Finds indices of citi trades in list
			 * */
			for(int i = 0; i < tradeList.size(); i++) {
				TradeForDataGen currTrade = tradeList.get(i);
				if(currTrade.getTraderName().equals("Citi Global Markets") )
					indexCitiTrades.add(currTrade);
			}
		}
		
		
		
		public List<WashTradeScenario> detectWashTrade(List<TradeForDataGen> tradeList){
			/** Returns detected wash trades 
			 * */
			findCitiTrades(tradeList);
			TradeForDataGen currentTrade, tempTrade;
			for(int i=0;i<indexCitiTrades.size();i++) {
				currentTrade =tradeList.get(i);
				System.out.println(i+" "+currentTrade.getSecurityName()+""+currentTrade.getSecurityType());
				
				for(int j=i+1;i<indexCitiTrades.size();j++)
				{
					tempTrade =tradeList.get(j);
					System.out.println(j+" "+tempTrade.getSecurityName()+""+tempTrade.getSecurityType());
					
						
					
				}
				
			}
			return detectedTrades;
		}


}
