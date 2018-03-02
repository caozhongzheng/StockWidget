/**  
 * To save tasks and gained values
 * RK_ID: RK_STOCK
 * @author wgz && doobazgx@163.com
 * @date 2010-04-27
 */
package com.lenovo.leos.stocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class FetchSynch implements StockConstants {
	// tasks list
	public HashMap<String, Boolean> fetchTasks = new HashMap<String, Boolean>();
	
	// task data for values
	public HashMap<String,String> taskDatas = new HashMap<String, String>();	
	
	// failed tasks
	public List<String> fetchFailure = new ArrayList<String>();

    // save Names
    private HashMap<String, String> stockNames = new HashMap<String, String>();

    /**
     * Whether the name has been gotten
     * @param key  The stock code, such as sh000001
     * @return  boolean  Whether the stock name exists
     */
    public boolean hasStockName(String key) {
        return stockNames.containsKey(key);
    }    

    /**
     * Save the stock's name according to the stock's code
     * @param key The stock code, such as sh000001
     * @param name The stock name
     */
    public void saveStockName(String key, String name) {
        if (key == null || name == null) {
            return;
        }
        if (hasStockName(key)) {
            stockNames.remove(key);
        }    
        stockNames.put(key, name);
    }    

    /**
     * Get the stock name according to the stock code
     * @param key The stock code, such as sh000001
     * @return String  The stock name
     */
    public String getStockName(String key) {
        return stockNames.get(key);
    } 

    /**
     * remove the stock name  according to the stock code
     * @param key The stock code, such as sh000001
     */
    public void removeStockName(String key) {
        if (hasStockName(key)) {
             stockNames.remove(key);
        }
    }     
        
	/**
	 * To save tasks
	 * @author wgz && doobazgx@163.com
	 * @date 2010-04-29
	 * @param task  such as s0000/1
	 * @param value whether the task is over
	 * @return boolean    true ---- saving succeeded
	 *                    false --- saving failed
	 */
	public synchronized boolean putFetchTask(String task, boolean value) {
		if (task == null) {
			return false;
		}
		if (value && !fetchTasks.containsKey(task)) {
			return false;
		}
		fetchTasks.put(task, value);
		return true;
	}
	
	/**
	 * To save list item values
	 * @author wgz
	 * @date 2010-04-29
	 * @param task      code appends type, such as s0000/1
	 * @param taskData  a list item value, such as s0000/1/XXX/....
	 */
	public synchronized void putTaskData(String task, String taskData) {
		this.taskDatas.put(task, taskData);
	}
		
	public synchronized void putFetchFailure(String task) {
		this.fetchFailure.add(task);
	}
	
	/**
	 * Save data into stocker.xml
	 * @author wgz && doobazgx@163.com
	 * @date 2010-04-29
	 * @param context
	 * @return int  SAVE_OK ---- OK   
	 *              DONNOT_SAVE ---- don't refresh 
	 *              GETTING_FAILED ---- getting data failed
	 */
	public synchronized int finishTask(Context context) {
		String key;
		int ret = SAVE_OK;
		String[] codeAndTypes = null;

		Uri uri = Uri.parse("content://com.lenovo.leos.stocks.stockprovider/person");
		ContentResolver contentResolver = context.getContentResolver();
		Cursor ss =	contentResolver.query(uri, null, SELECED_CODES_AND_TYPES_XML, null, null);
		String[] frisetname = null;

		if(ss.moveToFirst()) {
			frisetname = ss.getColumnNames();
		}
		StringBuilder build = new StringBuilder();
		for(int i=0; i<frisetname.length; i++){
			build.append(frisetname[i]).append(";");
		}

		String selectedCodeAndTypes = new String(build);
		codeAndTypes = selectedCodeAndTypes.split(VALUES_SEPERATOR);
		int len = codeAndTypes.length;
		if ("".equals(selectedCodeAndTypes.trim())) {
			return DONNOT_SAVE;
		}

		int taskNum = fetchTasks.size();
		int valueNum = taskDatas.size();;
		int failureNum = fetchFailure.size();
		// Not all stocks info are gained
		if (this.fetchTasks.isEmpty()
				|| taskNum != valueNum
				|| taskNum != len) {
			return DONNOT_SAVE;
		}

		// Fetching data failed
		if (taskNum == failureNum) {
			ret = GETTING_FAILED;
		}
		//verify all task synch over,write xml
		StringBuffer sbStockerValues = new StringBuffer();

		// Check whether all have been completed
		Set<String> tasks = this.fetchTasks.keySet();
		Iterator<String> iterator = tasks.iterator();
		while (iterator.hasNext()) {
			key = iterator.next();
			if (!this.fetchTasks.get(key)) {
				return DONNOT_SAVE;
			}
		}

		// Get the stocks info and save the string
		for (int i = 0; i < len; i++) {
			String task = codeAndTypes[i].trim();
			if (!"".equals(task) && this.taskDatas.get(task) == null) {
				return DONNOT_SAVE;
			}
			sbStockerValues.append(this.taskDatas.get(task).trim()).append(VALUES_SEPERATOR);
		}
		Log.i(STOCK_TAG, "debug by doobazgx@163.com      finish fetching list and return :: "+ret);

		ContentValues values2 = new ContentValues();
		values2.put("type", SELECTED_VALUE_XML);
		values2.put("content",sbStockerValues.toString());
		contentResolver.insert(uri, values2);

		this.fetchTasks.clear();
		this.taskDatas.clear();
		this.fetchFailure.clear();
		ss.close();

		return ret;
	}
}