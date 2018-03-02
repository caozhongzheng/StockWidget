package com.lenovo.leos.stocks;

import android.net.Uri;



public interface StockConstants {

	public static final boolean DEBUG = true;
	public static final String STOCK_TAG = "RK_STOCK";
	
	public static final String PREFERENCES = "newstock";
	public static final String SELECTED_VALUE_XML = "selecedStockerCodeAndValue";
	public static final String SELECTED_VALUE_DRAG_XML = "selecedStockerCodeAndValue_drag";
	public static final String SELECTED_COLUMN = "selectedStockerColume";
	public static final String SELECED_CODES_AND_TYPES_XML = "selectedCodesAndTypes";
	public static final String FIRST_SELECED_CODES_AND_TYPES_XML = "first_selectedCodesAndTypes";
	public static final String SAMLL_SELECED_CODES_AND_TYPES_XML = "small_selectedCodesAndTypes";
	public static final String FIRST_SELECED_CODES_AND_INFO_XML = "first_info_selectedCodesAndTypes";
	public static final String FITCH_STOCK_INFO="com.lenovo.leos.stocks.fetchstockinfo";
	//Be Used for sorting stocks	
	public static final int INVALID_INT = -1;
	public static final int COL_CODE = 0;
	public static final int COL_TYPE = 1;
	public static final int COL_NAME = 2;
	public static final int COL_ZUIXIN = 3;
	public static final int COL_ZHANGDIEE = 4;
	public static final int COL_ZHANGDIEFU = 5;
	public static final int COL_CHENGJIAOLIANG = 6;
	
	public static final int PORTRAIT = 1;
	public static final int LANDSCAPE = 2;
	
	public static final String VALUES_SEPERATOR = ";";
	public static final String VALUE_VALUE_SEPERATOR = "/";	
	public static final String CODE_TYPE_SEPERATOR = "-";
	public static final String CODE_TYPE_NILL = "--";
	
	public static final String DEFAULT_CODE="sh000001/1";
	public static final int LIST_ITEM_ANIMATION = 0;
	public static final int ITEM_LIST_ANIMATION = 1;	
	public static final int SHOW_GN_TIMES_IMAGE = 12;
	public static final int SHOW_GN_DAY_K_IMAGE = 13;
	public static final int SHOW_GN_WEEK_K_IMAGE = 14;
	public static final int SHOW_GN_MONTH_K_IMAGE = 15;
	public static final int SHOW_US_ONE_DAY_IMAGE = 2;
	public static final int SHOW_US_ONE_MONTH_IMAGE = 3;
	public static final int SHOW_US_THREE_MONTH_IMAGE = 4;
	public static final int SHOW_US_ONE_YEAR_IMAGE = 6;
	public static final int SHOW_FUND_CURRENT_IMAGE = 16;
	public static final int SHOW_FUND_HISTORY_IMAGE = 17;
	public static final int REFRESH_STOCK_LIST = 20;
	public static final int ENABLED_BACK_BUTTON = 21;
	
	public static final int TYPE_GN = 0;
	public static final int TYPE_US = 1;
	public static final int TYPE_FUND = 2;
	
	public static final int SAVE_OK = 0;
	public static final int DONNOT_SAVE = 1;
	public static final int GETTING_FAILED = 2;
	
	public static final String ITEM_ENABLED = "itemEnabled";
	public static final int ITEM_ENABLED_TRUE = 1;
	public static final int ITEM_ENABLED_FALSE = 0;
	
	public static final Uri URI = Uri.parse("content://com.lenovo.leos.stocks.stockprovider/person");

	public static final String URL = "http://3g.sina.com.cn/interface/f/stock/common/stock_info.php?wm=b012&cpage=1&ch=stock_info&s=";
    public static String kStockerSearchUrl = "http://data.3g.sina.com.cn/oem/dynamic.php?wm=b012&ch=stock_search&word=";
	
    public static final String INTENT_NAME = "com.lenovo.leos.lenovo_widget_intent";
    
    public static final String INTENT_EXTRA_NAME = "extra_function_name";
	
    public static final String INTENT_FETCHS="com.alarm.fetch.updateinfocomplete_items";
	
    public static final String INTENT_DELET="com.alarm.delet.updateinfocomplete_item";
	
    public static final String STANDBY="com.alarm.fetch.update_items";
    
	public static final String INTENT_FINISH="com.android.widget.finish";
	// The Launcher loaded a widget
//	public static final String ADD_OTHER_WIDGET = "com.lenovo.leos.stocks.addWidget";
	
	// The Launcher removed a widget
//	public static final String DELETE_OTHER_WIDGET = "com.lenovo.leos.stocks.deleteWidget";
    public static final String GETWIDGETID="com.lenovo.leos.lenovo_getwidget_id_intent";
    public static final String CODETYPE="type";
    public static final String CONRENT="content";
    public static final String INITSTOCK="initStocktListView";
	// Fetching a image succeeded
	public static final String UPDATE_COMPLETE = "com.alarm.test.updateinfocomplete";
	
	
	public static final String UPDATE_SAVE_COMPLETE = "com.alarm.save.updateinfocomplete_item";
//	public static final String UPDATE_ITEM_COMPLETE = "com.alarm.test.updateinfocomplete_item";
    // Fetching stock info failed
	public static final String CONNECTIVITY_CHANGE ="android.net.conn.CONNECTIVITY_CHANGE";
//	public static final String SAVE_SELECTED_VALUE_XML="com.alarm.test.save_selectedvalue";
	public static final String SAVE_SELECTED_TYPES_XML="com.alarm.test.save_selectedtypes";
	public static final String APPWIDGET_UPDATE="android.appwidget.action.APPWIDGET_UPDATE";
	public static final String STOCKWIDGET_UPDATE="com.lenovo.leos.stocks.action.APPWIDGET_UPDATE";
	
//	public static final String UPDATE_FAILURE = "com.alarm.test.updateinfofailure";
	
	// Fetching a image failed
	public static final String UPDATE_IMAGE_FAILURE = "com.alarm.test.updateimagefailure";
	
	// When click a list item in the first list view, show the details of it.
	public static final String FETCH_STOCKER = "com.alarm.test.fetchinfocomplete";
	
	// Refresh the first list view
	public static final String UPDATE_STOCKER = "com.alarm.test.updatestockerinfo";
	
	// Refresh the item view
	public static final String REFRESH_STOCKER = "com.alarm.test.refreshstockerinfo";
	
	// When click the back button of the item view
	public static final String BACK_VIEW = "com.alarm.test.backview";
	
	// Refresh the settings view
	public static final String UPDATE_SETTINGS_VIEW = "com.alarm.test.updateliststockerinfo";
	public static final String UPDATE_SETTINGS_OK="com.alarm.test.updateliststockerinfo_ok";
	
	
	// When click any button of the item view with the "US" type
	public static final String SHOW_DAY_IMAGE = "com.alarm.test.showdayimage";
	public static final String SHOW_MONTH_IMAGE = "com.alarm.test.showmonthimage";
	public static final String SHOW_THREEMONTH_IMAGE = "com.alarm.test.showthreemonthimage";
	public static final String SHOW_SIXMONTH_IMAGE = "com.alarm.test.showsixmonthimage";
	public static final String SHOW_ONEYEAR_IMAGE = "com.alarm.test.showoneyearimage";
	
	// When click any button of the item view with the "GN" type
	public static final String SHOW_TIMES_IMAGE = "com.alarm.test.showtimesimage";
	public static final String SHOW_DAYK_IMAGE = "com.alarm.test.showdayKimage";
	public static final String SHOW_WEEKK_IMAGE = "com.alarm.test.showweekkimage";
	public static final String SHOW_MONTHK_IMAGE = "com.alarm.test.showmonthKimage";
	
	// When click any button of the item view with the "FUND" type
	public static final String SHOW_CURR_IMAGE = "com.alarm.test.showcurrimage";
	public static final String SHOW_HISTORY_IMAGE = "com.alarm.test.showhistoryimage";
	
	// When drag the widget, ...
 	public static final String  ACTION_WIDGET_SHOW = "com.lenovo.launcher.pad.UPDATE_WIDGET_STOCK";
	
	// change orientation
	public static final String ACTION_DOCKING_EXIST = "android.intent.action.docking.exist";
	public static final String DOCKING_EXIST_KEY = "exist";
		
	public static final int RET_SUCCESS = 200;
	
	public static final int TIMEOUT = 5000;
	
	public static final int READ_IMAGE_TIMEOUT = 8000;
	
	public static final String INVALID_VALUE = "0";
	
	public static final String INVALID_RATIO = "100%";
	
	// Stock status: open
	public static final String STATUS_OPEN = "1";
	// Stock status: suspension
	public static final String STATUS_SUSPENSION = "2";
	
	public static final String STOCK_TYPE_US = "us";
	public static final String STOCK_TYPE_GN = "gn";
	public static final String STOCK_TYPE_HK = "hk";
	public static final String STOCK_TYPE_FUND = "fund";
	
	public static final int SELECTED_COLUMN_RATIO = 0;
	public static final int SELECTED_COLUMN_CHANGE = 1;
	public static final int SELECTED_COLUMN_VOLUME = 2;
	
	public static final int MAX_CONNECTION_NUM = 15;
	
	public static final Object[][] SPECIFIED_STOCKS = {
		{"sh000001/1", R.string.sh},
		{"sz399001/1", R.string.sz},
		{"00992/5", R.string.lenovo}};
	
	public static final String Keep="sh000001/1;sz399001/1;";
	public static final long TIMESTAMP_FETCH_ALL = -1L;
	public static final String FRISTSTOCK="sh000001/1";
	public static final String DEFULTSTRING="上证指数;--;--;--;--;--;sh000001/1";
	public static final String WIDGETID="widgetid";
	public static final String DBSTOCK="stock";
	public static final String SH="sh000001";
	public static final String NULLSTRING="";
	
}
