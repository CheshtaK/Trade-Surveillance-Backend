package com.citi.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.citi.bean.FrontRunningScenario;
import com.citi.bean.TradeForDataGen;
import com.citi.bean.WashTradeScenario;
import com.citi.businesslogic.DetectFrontRunning;
import com.citi.businesslogic.DetectWashTrades;
import com.citi.dao.TradeJDBCTemplate;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

/**
* TradeController is the REST Controller used by the application
* @author Kryselle
* 
*/
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TradeController {
	
	private static final Logger log = LoggerFactory.getLogger(TradeController.class);
	
	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	TradeJDBCTemplate tradeJDBCTemplate = (TradeJDBCTemplate)context.getBean("tradeJDBCTemplate");
	
	/**
	 * getTradeList generates the random trade list including front running trades and returns it 
	 * @return the generated trade list
	 */
	@RequestMapping(value = TradeRestURIConstants.GET_TRADELIST, method = RequestMethod.GET)
	public @ResponseBody List<TradeForDataGen> getTradeList() {
		List<TradeForDataGen> tradeList = tradeJDBCTemplate.getTradeList();
		log.info("Trade list generated and sent");
		return tradeList;
	}
	
	/**
	 * insertTrade inserts the trade
	 * @param trade the trade to be inserted
	 * @return confirmation of insert
	 */
	@RequestMapping(value = TradeRestURIConstants.INSERT_TRADE, method = RequestMethod.POST)
	public @ResponseBody void insertTrade(@RequestBody TradeForDataGen trade) {
		log.info("Trade inserted");
		tradeJDBCTemplate.insertTrade(trade);
	}
	
	/**
	 * fetchTradeList fetches the generated trade list 
	 * @return the fetched trade list
	 */	
	@RequestMapping(value = TradeRestURIConstants.FETCH_TRADELIST, method = RequestMethod.GET)
	public @ResponseBody List<TradeForDataGen> fetchTradeList() {
		List<TradeForDataGen> tradeList = tradeJDBCTemplate.fetchTradeList();
		log.info("Trade list fetched");
		return tradeList;
	}
	
	/**
	 * frontRunningTradesDetector detects the trades involved in front running 
	 * @return the detected front running trade list
	 */
	@RequestMapping(value = TradeRestURIConstants.GET_FRONT_RUNNING_TRADES, method = RequestMethod.GET)
	public @ResponseBody List<FrontRunningScenario> frontRunningTradesDetector() {
		List<TradeForDataGen> tradeList = tradeJDBCTemplate.fetchTradeList();
		DetectFrontRunning detector = new DetectFrontRunning();
		List<FrontRunningScenario> frontRunningTradeList = detector.detectFrontRunning(tradeList);
		log.info("Front running trades detected and sent");
		return frontRunningTradeList;
	}
	
	/**
	 * sendEmail sends an email to the concerned authority to warn them about front running scenarios detected
	 * @return sent email confirmation
	 * @throws javax.mail.MessagingException 
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	@RequestMapping(value = TradeRestURIConstants.SEND_EMAIL, method = RequestMethod.GET)
	public String sendEmail() throws MessagingException, IOException, javax.mail.MessagingException {
		tradeJDBCTemplate.sendmail();
		return "Email sent successfully";
	}
	
	/**
	 * sendEmail sends an email to the concerned authority to warn them about wash trade scenarios detected
	 * @return sent email confirmation
	 * @throws javax.mail.MessagingException 
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	@RequestMapping(value = TradeRestURIConstants.SEND_EMAIL_WASH, method = RequestMethod.GET)
	public String sendEmailWash() throws MessagingException, IOException, javax.mail.MessagingException {
		tradeJDBCTemplate.sendmailWash();
		return "Email sent successfully";
	}
	
	@RequestMapping(value = TradeRestURIConstants.GET_WASH_TRADES, method = RequestMethod.GET)
	public @ResponseBody List<WashTradeScenario> WashTradesDetector() {
		List<TradeForDataGen> tradeList = tradeJDBCTemplate.fetchTradeListForWashTrades();
		DetectWashTrades detector = new DetectWashTrades();
		List<WashTradeScenario> WashTradeList = detector.detectWashTrades(tradeList);
		log.info("Front running trades detected and sent");
		return WashTradeList;
	}
}
