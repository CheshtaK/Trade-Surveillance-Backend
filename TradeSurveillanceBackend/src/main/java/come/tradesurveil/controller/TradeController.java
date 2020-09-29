package come.tradesurveil.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tradesurveil.dao.TradeJDBCTemplate;
import com.tradesurveil.bean.TradeForDataGen;

@RestController
public class TradeController {
		TradeJDBCTemplate tradeJDBCTemplate = new TradeJDBCTemplate();
		
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
		public @ResponseBody List<TradeForDataGen> frontRunningTradesDetector() {
			List<TradeForDataGen> frontRunningTradeList = new ArrayList<>();
			return frontRunningTradeList;
		}
}
