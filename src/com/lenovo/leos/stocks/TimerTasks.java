package com.lenovo.leos.stocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class TimerTasks extends Thread  implements StockConstants, Serializable {

	static final String TAG = "TimerTasks" ;
	private int widgetid;
	private Context context;
	private RemoteViews views;
	private Boolean Status = true;
	private String FirstStock = null;
	public long time = 60000*10;
	Map<String, items> map = new HashMap<String, items>();
	private Map<String,String> type = new HashMap<String,String>();
	SharedPreferences preferences;

	public TimerTasks(Context mContext, int id, RemoteViews view) {
		super();
		preferences = mContext.getSharedPreferences(PREFERENCES,Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
		FirstStock = null;
		views = view;
		widgetid=id;
		this.context=mContext;
		DBOpenHelper	dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query(DBSTOCK, null, "widgetid=?", new String[]{id+NULLSTRING}, null, null, null);
		if(cursor.moveToFirst()) {
			FirstStock= cursor.getString(cursor.getColumnIndex("stockid"));
			Log.i(TAG, "1=="+FirstStock);
		}
		if(FirstStock==null){
			SharedPreferences	preferences = context.getSharedPreferences(PREFERENCES,Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
			String code=preferences.getString("deletstockcode",NULLSTRING);
			String codes=preferences.getString(SELECED_CODES_AND_TYPES_XML,NULLSTRING);
			Log.i(TAG, "2=="+code+"  codes="+codes);
			if(code!=null&&code.length()>0) {
				FirstStock=code.split(VALUES_SEPERATOR)[6];
				if(!codes.contains(FirstStock)) {
					FirstStock = DEFAULT_CODE;
				}
			} else {
				FirstStock=DEFAULT_CODE;
			}
			db.delete(DBSTOCK, "widgetid=?", new String[]{id+NULLSTRING.trim().toString()});
			ContentValues values = new ContentValues();
			values.put("stockid", FirstStock);
			values.put(WIDGETID, id);
			db.insert(DBSTOCK, null, values);
		}
		Log.i(TAG, "4=="+FirstStock);
		type.put(widgetid+NULLSTRING, FirstStock);
		cursor.close();
		db.close();
	}

	public TimerTasks(Context context, int id, int length) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	public synchronized void SmallonUpdateViews(Context context,
			AppWidgetManager appWidgetManager,items item, int id) {

		String name= item.getName();
		if(item.getName()==null){
			item.setName(CODE_TYPE_NILL);
		}
		if(item.getZhangdiefu()==null){
			item.setZhangdiefu(CODE_TYPE_NILL);
		}
		if(item.getZuixin()==null){
			item.setZuixin(CODE_TYPE_NILL);
		}
		if(item.getCity()==null){
			item.setCity(CODE_TYPE_NILL);
		}
		if(item.getZhangdiee()==null){
			item.setZhangdiee(CODE_TYPE_NILL);
		}
		if(item.getChengjiaoliang()==null){
			item.setChengjiaoliang(CODE_TYPE_NILL);
		}
		String codeCity = (FirstStock.split(VALUE_VALUE_SEPERATOR)[COL_CODE]+"."+item.getCity()).toUpperCase();
		views.setTextViewText(R.id.smallName, item.getName());
		views.setTextViewText(R.id.smallstockerCode, codeCity);
		views.setTextViewText(R.id.smallstockerZuiXinJG, item.getZuixin());
		String column = null;
		Uri uri = Uri.parse("content://com.lenovo.leos.stocks.stockprovider/person");
		Cursor cursor = context.getContentResolver().query(uri, null, SELECTED_COLUMN, null, null);
		if(cursor.moveToFirst()){
			column = cursor.getColumnNames()[0];
		}
		cursor.close();
		int col = Integer.parseInt(column);
		String st= col==SELECTED_COLUMN_RATIO?item.getZhangdiefu(): col==SELECTED_COLUMN_CHANGE?item.getZhangdiee():item.getChengjiaoliang();
		if(col != SELECTED_COLUMN_VOLUME){
			if((!st.contains("+"))&&(!st.contains(CODE_TYPE_SEPERATOR))){
				st="+"+st;
			}
		}
		
		if(st.contains("+")) {
			views.setTextViewText(R.id.smallValue_z, st);
			views.setViewVisibility(R.id.smallValue_z, View.VISIBLE);
			views.setViewVisibility(R.id.smallValue_d, View.GONE);
			views.setImageViewResource(R.id.smallbackground, R.drawable.stock_small_background_zhang);
		} else {
			views.setTextViewText(R.id.smallValue_d, st);
			views.setViewVisibility(R.id.smallValue_d, View.VISIBLE);
			views.setViewVisibility(R.id.smallValue_z, View.GONE);
			views.setImageViewResource(R.id.smallbackground, R.drawable.stock_small_background_die);
		}
		appWidgetManager.updateAppWidget(id,views);
	}

	@Override
	public void run() {
		while(Status){
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			//    		boolean screenOn = pm.isScreenOn();
			//    		if(screenOn){
			String selectedCodeAndValue = FirstStock.replace(VALUE_VALUE_SEPERATOR, CODE_TYPE_SEPERATOR);
			boolean parseResult = false;
			boolean putTask = false;
			int responseCode = 0;
			String stockName = null;
			InputStream inputStream = null;

			String urlString = URL + Utils.replaceBlank(selectedCodeAndValue);
			try {
				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, TIMEOUT);
				//HttpConnectionParams.setSocketBufferSize(params, BUFFER_SIZE);
				HttpClient httpClient = new DefaultHttpClient(params);
				HttpGet httpGet = new HttpGet(urlString);
				HttpResponse response=null;
				response = httpClient.execute(httpGet);
				if(response.getStatusLine().getStatusCode()==200){
					HttpEntity entity = response.getEntity();
					InputStream instream = entity.getContent();
					//					byte [] B=	StreamTool.readStream(instream);
					//					  Log.i("===v===",  new String(B));
					boolean Successful=parseStocker(instream,widgetid);
					items item= map.get(widgetid+NULLSTRING);
					if(Successful&&item!=null){
						StringBuilder build = new StringBuilder();
						build.append(item.getName()+VALUES_SEPERATOR
								+item.getCity()+VALUES_SEPERATOR
								+item.getZuixin()+VALUES_SEPERATOR
								+item.getZhangdiefu()+VALUES_SEPERATOR
								+item.getZhangdiee()+VALUES_SEPERATOR
								+item.getChengjiaoliang()+VALUES_SEPERATOR
								+FirstStock);
						//往newstock.xml中追加本widget
						if(preferences != null){
							preferences.edit().putString(widgetid+NULLSTRING, new String(build)).commit();
						}
						Log.i(TAG, "5== "+new String(build));
						AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
						SmallonUpdateViews(context,appWidgetManager,item,widgetid);
					}
					Thread.sleep(time);
				}
			} catch (HttpHostConnectException e) {
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				String code=preferences.getString(widgetid+NULLSTRING, NULLSTRING);
				if(code!=null&&code.length()>0) {
					String[] codes=code.split(VALUES_SEPERATOR);
					SmallonUpdateViews(context,appWidgetManager,new items(codes[0], codes[1], codes[2],codes[3], codes[4], codes[5]),widgetid);
				}
				try {
					Thread.sleep(2000*10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					boolean network=getNetConnectState(context);
					if(!network){
						Status=false;
						LenovoLeosStockWidgetProvier.TaskMap.remove(widgetid);
					}
					e.printStackTrace();
				} catch (Exception e) {
					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
					String code=preferences.getString(widgetid+NULLSTRING, NULLSTRING);
					if(code!=null&&code.length()>0){
						String[] codes=code.split(VALUES_SEPERATOR);
						SmallonUpdateViews(context,appWidgetManager,new items(codes[0], codes[1], codes[2],codes[3], codes[4], codes[5]),widgetid);
					}
					try {
						Thread.sleep(2000*10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

	public void setTime(long tm){
		time=tm;
	}
	public void setStatues(boolean o){
		Status=o;
	}
	public Boolean getStatues(){
		return Status;
	}
	public boolean getNetConnectState(Context context) {
		boolean netConnect = false;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo infoM = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if ((info != null && info.isConnected())|| (infoM != null && infoM.isConnected())) {
			netConnect = true;
		}

		return netConnect;
	}
	boolean parseStocker(InputStream mInputStream, int widgetid) {
		if (null == mInputStream)
			return false;
		String mStockerCode = null;
		String mStockerName = null;
		String mStockerCity = null;
		String mStockerZuixin = null;
		String mStokcerZhangdiee = null;
		String mStockerZhangdiefu = null;
		String mStockerChengjiaoliang = null;
		String zuixin = null;
		String tmp = null;
		String formatStr = null;
		boolean parseRet = false;
		boolean putTask = false;
		String nullTag = context.getResources().getString(R.string.nullTag);
		StringBuffer mStockerValues = new StringBuffer();
		String stype = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(mInputStream, null);
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String tag = xpp.getName();
					if (tag.equals(DBSTOCK)) {

					} else if (tag.equals("name")) {
						xpp.next();
						mStockerName = xpp.getText();
						mStockerName = mStockerName == null ? mStockerCode : mStockerName.trim().replace(" ", NULLSTRING);
					} else if (tag.equals("zuixin")) {
						xpp.next();
						zuixin = xpp.getText() == null ? null : xpp.getText().trim();
						if (zuixin != null && !INVALID_VALUE.equals(zuixin)) {
							formatStr =  Utils.formatDoubleSize(zuixin);
						} else {
							formatStr = nullTag;
						}
						mStockerZuixin = formatStr;
					} else if (tag.equals("city")) {
						xpp.next();
						mStockerCity = xpp.getText();
						mStockerCity = mStockerCity == null ? mStockerCode : mStockerCity.trim().replace(" ", NULLSTRING);
					} else if (tag.equals("stype")) {//not city
						xpp.next();
						tmp = xpp.getText();
						if((tmp!=null && !tmp.equalsIgnoreCase("gn")) || mStockerCity == null){
							mStockerCity = tmp;
							mStockerCity = mStockerCity == null ? mStockerCode : mStockerCity.trim().replace(" ", NULLSTRING);
						}
					} else if (tag.equals("zhangdiee")) {
						xpp.next();
						tmp = xpp.getText() == null ? null : xpp.getText().trim();
						if (!(zuixin != null && zuixin.trim().equals(INVALID_VALUE)) && tmp != null && !INVALID_VALUE.equals(tmp) ) {
							formatStr =  Utils.formatDoubleSize(tmp);
						} else {
							formatStr = nullTag;
						}
						mStokcerZhangdiee = formatStr;
					} else if (tag.equals("zhangdiefu")) {
						xpp.next();
						tmp = xpp.getText() == null ? null : xpp.getText().trim();
						if (!(zuixin != null && zuixin.trim().equals(INVALID_VALUE)) && tmp != null && !INVALID_VALUE.equals(tmp)) {
							formatStr =  tmp;
						} else {
							formatStr = nullTag;
						}
						mStockerZhangdiefu = formatStr;
					} else if (tag.equals("chengjiaoliang")) {
						xpp.next();
						tmp = xpp.getText() == null ? null : xpp.getText().trim();
						if (!(zuixin != null && zuixin.equals(INVALID_VALUE)) && tmp != null && !INVALID_VALUE.equals(tmp)) {
							formatStr =  Utils.formatSize(tmp);
						} else {
							formatStr = nullTag;
						}
						StockLargeViewLand.stockerEnvelop.setMChengjiaoliang(mStockerCode, formatStr);
						StockLargeViewPort.stockerEnvelop.setMChengjiaoliang(mStockerCode, formatStr);
						mStockerChengjiaoliang = formatStr;
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					// We have a comlete item -- post it back to the UI
					// using the mHandler (necessary because we are not
					// running on the UI thread).
					String tag = xpp.getName();
					if (tag.equals(DBSTOCK)) {
						// Save the stock's info
						map.put(widgetid+NULLSTRING, new items(mStockerName,mStockerCity,mStockerZuixin,mStockerZhangdiefu,mStokcerZhangdiee,mStockerChengjiaoliang));
						mStockerValues = new StringBuffer();
						parseRet = true;
					}
				}
				eventType = xpp.next();
			}

			return parseRet;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
