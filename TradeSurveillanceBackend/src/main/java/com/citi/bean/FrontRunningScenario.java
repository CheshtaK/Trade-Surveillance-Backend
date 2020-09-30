/**
 * 
 */
package com.citi.bean;

import java.util.List;

/**
 * FrontRunningScenario object has 2 data members :- 
 * a list of detected front-running trades, 
 * and the a string representing the scenario detected.
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
