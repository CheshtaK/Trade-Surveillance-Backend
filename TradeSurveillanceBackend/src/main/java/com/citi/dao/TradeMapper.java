package com.citi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.citi.bean.TradeForDataGen;

/**
 * TradeMapper maps incoming trades to the POJO
 * @author Kryselle
 *
 */
public class TradeMapper implements RowMapper<TradeForDataGen> {

	/**
	 * mapRow maps the data items in a row to the POJO
	 * @param rs the result set
	 * @param rowNum the row number
	 * @return mapped trade
	 */
	public TradeForDataGen mapRow(ResultSet rs, int rowNum) throws SQLException {
		TradeForDataGen trade = new TradeForDataGen();
		trade.setType(rs.getString("type"));
		trade.setTimestamp(rs.getTimestamp("timestamp"));
		trade.setSecurityName(rs.getString("securityName"));
		trade.setSecurityType(rs.getString("securityType"));
		trade.setBrokerName(rs.getString("brokerName"));
		trade.setTraderName(rs.getString("traderName"));
		trade.setQuantity(rs.getInt("quantity"));
		trade.setPrice(rs.getDouble("price"));
		return trade;
	}
	
}
