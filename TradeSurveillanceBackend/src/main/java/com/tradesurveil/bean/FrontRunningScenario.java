/**
 * 
 */
package com.tradesurveil.bean;

import java.util.List;

/**
 * @author Khyati
 *
 */
public class FrontRunningScenario {
	private List<TradeForDataGen> involvedTrades;
	private String scenario;
	public List<TradeForDataGen> getInvolvedTrades() {
		return involvedTrades;
	}
	public void setInvolvedTrades(List<TradeForDataGen> involvedTrades) {
		this.involvedTrades = involvedTrades;
	}
	public String getScenario() {
		return scenario;
	}
	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
}
