/**  
 * Fetch network time
 * RK_ID: RK_STOCK
 * @author wgz
 * @date 2010-04-27
 */
package com.lenovo.leos.stocks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class FetchSystemTime extends Thread implements StockConstants {

	private static Context mContext;

	/**
	 * The post string
	 */
	private static final String POST_STR =
		"api_key=60bb4e103a0e425aaba000e60e5e000d&v=1.0&user=junju96@sina.com&password=f74a10e1d6b2f32a47b8bcb53dac5345&format=XML";

	private static final String URL = 
		"http://3g.sina.com.cn/interface/f/stock/common/stock_info.php?wm=b012&cpage=1&ch=stock_info&s=sh000001-1";

	public FetchSystemTime(Context context) {
		mContext = context;
	}

	public void run() {
		setStockerSystemTime();
	}

	/**
	 * To fetch network time and save it
	 * @author wgz
	 * @date 2010-04-29	 * 
	 */
	public static void setStockerSystemTime() {
		try {
			URL url = new URL(URL);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection.setDoInput(true);
			mHttpURLConnection.setDoOutput(true);
			mHttpURLConnection.setRequestProperty("Content-length", String
					.valueOf(POST_STR.length()));
			mHttpURLConnection.setRequestProperty("Content-Type", "text/html");
			mHttpURLConnection.setConnectTimeout(TIMEOUT);
			Log.i("Time",mHttpURLConnection.getHeaderField(0)+"-0-");
			Log.i("Time",mHttpURLConnection.getHeaderField(1)+"-1-");
			Log.i("Time",mHttpURLConnection.getHeaderField(2)+"-2-");
			Log.i("Time",mHttpURLConnection.getHeaderField(3)+"-3-");
			Log.i("Time",mHttpURLConnection.getHeaderField(4)+"-4-");
			Log.i("Time",mHttpURLConnection.getHeaderField(5)+"-5-");
			Log.i("Time",mHttpURLConnection.getHeaderField(6)+"-6-");
			Log.i("Time",mHttpURLConnection.getHeaderField(7)+"-7-");
			Log.i("Time",mHttpURLConnection.getHeaderField(8)+"-8-");
			Log.i("Time",mHttpURLConnection.getHeaderField(9)+"-9-");
			StockLargeViewLand.stockerEnvelop.mStockerSystemTime = Utils.formatTime(new Date(mHttpURLConnection.getHeaderField(1)),mContext);
			StockLargeViewPort.stockerEnvelop.mStockerSystemTime = Utils.formatTime(new Date(mHttpURLConnection.getHeaderField(1)),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}