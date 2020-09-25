/**
 *
 */
package com.tradesurveil.bean;

/**
 * @author Kryselle
 *
 */
public class Security {

	private int securityID;
	private String name;
	private String type;
	private double initialMarketPrice;
	
	public int getSecurityID() {
		return securityID;
	}
	
	public void setSecurityID(int securityID) {
		this.securityID = securityID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public double getInitialMarketPrice() {
		return initialMarketPrice;
	}
	
	public void setInitialMarketPrice(double initialMarketPrice) {
		this.initialMarketPrice = initialMarketPrice;
	}

}
