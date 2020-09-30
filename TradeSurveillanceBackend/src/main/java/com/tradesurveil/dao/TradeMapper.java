package com.tradesurveil.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.tradesurveil.bean.TradeForDataGen;

public class TradeMapper implements RowMapper<TradeForDataGen> {

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
