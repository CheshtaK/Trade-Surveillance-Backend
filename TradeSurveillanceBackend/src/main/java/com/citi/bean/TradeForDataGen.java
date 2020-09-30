/**
 *
 */
package com.citi.bean;

import java.sql.Timestamp;

/**
 * TradeForDataGen is the trade POJO for data generation including the field trade type, time trade was made,
 * security name and type, broker name, trader name, trade quantity and trade price
 * @author Kryselle
 *
 */
public class TradeForDataGen {

	private String type;
	private Timestamp timestamp;
	private String securityName;
	private String securityType;
	private String brokerName;
	private String traderName;
	private int quantity;
	private double price;
	
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

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getTraderName() {
		return traderName;
	}

	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	
}
