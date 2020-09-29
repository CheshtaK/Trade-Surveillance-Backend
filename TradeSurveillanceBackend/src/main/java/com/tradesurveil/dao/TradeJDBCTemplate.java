package com.tradesurveil.dao;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tradesurveil.bean.TradeForDataGen;
import com.tradesurveil.businesslogic.DatasetGenerator;


public class TradeJDBCTemplate implements TradeDAO {
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	   
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public void insertTrade(TradeForDataGen trade) {
		String SQL = "INSERT INTO Trades (type, timestamp, security_name, security_type, broker_name, trader_name, quantity, price) values (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplateObject.update(SQL, trade.getType(), trade.getTimestamp(), trade.getSecurityName(), trade.getSecurityType(), trade.getBrokerName(), trade.getTraderName(), trade.getQuantity(), trade.getPrice());
	}

	public List<TradeForDataGen> fetchTradeList() {
		String SQL = "SELECT * FROM Trades;";
		List<TradeForDataGen> tradeList = jdbcTemplateObject.query(SQL, new TradeMapper());
		return tradeList;
	}
	
	public void insertTradeList(List<TradeForDataGen> tradeList) {
		deleteTrades();
		for (TradeForDataGen trade : tradeList) {
			insertTrade(trade);
		}
	}
	
	public void deleteTrades() {
		String SQL = "DROP TABLE IF EXISTS Trades;";
		jdbcTemplateObject.execute(SQL);
		SQL = "CREATE TABLE Trades (trade_id INT NOT NULL AUTO_INCREMENT, type VARCHAR(45), timestamp TIMESTAMP, security_name VARCHAR(45), security_type VARCHAR(45), broker_name VARCHAR(45), trader_name VARCHAR(45), quantity INT, price DOUBLE(10,2), PRIMARY KEY (trade_id));";
		jdbcTemplateObject.execute(SQL);
	}
	
	@SuppressWarnings("resource")
	public List<TradeForDataGen> getTradeList() {
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		TradeJDBCTemplate tradeJDBCTemplate = (TradeJDBCTemplate)context.getBean("tradeJDBCTemplate");
		DatasetGenerator datasetGenerator = new DatasetGenerator();
		List<TradeForDataGen> tradeList = new ArrayList<>();

		tradeList = datasetGenerator.generateRandomTrades(90, 100);
		tradeJDBCTemplate.insertTradeList(tradeList);
		tradeList = tradeJDBCTemplate.fetchTradeList();
		return tradeList;
	}
}