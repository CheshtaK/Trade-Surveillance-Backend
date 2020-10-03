package com.citi.bean;
import java.util.List;

public class WashTradeScenario {

	private List<TradeForDataGen> involvedTrades;
	private String scenario;
	public List<TradeForDataGen> getInvolvedTrades() {
		return involvedTrades;
	}
	public void setInvolvedTrades(List<TradeForDataGen> involvedTrades) {
		this.involvedTrades = involvedTrades;
	}
}
