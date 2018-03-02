/**  
 * Class for store temporary stocks info
 * RK_ID: RK_STOCK
 * @author wgz && doobazgx@163.com
 * @date 2010-04-27
 */
package com.lenovo.leos.stocks;

import java.util.HashMap;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

public class StockerEnvelop implements StockConstants {

	public static FetchSynch fetchSynch = new FetchSynch();

	private Context mContext;
	public int currentVolume = 0;

	public static HashMap<String, String> mStockerCodeAndType = new HashMap<String, String>();
	public HashMap<String, String> mStockerName = new HashMap<String, String>();
	public HashMap<String, String> mStockerStype = new HashMap<String, String>();
	public HashMap<String, String> putMStockerCity = new HashMap<String, String>();

	public HashMap<String, String> mStockerZhangdiee = new HashMap<String, String>();
	public HashMap<String, String> mStockerZhangdiefu = new HashMap<String, String>();
	public HashMap<String, String> mStockerZuixin = new HashMap<String, String>();
	public HashMap<String, String> mStockerChengjiaoliang = new HashMap<String, String>();

	public HashMap<String, String> mStockerTimeImage = new HashMap<String, String>();
	public HashMap<String, String> mStockerDayImage = new HashMap<String, String>();
	public HashMap<String, String> mStockerCurrImage = new HashMap<String, String>();

	public HashMap<String, Bitmap> mStockerBitMap   =  new HashMap<String, Bitmap>();

	public String sStockerZuixin = null;
	public String sStockerZuoshou = null;
	public String sStockerJinkai = null;
	public String sStockerZuidi = null;
	public String sStockerZuigao = null;
	public String sStockerZhangdiee = null;
	public String sStockerZhangdiefu = null;
	public String sStockerChengjiaoliang = null;
	public String sStockerChengjiaoe = null;
	public String sOpen = null;

	public String mStockerUpdateTime = null;
	public String mStockerSystemTime = null;

	// internal stock image
	public String mTimeImage = null;
	public String mDayKImage = null;
	public String mWeekKImage = null;
	public String mMonthKImage = null;

	// America stock image
	public String mDayImage = null;
	public String mOneMonthImage = null;
	public String mThreeMonthImage = null;
	public String mSixMonthImage = null;
	public String mOneYearImage = null;

	// fund image
	public String mCurrentImage = null;
	public String mHistoryImage = null;

	public int mImageType = -1;
	public String currentImageUrl = null;
	public boolean isItem = false;

	public long stockInfoTimestamp = -1L;
	public long imageTimestamp = -1L;
	public static boolean noNetworkData = true;

	public int dataFetchResultVisible = -1;

	public StockerEnvelop() {
	}

	public StockerEnvelop(Context context) {
		mContext = context;
	}

	/**
	 * When you click an item searched, this item will be saved into stocker.xml
	 * @author wgz && doobazgx@163.com
	 * @date 2010-04-29
	 * @param stockerCodeAndType  code appends value, such as s0000/1
	 * @return boolean    true ---- this item is saved
	 *                    false --- this item has existed in stocker.xml
	 */
	public synchronized boolean addSelecedStockerValues(String stockerCodeAndType, String stockerName) {
		StringBuffer sbValues = new StringBuffer();
		StringBuffer sbCodeAndTypes = new StringBuffer();

		String nullTag = mContext.getResources().getString(R.string.nullTag);

		// fix bug:164046
		if(stockerCodeAndType == null || stockerCodeAndType.length() == 0) {
			return false;
		}
		stockerCodeAndType = stockerCodeAndType.trim();

		Uri uri = Uri.parse("content://com.lenovo.leos.stocks.stockprovider/person");
		ContentResolver contentResolver = mContext.getContentResolver();
		Cursor temp = contentResolver.query(uri, null, SELECTED_VALUE_XML, null, null);
		String[] frisetname = null;
		if(temp.moveToFirst()) {
			frisetname = temp.getColumnNames();
		}
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < frisetname.length; i++) {
			if(frisetname[i].trim().length() > 0) {
				build.append(frisetname[i].trim()).append(";");
			}
		}
		String selectedCodeAndValues = new String(build).trim();

		Cursor temp1 = contentResolver.query(uri, null, SELECED_CODES_AND_TYPES_XML, null, null);
		String[] CODES_AND_TYPES = null;
		if(temp1.moveToFirst()){
			CODES_AND_TYPES = temp1.getColumnNames();
		}
		StringBuilder build1 = new StringBuilder();
		for(int i = 0; i < CODES_AND_TYPES.length; i++) {
			if(CODES_AND_TYPES[i].trim().length() > 0) {
				build1.append(CODES_AND_TYPES[i].trim()).append(";");
			}
		}
		String selectedCodesAndTypes = new String(build1).trim();

		temp.close();
		temp1.close();

		// While this item is the first one
		if (selectedCodeAndValues == null 
				|| selectedCodeAndValues.trim().length() == 0) {
			sbValues.append(stockerCodeAndType)
					.append(VALUE_VALUE_SEPERATOR)
					.append(stockerName)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUES_SEPERATOR);
			sbCodeAndTypes.append(stockerCodeAndType)
					.append(VALUES_SEPERATOR);

			ContentValues values1 = new ContentValues();
			values1.put("type", SELECTED_VALUE_XML);
			values1.put("content", sbValues.toString().trim());
			contentResolver.insert(uri, values1);

			ContentValues values2 = new ContentValues();
			values2.put("type", SELECED_CODES_AND_TYPES_XML);
			values2.put("content", sbCodeAndTypes.toString().trim());
			contentResolver.insert(uri, values2);

			return true;
		}

		// While there has been other items in the stocker.xml
		if (selectedCodeAndValues.indexOf(stockerCodeAndType) < 0) {
			sbValues.append(stockerCodeAndType)
					.append(VALUE_VALUE_SEPERATOR)
					.append(stockerName)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUE_VALUE_SEPERATOR)
					.append(nullTag)
					.append(VALUES_SEPERATOR)
					.append(selectedCodeAndValues.trim());
			sbCodeAndTypes.append(stockerCodeAndType)
					.append(VALUES_SEPERATOR)
					.append(selectedCodesAndTypes.trim());

			ContentValues values1 = new ContentValues();
			values1.put("type", SELECTED_VALUE_XML);
			values1.put("content", sbValues.toString().trim());
			contentResolver.insert(uri, values1);

			ContentValues values2 = new ContentValues();
			values2.put("type", SELECED_CODES_AND_TYPES_XML);
			values2.put("content", sbCodeAndTypes.toString().trim());
			contentResolver.insert(uri, values2);

			return true;
		}
		return false;
	}

	public synchronized String getMStockerCodeAndType(String mStockerCode) {
		return (String) mStockerCodeAndType.get(mStockerCode);
	}

	public static int getcoun() {
		int count = mStockerCodeAndType.size();

		return count;
	}

	public synchronized void putMStockerCodeAndType(String stockerCode,
			String mStockerCodeAndType) {
		if (!this.mStockerCodeAndType.containsValue(mStockerCodeAndType)
				&& mStockerCodeAndType.trim().length() != 0)
			this.mStockerCodeAndType.put(stockerCode, mStockerCodeAndType);
	}

	public synchronized Bitmap getMStockerBitMap(String stockerCode) {
		return (Bitmap) mStockerBitMap.get(stockerCode);
	}

	public synchronized void putMStockerBitMap(String stockerCode,
			Bitmap bitmap) {
		this.mStockerBitMap.put(stockerCode, bitmap);
	}
	
	public synchronized String getMStockerName(String stockerCode) {
		return (String) mStockerName.get(stockerCode);
	}

	public synchronized void putMStockerName(String stockerCode,
			String stockerName) {
		this.mStockerName.put(stockerCode, stockerName);
	}

	public synchronized String getMStockerStype(String stockerCode) {
		return (String) mStockerStype.get(stockerCode);
	}

	public synchronized void putMStockerStype(String stockerCode,
			String stockerStype) {
		this.mStockerStype.put(stockerCode, stockerStype);
	}
	public synchronized void putMStockerCity(String stockerCode,
			String stockerStype) {
		this.putMStockerCity.put(stockerCode, stockerStype);
	}

	public synchronized String getMStockerCity(String stockerCode) {
		return (String) this.putMStockerCity.get(stockerCode);
	}

	public synchronized String getMStockerZhangdiee(String stockerCode) {
		return (String) this.mStockerZhangdiee.get(stockerCode);
	}

	public synchronized void setMStockerZhangdiee(String stockerCode,
			String stockerZhangdiee) {
		mStockerZhangdiee.put(stockerCode, stockerZhangdiee);
	}

	public synchronized String getMChengjiaoliang(String stockerCode) {
		return (String) this.mStockerChengjiaoliang.get(stockerCode);
	}

	public synchronized void setMChengjiaoliang(String stockerCode,
			String stockerChengjiaoliang) {
		mStockerChengjiaoliang.put(stockerCode, stockerChengjiaoliang);
	}

	public synchronized String getMStockerZhangdiefu(String stockerCode) {
		return (String) this.mStockerZhangdiefu.get(stockerCode);
	}

	public synchronized void setMStockerZhangdiefu(String stockerCode,
			String stockerZhangdiefu) {
		mStockerZhangdiefu.put(stockerCode, stockerZhangdiefu);
	}

	public synchronized String getMStockerZuixin(String stockerCode) {
		return (String) this.mStockerZuixin.get(stockerCode);
	}

	public synchronized void setMStockerZuixin(String stockerCode,
			String stockerZuixin) {
		mStockerZuixin.put(stockerCode, stockerZuixin);
	}

	public synchronized String getMStockerTimeImage(String stockerCode) {
		return (String) this.mStockerTimeImage.get(stockerCode);
	}

	public synchronized void setMStockerTimeImage(String stockerCode,
			String stockerTimeImage) {
		mStockerTimeImage.put(stockerCode, stockerTimeImage);
	}

	public synchronized String getMStockerDayImage(String stockerCode) {
		return (String) this.mStockerDayImage.get(stockerCode);
	}

	public synchronized void setMStockerDayImage(String stockerCode,
			String stockerCurrImage) {
		mStockerDayImage.put(stockerCode, stockerCurrImage);
	}

	public synchronized String getMStockerCurrImage(String stockerCode) {
		return (String) this.mStockerCurrImage.get(stockerCode);
	}

	public synchronized void setMStockerCurrImage(String stockerCode,
			String stockerCurrImage) {
		mStockerCurrImage.put(stockerCode, stockerCurrImage);
	}

	/**
	 * To clear temporary info for the next item's info
	 * @author doobazgx@163.com
	 * @date 2010-04-29	
	 */
	public synchronized void clearTempInfo() {
		sStockerZuixin = null;
		sStockerZuoshou = null;
		sStockerJinkai = null;
		sStockerZuidi = null;
		sStockerZuigao = null;
		sStockerZhangdiee = null;
		sStockerZhangdiefu = null;
		sStockerChengjiaoliang = null;
		sStockerChengjiaoe = null;
		sOpen = null;

		// internal stock image
		mTimeImage = null;
		mDayKImage = null;
		mWeekKImage = null;
		mMonthKImage = null;

		// America stock image
		mDayImage = null;
		mOneMonthImage = null;
		mThreeMonthImage = null;
		mSixMonthImage = null;
		mOneYearImage = null;

		// fund image
		mCurrentImage = null;
		mHistoryImage = null;
		
		mImageType = -1;
		currentImageUrl = null;
	}
}
