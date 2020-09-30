package com.citi.dao;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.citi.bean.TradeForDataGen;
import com.citi.businesslogic.DatasetGenerator;
import com.citi.controller.TradeController;

/**
 * TradeJDBCTemplate defines methods of TradeDAO and enables database operations
 * @author Kryselle
 *
 */
public class TradeJDBCTemplate implements TradeDAO {
	
	private static final Logger log = LoggerFactory.getLogger(TradeJDBCTemplate.class);
	
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	/**
	 * setDataSource initializes the dataSource
	 * @param dataSource the dataSource to be initialized
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	/**
	 * insertTrade inserts the trade into the database
	 * @param trade the trade to be inserted
	 */
	public void insertTrade(TradeForDataGen trade) {
		String SQL = "INSERT INTO Trades (type, timestamp, securityName, securityType, brokerName, traderName, quantity, price) values (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplateObject.update(SQL, trade.getType(), trade.getTimestamp(), trade.getSecurityName(), trade.getSecurityType(), trade.getBrokerName(), trade.getTraderName(), trade.getQuantity(), trade.getPrice());
		log.info("Trade inserted into database");
	}

	/**
	 * fetchTradeList fetched the existing trade list from the database
	 * @return fetched trade list from database
	 */
	public List<TradeForDataGen> fetchTradeList() {
		String SQL = "SELECT * FROM Trades ORDER BY timestamp ASC;";
		List<TradeForDataGen> tradeList = jdbcTemplateObject.query(SQL, new TradeMapper());
		log.info("Trade list fetched from database");
		return tradeList;
	}
	
	/**
	 * insertTradeList inserts the trade list into the database
	 * @param tradeList the list of trades to be inserted
	 */
	public void insertTradeList(List<TradeForDataGen> tradeList) {
		deleteTrades();
		for (TradeForDataGen trade : tradeList) {
			insertTrade(trade);
		}
	}
	
	/**
	 * deleteTrades drops the existing trade table and creates a new empty trade table
	 */
	public void deleteTrades() {
		String SQL = "DROP TABLE IF EXISTS Trades;";
		jdbcTemplateObject.execute(SQL);
		SQL = "CREATE TABLE Trades (trade_id INT NOT NULL AUTO_INCREMENT, type VARCHAR(45), timestamp TIMESTAMP, securityName VARCHAR(45), securityType VARCHAR(45), brokerName VARCHAR(45), traderName VARCHAR(45), quantity INT, price DOUBLE(10,2), PRIMARY KEY (trade_id));";
		jdbcTemplateObject.execute(SQL);
	}
	
	/**
	 * getTradeList generates trades, pushes them to the database, and returns the generated trade list
	 * @return generates trade list
	 */
	@SuppressWarnings("resource")
	public List<TradeForDataGen> getTradeList() {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		TradeJDBCTemplate tradeJDBCTemplate = (TradeJDBCTemplate)context.getBean("tradeJDBCTemplate");
		DatasetGenerator datasetGenerator = new DatasetGenerator();
		List<TradeForDataGen> tradeList = new ArrayList<>();

		tradeList = datasetGenerator.generateRandomTrades(90, 100);
		tradeJDBCTemplate.insertTradeList(tradeList);
		tradeList = tradeJDBCTemplate.fetchTradeList();
		log.info("Trades generated and pushed to database");
		return tradeList;
	}
}
