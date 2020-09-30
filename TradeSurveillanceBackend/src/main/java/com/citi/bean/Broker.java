/**
 *
 */
package com.citi.bean;

/**
 * Broker is the POJO including broker ID and name
 * @author Kryselle
 *
 */
public class Broker {
	
	private int brokerID;
	private String name;
	
	public int getBrokerID() {
		return brokerID;
	}
	
	public void setBrokerID(int brokerID) {
		this.brokerID = brokerID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
