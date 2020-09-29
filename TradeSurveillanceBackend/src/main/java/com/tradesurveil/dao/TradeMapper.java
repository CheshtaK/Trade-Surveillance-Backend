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
		trade.setSecurityName(rs.getString("security_name"));
		trade.setSecurityType(rs.getString("security_type"));
		trade.setBrokerName(rs.getString("broker_name"));
		trade.setTraderName(rs.getString("trader_name"));
		trade.setQuantity(rs.getInt("quantity"));
		trade.setPrice(rs.getDouble("price"));
		return trade;
	}
	
}
