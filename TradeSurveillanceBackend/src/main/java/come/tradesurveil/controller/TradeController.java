package come.tradesurveil.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tradesurveil.dao.TradeJDBCTemplate;
import com.tradesurveil.bean.FrontRunningScenario;
import com.tradesurveil.bean.TradeForDataGen;
import com.tradesurveil.businesslogic.DetectFrontRunning;

@RestController
public class TradeController {
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		TradeJDBCTemplate tradeJDBCTemplate = (TradeJDBCTemplate)context.getBean("tradeJDBCTemplate");
//		TradeJDBCTemplate tradeJDBCTemplate = new TradeJDBCTemplate();
		
		@RequestMapping(value = TradeRestURIConstants.GET_TRADELIST, method = RequestMethod.GET)
		public @ResponseBody List<TradeForDataGen> getTradeList() {
			List<TradeForDataGen> tradeList = tradeJDBCTemplate.getTradeList();
			return tradeList;
		}
		
		@RequestMapping(value = TradeRestURIConstants.INSERT_TRADE, method = RequestMethod.POST)
		public @ResponseBody void insertTrade(@RequestBody TradeForDataGen trade) {
			tradeJDBCTemplate.insertTrade(trade);
		}
		
		
		@RequestMapping(value = TradeRestURIConstants.GET_FRONT_RUNNING_TRADES, method = RequestMethod.GET)
		public @ResponseBody List<FrontRunningScenario> frontRunningTradesDetector() {
			
			List<TradeForDataGen> tradeList = tradeJDBCTemplate.fetchTradeList();
			DetectFrontRunning detector = new DetectFrontRunning();
			List<FrontRunningScenario> frontRunningTradeList = detector.detectFrontRunning(tradeList);
			return frontRunningTradeList;
		}
}
