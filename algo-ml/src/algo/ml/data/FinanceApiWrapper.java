package algo.ml.data;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class FinanceApiWrapper {
	
	public static List<HistoricalQuote> getHistoryFromYahooFinanceAPI(String symbol, Calendar from, Calendar to, Interval interval) {
		
		try {
			return YahooFinance.get(symbol).getHistory(from, to, interval);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static double[][] getMatrix(String symbol, Calendar from, Calendar to, Interval interval) {
		
		List<HistoricalQuote> hqs = getHistoryFromYahooFinanceAPI(symbol, from, to, interval);
		
		//two columns (date as x, adj close as y)
		double[][] matrix = new double[hqs.size()][2];
		
		for (int i = 0; i < hqs.size(); i++) {
			
			HistoricalQuote hq = hqs.get(i);
			double x = hq.getDate().getTimeInMillis();
			double y = hq.getAdjClose().doubleValue();
			matrix[i][0] = x;
			matrix[i][1] = y;
			
		}
		
		return matrix;
	}

}
