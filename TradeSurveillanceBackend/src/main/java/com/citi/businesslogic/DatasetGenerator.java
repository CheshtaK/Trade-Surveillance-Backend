/**
 * 
 */
package com.citi.businesslogic;

import java.sql.Timestamp;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citi.bean.TradeForDataGen;
import com.citi.bean.WashTradeScenario;
import com.citi.controller.TradeController;


/**
 * DatasetGenerator generates the entire trade dataset including the front running trades
 * @author Kryselle, Vishal
 *
 */
public class DatasetGenerator {

	private static final Logger log = LoggerFactory.getLogger(DatasetGenerator.class);
	
	// TODO: change range of quantity after changing prices to USD
	private double thresholdSecurityValue = 100000d;
	
	List<String> brokerList = generateBrokerList();
	List<String> traderList = generateTraderList();
	List<String> securityNameList = generateSecurityNameList();
	List<String> securityTypeList = generateSecurityTypeList();
	List<String> tradeTypes = generateTradeTypes();
	Map<String, Double> marketPrice = initialMarketPrice();
	
	// Timestamps in IST for conversion in GMT
	static Timestamp timestamp = Timestamp.valueOf("2020-10-05 14:30:00");
	Timestamp closingTime = Timestamp.valueOf("2020-10-05 20:30:00");
	
	/**
	 * generateRandomTrades generates the entire trade list including front running trades
	 * @param minNumberOfTrades is the minimum number of trades to be generated
	 * @param maxNumberOfTrades is the maximum number of trades to be generated
	 * @return the generated trade list
	 */
	public List<TradeForDataGen> generateRandomTrades(int minNumberOfTrades, int maxNumberOfTrades) {
		
		List<TradeForDataGen> tradeList= new ArrayList<TradeForDataGen>();
		int randomNumber = (int) ((Math.random() * (maxNumberOfTrades - minNumberOfTrades)) + minNumberOfTrades);
		
		for (int i = 0; i < randomNumber; i++) {
			TradeForDataGen trade = new TradeForDataGen();
			
			//HARDCODED TIMESTAMP INCREASE RANGE OF 10s
			timestamp = new Timestamp(timestamp.getTime() + generateRandomNumber(4, 10) * 1000);
			if(timestamp.compareTo(closingTime) >= 0)
				return tradeList;
			trade.setTimestamp(timestamp);
//code for wash trades
			
			if(i%generateRandomNumber(2,3)==0 && i> (randomNumber/2) )
			{
				int k=generateRandomNumber(0,i-1);
				TradeForDataGen oldtrade = tradeList.get(k);
				
				if(oldtrade.getTraderName()=="Citi Global Markets")
				{
				trade.setBrokerName(oldtrade.getBrokerName());
				trade.setTraderName(oldtrade.getTraderName());
				trade.setSecurityName(oldtrade.getSecurityName());
				trade.setSecurityType(securityTypeList.get(generateRandomNumber(0, securityTypeList.size() - 1)));
				
				
				double currentMarketPrice = marketPrice.get(trade.getSecurityName()+"-"+trade.getSecurityType());
				double increase; 
				int quantity;
				quantity =(int) ((oldtrade.getPrice()*oldtrade.getQuantity())/currentMarketPrice);
				
				trade.setPrice(currentMarketPrice);
				trade.setQuantity(quantity);
				
				if(oldtrade.getType()=="Buy")
				{
					trade.setType("Sell w");
					 increase = ((double) trade.getQuantity())/600 * (-5);
				}
				else
				{
					trade.setType("Buy w");
					increase = ((double) trade.getQuantity())/600 * 5;
				}
				
				double newMarketPrice =  marketPrice.get(trade.getSecurityName()+"-"+trade.getSecurityType()) +increase;
				marketPrice.replace(trade.getSecurityName()+"-"+trade.getSecurityType(), newMarketPrice);
				tradeList.add(trade);
			//	System.out.println("wash"+oldtrade.getType() + "\t" + oldtrade.getTimestamp() + "\t" + oldtrade.getSecurityName() + "\t" + oldtrade.getSecurityType() + "\t" + oldtrade.getBrokerName() + "\t" + oldtrade.getTraderName() + "\t" + oldtrade.getPrice() + "\t" + oldtrade.getQuantity());
				}
				
			}
			
			else
			{
			trade.setType(tradeTypes.get(generateRandomNumber(0, tradeTypes.size() - 1)));
			trade.setSecurityName(securityNameList.get(generateRandomNumber(0, securityNameList.size() - 1)));
			trade.setSecurityType(securityTypeList.get(generateRandomNumber(0, securityTypeList.size() - 1)));
			trade.setBrokerName(brokerList.get(generateRandomNumber(0, brokerList.size() - 1)));
			trade.setTraderName(traderList.get(generateRandomNumber(0, traderList.size() - 1)));
			
			//HARDCODED QUANTITY RANGE of 100
			trade.setQuantity(generateRandomNumber(500, 1100));
			
			//HARDCODED MARKET PRICE TO INCREASE PROPORTIONALLY TO QUANTITY IN CASE OF BUY AND DECREASE IN CASE OF SELL WITHIN 5 RUPEE RANGE
			double currentMarketPrice = marketPrice.get(trade.getSecurityName()+"-"+trade.getSecurityType());
			double newMarketPrice = currentMarketPrice;
			/*if (trade.getType() == "Buy") {
				double increase = ((double) trade.getQuantity())/100 * 5;
				newMarketPrice = currentMarketPrice + increase;
			}
			else {
				double decrease = ((double) trade.getQuantity())/100 * 5;
				newMarketPrice = currentMarketPrice - decrease;
			}*/
			trade.setPrice(Math.round(newMarketPrice * 100.0) / 100.0);
			
			
			//code for front running 
			if(trade.getPrice()*trade.getQuantity()>= thresholdSecurityValue && (trade.getSecurityType()=="ES"||trade.getSecurityType()=="Futures")) {

				TradeForDataGen trade2 = new TradeForDataGen();
				TradeForDataGen trade3 = new TradeForDataGen();
				
				if(trade.getTraderName()!="Citi Global Markets") {
					
					if(trade.getType() == "Buy") {
						trade2.setSecurityType(securityTypeList.get(generateRandomNumber(0, securityTypeList.size() - 1)));
						trade3.setSecurityType(trade2.getSecurityType());
						double increase;
						//if put type then SBB situation
						if(trade2.getSecurityType()=="Put") {	
							trade2.setType("Sell");
							trade3.setType("Buy");
							increase = ((double) trade.getQuantity())/600 * (-5);
						}
						else {
							//for call option bullish view propagates
							trade2.setType("Buy");
							trade3.setType("Sell");
							increase = ((double) trade.getQuantity())/600 * 5;
						}
						Timestamp timestamp2 = new Timestamp(timestamp.getTime() - generateRandomNumber(1, 3) * 1000);
						trade2.setTimestamp(timestamp2);
						trade2.setTraderName("Citi Global Markets");
						trade2.setBrokerName(brokerList.get(generateRandomNumber(0, brokerList.size() - 1)));
						trade2.setQuantity(generateRandomNumber(trade.getQuantity()/2,trade.getQuantity() ));
						trade2.setSecurityName(trade.getSecurityName());
						trade2.setPrice(marketPrice.get(trade2.getSecurityName()+"-"+trade2.getSecurityType()));	
						newMarketPrice = marketPrice.get(trade2.getSecurityName()+"-"+trade2.getSecurityType()) + increase;
						marketPrice.replace(trade2.getSecurityName()+"-"+trade2.getSecurityType(), newMarketPrice);
						
						timestamp = new Timestamp(timestamp.getTime() + generateRandomNumber(1, 3) * 1000);
						trade3.setTimestamp(timestamp);
						trade3.setTraderName("Citi Global Markets");
						trade3.setBrokerName(brokerList.get(generateRandomNumber(0, brokerList.size() - 1)));
						trade3.setQuantity(generateRandomNumber(trade.getQuantity()/2,trade.getQuantity() ));
						trade3.setSecurityName(trade2.getSecurityName());
						trade3.setPrice(marketPrice.get(trade3.getSecurityName()+"-"+trade3.getSecurityType()));
						
						tradeList.add(trade2);
						tradeList.add(trade);
						tradeList.add(trade3);
					}
					
					else if(trade.getType() == "Sell") {
						trade2.setSecurityType(securityTypeList.get(generateRandomNumber(0, securityTypeList.size() - 1)));
						trade3.setSecurityType(trade2.getSecurityType());
						double decrease;
						if(trade2.getSecurityType()=="Put") {
							trade2.setType("Buy");
							trade3.setType("Sell");
							decrease = ((double) trade.getQuantity())/600 * (-5);
						}
						else {
							trade2.setType("Sell");
							trade3.setType("Buy");
							decrease = ((double) trade.getQuantity())/600 * 5;
						}
						Timestamp timestamp2 = new Timestamp(timestamp.getTime() - generateRandomNumber(1, 3) * 1000);
						trade2.setTimestamp(timestamp2);
						trade2.setTraderName("Citi Global Markets");
						trade2.setBrokerName(trade.getBrokerName());
						trade2.setQuantity(generateRandomNumber(trade.getQuantity()/2,trade.getQuantity() ));
						trade2.setSecurityName(trade.getSecurityName());
						trade2.setPrice(marketPrice.get(trade2.getSecurityName()+"-"+trade2.getSecurityType()));
						newMarketPrice =  marketPrice.get(trade2.getSecurityName()+"-"+trade2.getSecurityType()) - decrease;
						marketPrice.replace(trade2.getSecurityName()+"-"+trade2.getSecurityType(), newMarketPrice);
						
						timestamp = new Timestamp(timestamp.getTime() + generateRandomNumber(1, 3) * 1000);
						trade3.setTimestamp(timestamp);
						trade3.setTraderName("Citi Global Markets");
						trade3.setBrokerName(trade.getBrokerName());
						trade3.setQuantity(generateRandomNumber(trade.getQuantity()/2,trade.getQuantity() ));
						trade3.setSecurityName(trade.getSecurityName());
						trade3.setPrice(marketPrice.get(trade3.getSecurityName()+"-"+trade3.getSecurityType()));
						
						tradeList.add(trade2);
						tradeList.add(trade);
						tradeList.add(trade3);
					}
				}
				marketPrice.replace(trade.getSecurityName()+"-"+trade.getSecurityType(), newMarketPrice);
				log.info("Front running trade sequence generated");
			}
			else {
				marketPrice.replace(trade.getSecurityName()+"-"+trade.getSecurityType(), newMarketPrice);
				tradeList.add(trade);
			}
		}
		}
		log.info("Random trade list including front running trades generated");
		return tradeList;
		
	}
	
	/**
	 * generateRandomNumber generates a random number within a given range
	 * @param min is the lower limit
	 * @param max is the upper limit
	 * @return the random number
	 */
	public int generateRandomNumber(int min, int max) {
		double random = Math.random() * (max - min) + min;
		return (int) Math.round(random);
	}
	
	/**
	 * generateBrokerList generates the list of brokers names
	 * @return the list of brokers names
	 */
	public List<String> generateBrokerList() {
		List<String> brokerList = new ArrayList<String>();
		brokerList.add("Citi Velocity");
		brokerList.add("Charles Schwab");
		brokerList.add("Fidelity Investments");
		brokerList.add("E*TRADE");
		brokerList.add("TD Ameritrade");
		return brokerList;
	}
	
	/**
	 * generateTraderList generates the list of trader names
	 * @return the list of broker names
	 */
	public List<String> generateTraderList() {
		List<String> traderList = new ArrayList<String>();
		traderList.add("Citi Global Markets");
		traderList.add("Vanguard Group");
		traderList.add("Bridgewater Associates");
		traderList.add("D. E. Shaw & Co.");
		return traderList;
	}
	
	/**
	 * generateSecurityNameList generates the list of security names
	 * @return the list of security names
	 */
	public List<String> generateSecurityNameList() {
		List<String> securityNameList = new ArrayList<String>();
		securityNameList.add("Apple");
		securityNameList.add("Facebook");
		securityNameList.add("Walmart");
		return securityNameList;
	}
	
	/**
	 * generateSecurityTypeList generates the list of security types
	 * @return the list of security types
	 */
	public List<String> generateSecurityTypeList() {
		List<String> securityTypeList = new ArrayList<String>();
		securityTypeList.add("ES");
		securityTypeList.add("Futures");
		securityTypeList.add("Call");
		securityTypeList.add("Put");
		return securityTypeList;
	}
	
	/**
	 * generateTradeType generates the list of trade types
	 * @return the list of trade types
	 */
	public List<String> generateTradeTypes() {
		List<String> tradeTypes = new ArrayList<String>();
		tradeTypes.add("Buy");
		tradeTypes.add("Sell");
		return tradeTypes;
	}
	
	/**
	 * initialMarketPrice sets the initial market prices of the securities
	 * @return the initial market prices
	 */
	public Map<String, Double> initialMarketPrice() {
		
		Map<String, Double> initialMarketPrice = new HashMap<String, Double>();
		
		initialMarketPrice.put("Apple-ES", 114.09);
		initialMarketPrice.put("Apple-Futures",113.25);
		initialMarketPrice.put("Apple-Call", 54.10);
		initialMarketPrice.put("Apple-Put", 51.01);
		
		initialMarketPrice.put("Facebook-ES", 261.79);
		initialMarketPrice.put("Facebook-Futures", 260.79);
		initialMarketPrice.put("Facebook-Call", 105.55);
		initialMarketPrice.put("Facebook-Put", 102.05);
		
		initialMarketPrice.put("Walmart-ES", 137.15);
		initialMarketPrice.put("Walmart-Futures", 102.00);
		initialMarketPrice.put("Walmart-Call", 31.15);
		initialMarketPrice.put("Walmart-Put", 30.14);
		
		return initialMarketPrice;
	}
	public static void main(String args[]) {
		DatasetGenerator datasetGenerator = new DatasetGenerator();
		List<TradeForDataGen> tradeList = new ArrayList<TradeForDataGen>();
		tradeList = datasetGenerator.generateRandomTrades(90, 100);
		for(TradeForDataGen trade: tradeList) {
			System.out.println(trade.getType() + "\t" + trade.getTimestamp() + "\t" + trade.getSecurityName() + "\t" + trade.getSecurityType() + "\t" + trade.getBrokerName() + "\t" + trade.getTraderName() + "\t" + trade.getPrice() + "\t" + trade.getQuantity());
		}
	
		List<WashTradeScenario> detectedTrades = new ArrayList<>();	
	//	 DetectWashTrades abc = new DetectWashTrades();
		//detectedTrades= abc.detectWashTrade(tradeList);
		//System.out.println("size :"+detectedTrades.size());
		//for(WashTradeScenario trade: detectedTrades) {
			//System.out.println();
		//}
		
		
		
	}
}