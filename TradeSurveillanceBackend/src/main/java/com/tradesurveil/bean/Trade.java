/**
 *
 */
package com.tradesurveil.bean;

import java.sql.Timestamp;

/**
 * @author Kryselle
 *
 */
public class Trade {
	
	private int tradeID;
	private String type;
	private Timestamp timestamp;
	private int securityID;
	private int brokerID;
	private int traderID;
	private int quantity;
	private double price;
	
	public int getTradeID() {
		return tradeID;
	}
	
	public void setTradeID(int tradeID) {
		this.tradeID = tradeID;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getSecurityID() {
		return securityID;
	}
	
	public void setSecurityID(int securityID) {
		this.securityID = securityID;
	}
	
	public int getBrokerID() {
		return brokerID;
	}
	
	public void setBrokerID(int brokerID) {
		this.brokerID = brokerID;
	}
	
	public int getTraderID() {
		return traderID;
	}
	
	public void setTraderID(int traderID) {
		this.traderID = traderID;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
}
