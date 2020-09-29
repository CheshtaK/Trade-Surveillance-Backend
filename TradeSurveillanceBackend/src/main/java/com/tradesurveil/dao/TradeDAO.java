/**
 * 
 */
package com.tradesurveil.dao;

import java.util.List;
import javax.sql.DataSource;

import com.tradesurveil.bean.TradeForDataGen;

/**
 * @author Kryselle
 *
 */
public interface TradeDAO {

	public void setDataSource(DataSource ds);
	public void insertTrade(TradeForDataGen trade);
	public List<TradeForDataGen> fetchTradeList();
	public void insertTradeList(List<TradeForDataGen> tradeList);
	public void deleteTrades();
	public List<TradeForDataGen> getTradeList();
	
}
