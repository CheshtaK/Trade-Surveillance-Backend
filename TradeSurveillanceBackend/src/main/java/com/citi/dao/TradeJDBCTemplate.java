package com.citi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.citi.bean.TradeForDataGen;
import com.citi.businesslogic.DatasetGenerator;
import com.citi.controller.TradeController;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

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
	
	/**
	 * sendmail sends an email warning about the detected scenario
	 */
	public void sendmail() throws MessagingException, IOException, javax.mail.MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		   
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		   protected PasswordAuthentication getPasswordAuthentication() {
		      return new PasswordAuthentication("cheshta.kwatra@gmail.com", "<my password>");
		   }
		});
		   
		MimeMessage message = new MimeMessage( session );
		Multipart multipart = new MimeMultipart( "alternative" );


		message.saveChanges();

		message.setFrom(new InternetAddress("cheshta.kwatra@gmail.com", false));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("cheshta.kwatra@gmail.com"));
		message.setSubject("Front Running | Possible scenario detected");
		message.setSentDate(new Date());
		   
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("A possible front running scenario has been detected as on date: October 5th, 2020."
				   + " Details of the detected transaction are available in the attached pdf."
				   + " Please evaluate and take necessary actions.", "text/html");
		   
		multipart.addBodyPart(messageBodyPart);
		MimeBodyPart attachPart = new MimeBodyPart();

		attachPart.attachFile("C:/Users/chesh/Downloads/front_running.pdf");
		multipart.addBodyPart(attachPart);
		   
		message.setContent( multipart );
		   
		Transport.send(message);
	}
	
	/**
	 * sendmailWash sends an email warning about the detected scenario
	 */
	public void sendmailWash() throws MessagingException, IOException, javax.mail.MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		   
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		   protected PasswordAuthentication getPasswordAuthentication() {
		      return new PasswordAuthentication("cheshta.kwatra@gmail.com", "<my password>");
		   }
		});
		   
		MimeMessage message = new MimeMessage( session );
		Multipart multipart = new MimeMultipart( "alternative" );


		message.saveChanges();

		message.setFrom(new InternetAddress("cheshta.kwatra@gmail.com", false));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("cheshta.kwatra@gmail.com"));
		message.setSubject("Wash Trade | Possible scenario detected");
		message.setSentDate(new Date());
		   
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("A possible wash trade scenario has been detected as on date: October 5th, 2020."
				   + " Details of the detected transaction are available in the attached pdf."
				   + " Please evaluate and take necessary actions.", "text/html");
		   
		multipart.addBodyPart(messageBodyPart);
		MimeBodyPart attachPart = new MimeBodyPart();

		attachPart.attachFile("C:/Users/chesh/Downloads/wash_trade.pdf");
		multipart.addBodyPart(attachPart);
		   
		message.setContent( multipart );
		
		Transport.send(message);
	}
	
	/**
	 * @return TradeList with only Firm Trades
	 */
	public List<TradeForDataGen> fetchTradeListForWashTrades(){
		String SQL = "SELECT * FROM Trades where traderName = 'Citi Global Markets' ORDER BY timestamp ASC ;";
		List<TradeForDataGen> tradeList = jdbcTemplateObject.query(SQL, new TradeMapper());
		log.info("Trade list fetched from database for Wash Trade Detection");
		return tradeList;
	}
}
