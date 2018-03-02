package com.lenovo.leos.stocks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lenovo.leos.stocks.Panel.OnPanelListener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StockLargeViewPort extends StockLargeView implements StockConstants, OnPanelListener {

	public static String TAG = "StockLargeViewPort";
	private static HashMap<Integer, String[]> mHashMap = new HashMap<Integer, String[]>();

	public static ArrayList<CharSequence> codelist = new ArrayList<CharSequence>();//stock_edit_view 这是删除的stock列表stockcode&type
	public static ArrayList<CharSequence> selcodelist=new ArrayList<CharSequence>();// 这是做参照用的stock列表stockcode
//	public static ArrayList<CharSequence> selcodelist_add=new ArrayList<CharSequence>();// 这是添加的stock列表新添加的stockcode
//	public static ArrayList<CharSequence> selcodelist_add_name=new ArrayList<CharSequence>();// 这是添加的stock列表新添加的stockname
//	public static ArrayList<CharSequence> selcodelist_del=new ArrayList<CharSequence>();// 这是添加的stock列表要删除的stockcode
//	public static ArrayList<CharSequence> selcodelist_merge=new ArrayList<CharSequence>();// 这是merge后的stock列表
	public static String Stockname[] = null;
	public static String indexCodeString[] = null;
	public static String Stockcode[] = null;
	public static String Stockzuixin[] = null;
	public static String CodesAndTypes_drag = null;

	public static ProgressBar bar;
	public static String SelectStock = null;
	public static String selectStockcode;
	public static String tempname = null;
	public static String smallstockName = null;
	private String SelectedStockFirst;
	private String[] mTempValues = null;

	public static String mItemStockerCode = null;
	public static StockerEnvelop stockerEnvelop = new StockerEnvelop();

	public static Context mContext;
	static int position = 100;
	public static int INDEX = 1;
	public static int REMIN = 1;
	public static int RefreshId = 12;
	private static int mOrientation = -1;
	public static int WidgetId = 0;

	public static final int FETCHSTOCKER_ID_TIMEOUT = 2;
	private static final int REFRESH_ID = 0x100001;
	private static final int REFRESH_OUTTIME = 3000;

	private StockerWorker mWorker;
	private StockerListAdapter mAdapter;
	public static StockLargeListAdapter mStockLargeListAdapter;
	public static StockeListAdapter ListAdapter;
	private ListView listview;
	private  Handler mTimeoutHandler;

	private TextView mJinkai;
	private TextView mZuigao;
	private TextView mZuidi;
	private TextView mZuoshou;
	private TextView mZuixin;
	private TextView mZhangdiefu;
	private TextView mZhangdiee;
	private TextView curr;
	private TextView history;
	private TextView threemonth;
	private TextView onemonth;
	private TextView oneday;
	private TextView fenshitu;
	private TextView monthk;
	private TextView weekk;
	private TextView oneyear;
	private TextView dayk;
	private TextView mChengjiaoe;
	private TextView mOpen;

	private View stock_list_item_view;
	private View stock_search_view;
	private View stocklist_view;
	private View stock_edit_view;
	private View largeview;
	private View stockerInfo;
	private View viewgroup;
	private View us;
	private View fn;
	private View gn;
	private View stockerItemNamelayout;
	private View search_layout;
	private ImageView stockerKImageView;
	private ImageView Stockquit;
	private ImageView ToStockListviw;
	private ImageView stock_refresh;
	private ImageView Image;
	private ImageView searchImageView;
	private TextView mChengjiaoliang;
	private TextView stockercode;
	private TextView stockerItemName;
	private TextView stockerTopZX;
	private TextView stockerTopZDF;
	private TextView updateStockTime;
	private TextView StockInfo_zhandie_info;
	private TextView StockInfo_chengjiaoliang_info;
	private ProgressBar pb;
	private ListView list;
	private ListView stocklist;
	private ImageView mCompleteBt;
	private TextView mZhangdiefuBt;
	private TextView mChengjiaoliangBt;
	private TextView mZhangdieeBt;
	private ImageView tag;
	private String VALUE_DRAG_XML;
	private ImageView mDelImageView;
	private TextView mStockerNameTextView;
	private TextView mStockerZuixinTextView;
	private TextView mStockerValuesTextView;
	private TextView stockerValues_zhang;
	private TextView stockerValues_die;
	private ProgressBar mProgressBar;
//	private EditText edit;
	private ListViewInterceptor mStockerListView;

	private TextView searchCode;
    private TextView searchName;
    private ImageView searchAdded;
    
	private Panel leftPanel;
	public static QuoteAdaptor mQuotAdaptor = null;

	private static String mSelecedCodeAndValues = NULLSTRING;
	private static boolean mInProgress = true;
	private static  Boolean ANIM = true;
	public  static  Boolean refreshList = false;

	public static Uri URI = StockConstants.URI;
	public static ContentResolver cResource;

	public StockLargeViewPort(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public StockLargeViewPort(Context context, int widgetId) {
		super(context);
		// TODO Auto-generated constructor stub
		cResource = context.getContentResolver();
		mContext = context;
		WidgetId = widgetId;

		init();
		fetchFristInfo(context);
	}

	public StockLargeViewPort(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		cResource = context.getContentResolver();
		mContext = context;
		WidgetId = DialogActivity.WID;

		init();
		fetchFristInfo(context);
	}

	public void setPaddings(int window_width, int window_height, int posx ,int posy) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(window_width, window_height);
		lp.leftMargin = posx;
		lp.topMargin = posy;

		this.addView(largeview, lp);
	}

	public void init() {
		SetInitView();

		mTimeoutHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				//在股票列表中点击涨跌额/涨跌幅/成交量分类查看
				case FETCHSTOCKER_ID_TIMEOUT:
					updateRemoteViews();
				//在股票查询结束后progressbar消失
				case  REFRESH_ID:
					if(bar != null){
						bar.setVisibility(View.GONE);
						search_layout.setVisibility(VISIBLE);
						int count = StockerEnvelop.getcoun();
						if(count == 0){
							Toast.makeText(mContext, R.string.StockNull, 1).show();
						}
					}
				}
			}
		};
	}

	public void clear() {
		Log.d(TAG, "clear Port");
		INDEX = 1;
		REMIN = 1;
		SelectStock = null;
	}

	public void SetInitView() {
		largeview = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.stocklist_widget_large_port, null);

		stock_list_item_view 	= largeview.findViewById(R.id.stock_list_item_view);
		stock_search_view 		= largeview.findViewById(R.id.stock_search_view);
		stocklist_view 			= largeview.findViewById(R.id.stocklist_view);
		stock_edit_view			= largeview.findViewById(R.id.stock_edit_view);
		search_layout                   = largeview.findViewById(R.id.search_layout);

		leftPanel = (Panel) largeview.findViewById(R.id.leftPanel1);
		leftPanel.setOnPanelListener(this);
		leftPanel.setInterpolator(new BackInterpolator(EasingType.OUT, 2));
		stocklist = (ListView) leftPanel.findViewById(R.id.panelContent1);

		viewgroup = (View)largeview.findViewById(R.id.viewgroup);

		stockerItemName = (TextView) largeview.findViewById(R.id.stockerTopName);
		stockercode 	= (TextView) largeview.findViewById(R.id.stockerTopCode);
		stockerTopZX 	= (TextView) largeview.findViewById(R.id.stockerTopZuixin);
		stockerTopZDF 	= (TextView) largeview.findViewById(R.id.stockerTopZdf);
		updateStockTime = (TextView) largeview.findViewById(R.id.updateStockTime);
		mJinkai 		= (TextView) largeview.findViewById(R.id.mJinkai);
		mZuigao 		= (TextView) largeview.findViewById(R.id.mZuigao);
		mZuidi 			= (TextView) largeview.findViewById(R.id.mZuidi);
		mChengjiaoliang = (TextView) largeview.findViewById(R.id.mChengjiaoliang);
		mZuoshou 		= (TextView) largeview.findViewById(R.id.mZuoshou);
		mZuixin 		= (TextView) largeview.findViewById(R.id.mZuixin);
		mZhangdiefu 	= (TextView) largeview.findViewById(R.id.mZhangdiefu);
		mZhangdiee 		= (TextView) largeview.findViewById(R.id.mZhangdiee);
		mChengjiaoe 	= (TextView) largeview.findViewById(R.id.mChengjiaoe);
		mOpen 			= (TextView) largeview.findViewById(R.id.mOpen);
		stockerInfo 	= (View) largeview.findViewById(R.id.stockerInfo);
		stockerInfo.setOnClickListener(new BUttonOnClickListener());
		stockerItemNamelayout = (View) largeview.findViewById(R.id.stockerItemNamelayout);
		stockerKImageView = (ImageView) largeview.findViewById(R.id.stockerKImageView);
		stockerKImageView.setOnClickListener(new BUttonOnClickListener());

		String nullTag = mContext.getResources().getString(R.string.nullTag);
		updateStockTime.setText(mContext.getResources().getString(R.string.timeTitle) + " " + nullTag);

		mJinkai.setText(nullTag);
		mZuigao.setText(nullTag);
		mZuidi.setText(nullTag);
		mChengjiaoliang.setText(nullTag);
		mZuoshou.setText(nullTag);
		mZuixin.setText(nullTag);
		mZhangdiefu.setText(nullTag);
		mZhangdiee.setText(nullTag);
		mChengjiaoe.setText(nullTag);
		mOpen.setText(mContext.getResources().getString(R.string.close));

		us = (View)largeview.findViewById(R.id.us);
		fn = (View)largeview.findViewById(R.id.fn);
		gn = (View)largeview.findViewById(R.id.gn);
		us.setVisibility(GONE);
		fn.setVisibility(GONE);
		gn.setVisibility(GONE);

		int RightViewid = getRefreshId();
		if(RightViewid == SHOW_GN_TIMES_IMAGE) {
			us.setVisibility(GONE);
			fn.setVisibility(GONE);
			gn.setVisibility(VISIBLE);
		} else if(RightViewid == SHOW_US_ONE_DAY_IMAGE) {
			us.setVisibility(VISIBLE);
			fn.setVisibility(GONE);
			gn.setVisibility(GONE);
		} else if(RightViewid == SHOW_FUND_CURRENT_IMAGE) {
			us.setVisibility(GONE);
			fn.setVisibility(VISIBLE);
			gn.setVisibility(GONE);
		} else {
			us.setVisibility(GONE);
			fn.setVisibility(GONE);
			gn.setVisibility(VISIBLE);
		}
//		Stockquit = (ImageView) largeview.findViewById(R.id.stockerItemViewBack);
//		Stockquit.setOnClickListener(new BUttonOnClickListener());
//		ToStockListviw = (ImageView)largeview.findViewById(R.id.stockerInfoFresh);
//		ToStockListviw.setOnClickListener(new BUttonOnClickListener());
//		stock_refresh = (ImageView)largeview.findViewById(R.id.stock_new_refresh);
//		stock_refresh.setOnClickListener(new BUttonOnClickListener());
		ImageView addlist = (ImageView) largeview.findViewById(R.id.stockerADD);
		addlist.setOnClickListener(new BUttonOnClickListener());
		ImageView stock_setting = (ImageView) largeview.findViewById(R.id.stockerSET);
		stock_setting.setOnClickListener(new BUttonOnClickListener());

		fenshitu = (TextView) largeview.findViewById(R.id.fenshitu);
		fenshitu.setOnClickListener(new BUttonOnClickListener()) ;
		dayk = (TextView) largeview.findViewById(R.id.dayk);
		dayk.setOnClickListener(new BUttonOnClickListener());
		weekk = (TextView) largeview.findViewById(R.id.weekk);
		weekk.setOnClickListener(new BUttonOnClickListener()) ;
		monthk = (TextView) largeview.findViewById(R.id.monthk);
		monthk.setOnClickListener(new BUttonOnClickListener()) ;

		oneday = (TextView) largeview.findViewById(R.id.oneday);
		oneday.setOnClickListener(new BUttonOnClickListener()) ;
		onemonth = (TextView) largeview.findViewById(R.id.onemonth);
		onemonth.setOnClickListener(new BUttonOnClickListener()) ;
		threemonth = (TextView) largeview.findViewById(R.id.threemonth);
		threemonth.setOnClickListener(new BUttonOnClickListener()) ;
		oneyear = (TextView) largeview.findViewById(R.id.oneyear);
		oneyear.setOnClickListener(new BUttonOnClickListener()) ;

		curr = (TextView) largeview.findViewById(R.id.curr);
		curr.setOnClickListener(new BUttonOnClickListener()) ;
		history = (TextView) largeview.findViewById(R.id.history);
		history.setOnClickListener(new BUttonOnClickListener()) ;

		Image = (ImageView) largeview.findViewById(R.id.stockerKImageView);
		final Configuration configuration = mContext.getResources().getConfiguration();
		String tmpLang = configuration.locale.getLanguage();

		if (tmpLang.equals("zh")) {
			Image.setImageResource(( R.drawable.erro_cn));
		} else {
			Image.setImageResource((R.drawable.error));
		}

		curr.setBackgroundResource(R.drawable.index_bg_p);
		history.setBackgroundResource(R.drawable.index_bg);
		
		fenshitu.setBackgroundResource(R.drawable.index_bg_p);
		dayk.setBackgroundResource(R.drawable.index_bg);
		weekk.setBackgroundResource(R.drawable.index_bg);
		monthk.setBackgroundResource(R.drawable.index_bg);

		oneday.setBackgroundResource(R.drawable.index_bg_p);
		onemonth.setBackgroundResource(R.drawable.index_bg);
		threemonth.setBackgroundResource(R.drawable.index_bg);
		oneyear.setBackgroundResource(R.drawable.index_bg);

		curr.setTextColor(getResources().getColor(R.color.stock_text_color));
		history.setTextColor(getResources().getColor(R.color.stock_one_text));
		
		fenshitu.setTextColor(getResources().getColor(R.color.stock_text_color));
		dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
		weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
		monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

		oneday.setTextColor(getResources().getColor(R.color.stock_text_color));
		onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
		threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
		oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	}

	private void change2DefView() {
		REMIN = 1;
		INDEX = 1;
		stock_list_item_view.setVisibility(VISIBLE);
		stock_search_view.setVisibility(GONE);
		stocklist_view.setVisibility(GONE);
		stock_edit_view.setVisibility(GONE);
	}
	private void change2SearchView() {
		REMIN = 3;
		stock_list_item_view.setVisibility(GONE);
		stock_search_view.setVisibility(VISIBLE);
		stocklist_view.setVisibility(GONE);
		stock_edit_view.setVisibility(GONE);
	}
	private void change2ListView() {
		REMIN = 2;
		stock_list_item_view.setVisibility(GONE);
		stock_search_view.setVisibility(GONE);
		stocklist_view.setVisibility(VISIBLE);
		stock_edit_view.setVisibility(GONE);
	}
	private void change2EditView() {
		REMIN = 4;
		stock_list_item_view.setVisibility(GONE);
		stock_search_view.setVisibility(GONE);
		stocklist_view.setVisibility(GONE);
		stock_edit_view.setVisibility(VISIBLE);
	}
	//进入二态时获取股票数据并显示信息
	public void fetchFristInfo(Context context){

		change2DefView();
//		largeview.postInvalidate();

		SelectedStockFirst = null;

		if(SelectStock == null) {
			Cursor cs = cResource.query(URI, null, FIRST_SELECED_CODES_AND_TYPES_XML, null, WidgetId+NULLSTRING);
			String[] temp = null;
			if(cs.moveToFirst()) {
				temp = cs.getColumnNames();
			}
			if(temp == null){
				temp[COL_CODE] = DEFAULT_CODE;
			}
			if(codelist != null && codelist.size() > 0) {
				boolean bContain = false;
				for(int i = 0; i < codelist.size(); i++) {
					if(codelist.get(i).toString().trim().equalsIgnoreCase(temp[COL_CODE].trim())) {
						bContain = true;
						break;
					}
				}
				if(bContain) {
					temp[COL_CODE] = DEFAULT_CODE;
				}
			}
			cs.close();

			SelectedStockFirst = SelectStock = temp[COL_CODE];
			onClickItemViewRefreshBtn(SelectedStockFirst);
		} else {
			onClickItemViewRefreshBtn(SelectStock);
		}
		updateStockItem();
	}

	//启动线程FetchStockerInfo，获取股票数据并解析
	private void onClickItemViewRefreshBtn(String stockerCode) {
		StockLargeViewPort.stockerEnvelop.clearTempInfo();
		boolean isConnected = getNetConnectState(mContext);
		if(stockerCode != null) {
			stockerCode = stockerCode.trim();
			mItemStockerCode = stockerCode.split(VALUE_VALUE_SEPERATOR)[COL_CODE];
			if(isConnected) {
				long timeStamp = System.currentTimeMillis();
				StockLargeViewPort.stockerEnvelop.stockInfoTimestamp = timeStamp;
				FetchStockerInfo fetchStockerInfo = new FetchStockerInfo(
						stockerCode,
						timeStamp,
						mContext
						);
				fetchStockerInfo.start();
			}
		}
	}

	public void updateRemoteViews() {
		boolean isConnected = getNetConnectState(mContext);
		if (isConnected) {
			updateStaticStockerInfo();
		} else {
			noNetwork(mContext);
		}
	}

	//获取股票列表数据
	public void onUpdateViews(Context context) {
		boolean isConnected = getNetConnectState(context);
		if (isConnected) {
			startFetchStocker(context);
			updateStaticStockerInfo();
		} else {
			noNetwork(context);
		}
	}

	//获取完毕股票K图之后刷新界面
	public void updataStockItem() {
		updateStockItem(mContext, true);
	}

	//获取股票数据后刷新界面
	public void updateStockItem() {
		int  RightViewid = -1;

		stock_list_item_view.postInvalidate();

		if (mItemStockerCode != null) {
			RightViewid = getRefreshId();

			final Configuration configuration = mContext.getResources().getConfiguration();
			String tmpLang = configuration.locale.getLanguage();
			if (tmpLang.equals("zh")) {
				Image.setImageResource((R.drawable.erro_cn));
			} else {
				Image.setImageResource((R.drawable.error));
			}

			String ImagestockCode = mItemStockerCode.trim();
			Bitmap bitmap = StockLargeViewPort.stockerEnvelop.getMStockerBitMap(ImagestockCode);
			String ImagestockName = StockLargeViewPort.stockerEnvelop.getMStockerName(ImagestockCode);
			if (bitmap != null && ImagestockName != null) {
				Bitmap mBitmap = Bitmap.createScaledBitmap(bitmap, 344, 192, true);
				Image.setImageBitmap( mBitmap);
			}
		}
		String nullTag = mContext.getResources().getString(R.string.nullTag);
		String stockCode = mItemStockerCode;
		String stockName = StockLargeViewPort.stockerEnvelop.getMStockerName(stockCode);
		smallstockName = stockName;
		stockercode.setText(mItemStockerCode==null?nullTag:mItemStockerCode);
		stockerItemName.setText(stockName==null?nullTag:stockName);
		stockerInfo.setOnClickListener(new BUttonOnClickListener());
		stockerKImageView.setOnClickListener(new BUttonOnClickListener());
		updateStockTime.setText(mContext.getResources().getString(R.string.timeTitle) + " " + nullTag);
		mOpen.setText(mContext.getResources().getString(R.string.close));
		mJinkai.setText((StockLargeViewPort.stockerEnvelop.sStockerJinkai==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerJinkai));
		mZuigao.setText((StockLargeViewPort.stockerEnvelop.sStockerZuigao==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuigao));
		mZuidi.setText((StockLargeViewPort.stockerEnvelop.sStockerZuidi==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuidi));
		mChengjiaoliang.setText( (StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang));
		mZuoshou.setText( (StockLargeViewPort.stockerEnvelop.sStockerZuoshou==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuoshou));
		mZuixin.setText((StockLargeViewPort.stockerEnvelop.sStockerZuixin==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuixin));
		stockerTopZX.setText((StockLargeViewPort.stockerEnvelop.sStockerZuixin==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuixin));
		String zdf = StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu;
		if((!zdf.contains("+"))&&(!zdf.contains(CODE_TYPE_SEPERATOR))){
			zdf="+"+zdf;
		}
		mZhangdiefu.setText(zdf);
		stockerTopZDF.setText((zdf));
		if(zdf.equals(nullTag)) {
			stockerTopZDF.setTextColor(getResources().getColor(R.color.stock_one_text));
		} else if(zdf.contains("+")) {
			stockerTopZDF.setTextColor(getResources().getColor(R.color.stock_text_color_zhang));
		} else if(zdf.contains("-")) {
			stockerTopZDF.setTextColor(getResources().getColor(R.color.stock_text_color_die));
		}
		mZhangdiee.setText((StockLargeViewPort.stockerEnvelop.sStockerZhangdiee==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZhangdiee));
		mChengjiaoe.setText((StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe));

		if(mItemStockerCode != null && stockName != null) {
			stockerItemName.setText(stockName);
			String stockTime =  StockLargeViewPort.stockerEnvelop.mStockerUpdateTime;
			if (stockTime == null || NULLSTRING.equals(stockTime.trim())) {
				stockTime = nullTag;
			} else {
				int len = stockTime.lastIndexOf(":");
				if(len != -1) {
					stockTime=stockTime.subSequence(0, len)+NULLSTRING;
				}
			}

			updateStockTime.setText(mContext.getResources().getString(R.string.timeTitle) + " " + stockTime);
			mJinkai.setText((StockLargeViewPort.stockerEnvelop.sStockerJinkai==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerJinkai));
			mZuigao.setText((StockLargeViewPort.stockerEnvelop.sStockerZuigao==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuigao));
			mZuidi.setText((StockLargeViewPort.stockerEnvelop.sStockerZuidi==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuidi));
			mChengjiaoliang.setText( (StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang));
			mZuoshou.setText( (StockLargeViewPort.stockerEnvelop.sStockerZuoshou==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuoshou));
			mZuixin.setText((StockLargeViewPort.stockerEnvelop.sStockerZuixin==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuixin));
			mZhangdiefu.setText((StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu));
			mZhangdiee.setText((StockLargeViewPort.stockerEnvelop.sStockerZhangdiee==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZhangdiee));
			mChengjiaoe.setText((StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe));

			String city = StockLargeViewPort.stockerEnvelop.getMStockerStype(mItemStockerCode);
			if(city != null && city.equals("gn")) {
				String tempcity=StockLargeViewPort.stockerEnvelop.getMStockerCity(mItemStockerCode);
				if(tempcity != null && tempcity.length() > 0) {
					city = tempcity;
				}
				String newcity = mItemStockerCode+"."+city.toUpperCase();
				stockercode.setText(newcity);
			}

			if(StockLargeViewPort.stockerEnvelop.sOpen != null
					&& StockLargeViewPort.stockerEnvelop.sOpen.equals(STATUS_OPEN)) {
				mOpen.setText( mContext.getResources().getString(R.string.open1));
			} else if (StockLargeViewPort.stockerEnvelop.sOpen != null
					&& StockLargeViewPort.stockerEnvelop.sOpen.equals(STATUS_SUSPENSION)) {
				mOpen.setText(mContext.getResources().getString(R.string.stop));
			} else {
				mOpen.setText(mContext.getResources().getString(R.string.close));
			}
		}

		us.setVisibility(GONE);
		fn.setVisibility(GONE);
		gn.setVisibility(GONE);
		if(RightViewid == SHOW_GN_TIMES_IMAGE) {
			us.setVisibility(GONE);
			fn.setVisibility(GONE);
			gn.setVisibility(VISIBLE);
		} else if(RightViewid == SHOW_US_ONE_DAY_IMAGE) {
			us.setVisibility(VISIBLE);
			fn.setVisibility(GONE);
			gn.setVisibility(GONE);
		} else if(RightViewid == SHOW_FUND_CURRENT_IMAGE) {
			us.setVisibility(GONE);
			fn.setVisibility(VISIBLE);
			gn.setVisibility(GONE);
		}

		if(INDEX==1){
            fenshitu.setBackgroundResource(R.drawable.index_bg_p);
            dayk.setBackgroundResource(R.drawable.index_bg);
            weekk.setBackgroundResource(R.drawable.index_bg);
            monthk.setBackgroundResource(R.drawable.index_bg);
            oneday.setBackgroundResource(R.drawable.index_bg_p);
            onemonth.setBackgroundResource(R.drawable.index_bg);
            threemonth.setBackgroundResource(R.drawable.index_bg);
            oneyear.setBackgroundResource(R.drawable.index_bg);

            fenshitu.setTextColor(getResources().getColor(R.color.stock_text_color));
    		dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
    		weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
    		monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

    		oneday.setTextColor(getResources().getColor(R.color.stock_text_color));
    		onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
    		threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
    		oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==2){
	            fenshitu.setBackgroundResource(R.drawable.index_bg);
	            dayk.setBackgroundResource(R.drawable.index_bg_p);
	            weekk.setBackgroundResource(R.drawable.index_bg);
	            monthk.setBackgroundResource(R.drawable.index_bg);
	            oneday.setBackgroundResource(R.drawable.index_bg);
	            onemonth.setBackgroundResource(R.drawable.index_bg_p);
	            threemonth.setBackgroundResource(R.drawable.index_bg);
	            oneyear.setBackgroundResource(R.drawable.index_bg);

	            fenshitu.setTextColor(getResources().getColor(R.color.stock_one_text));
				dayk.setTextColor(getResources().getColor(R.color.stock_text_color));
				weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
				monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

				oneday.setTextColor(getResources().getColor(R.color.stock_one_text));
				onemonth.setTextColor(getResources().getColor(R.color.stock_text_color));
				threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==3){
	            fenshitu.setBackgroundResource(R.drawable.index_bg);
	            dayk.setBackgroundResource(R.drawable.index_bg);
	            weekk.setBackgroundResource(R.drawable.index_bg_p);
	            monthk.setBackgroundResource(R.drawable.index_bg);
	            oneday.setBackgroundResource(R.drawable.index_bg);
	            onemonth.setBackgroundResource(R.drawable.index_bg);
	            threemonth.setBackgroundResource(R.drawable.index_bg_p);
	            oneyear.setBackgroundResource(R.drawable.index_bg);

	            fenshitu.setTextColor(getResources().getColor(R.color.stock_one_text));
				dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
				weekk.setTextColor(getResources().getColor(R.color.stock_text_color));
				monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

				oneday.setTextColor(getResources().getColor(R.color.stock_one_text));
				onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				threemonth.setTextColor(getResources().getColor(R.color.stock_text_color));
				oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==4){
	            fenshitu.setBackgroundResource(R.drawable.index_bg);
	            dayk.setBackgroundResource(R.drawable.index_bg);
	            weekk.setBackgroundResource(R.drawable.index_bg);
	            monthk.setBackgroundResource(R.drawable.index_bg_p);
	            oneday.setBackgroundResource(R.drawable.index_bg);
	            onemonth.setBackgroundResource(R.drawable.index_bg);
	            threemonth.setBackgroundResource(R.drawable.index_bg);
	            oneyear.setBackgroundResource(R.drawable.index_bg_p);
	            
	            fenshitu.setTextColor(getResources().getColor(R.color.stock_one_text));
				dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
				weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
				monthk.setTextColor(getResources().getColor(R.color.stock_text_color));

				oneday.setTextColor(getResources().getColor(R.color.stock_one_text));
				onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				oneyear.setTextColor(getResources().getColor(R.color.stock_text_color));
	    }else if(INDEX==5){
		    	curr.setBackgroundResource(R.drawable.index_bg_p);
		        history.setBackgroundResource(R.drawable.index_bg);
		
		        curr.setTextColor(getResources().getColor(R.color.stock_text_color));
		        history.setTextColor(getResources().getColor(R.color.stock_one_text));
		}else if(INDEX==6){
		        curr.setBackgroundResource(R.drawable.index_bg);
		        history.setBackgroundResource(R.drawable.index_bg_p);
		        
		        history.setTextColor(getResources().getColor(R.color.stock_text_color));
		        curr.setTextColor(getResources().getColor(R.color.stock_one_text));
		}
		//刷新股票名列表listview
		String[] selectindex = null;
		String[] codesAndValues;
		String[] values;
		String[] names = null;
		String selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
		if(selecedStockerCodeAndValues == null || selecedStockerCodeAndValues.length() == 0) {
			initSelValXml();//将默认的三个股票添加到xml中去
			selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
		}
    	if (selecedStockerCodeAndValues != null
    				&& selecedStockerCodeAndValues.length() != 0 ) {
			codesAndValues = selecedStockerCodeAndValues.split(VALUES_SEPERATOR);
			int n = codesAndValues.length;
			names = new String[n];
			selectindex = new String[n];
			for (int i = 0; i < n; i++) {
				if (codesAndValues[i] != null
						&& codesAndValues[i].trim().length() != 0) {
					values = codesAndValues[i].split(VALUE_VALUE_SEPERATOR);
					selectindex[i] = values[COL_CODE].trim();
					names[i] = values[COL_NAME].trim();
					}
			}
    	}
    	indexCodeString = selectindex;

		String indexstring = null;
		int tempcod = 0;
		if(SelectStock == null) {
			Cursor cursor = cResource.query(URI, null, FIRST_SELECED_CODES_AND_TYPES_XML, null, WidgetId+NULLSTRING);
			String[] temp = null;
			if(cursor.moveToFirst()) {
				temp = cursor.getColumnNames();
			}
			if(temp == null) {
				temp[COL_CODE] = DEFAULT_CODE;
			}
			cursor.close();
			indexstring = temp[COL_CODE].split(VALUE_VALUE_SEPERATOR)[0].trim();
		} else {
			indexstring = SelectStock.split(VALUE_VALUE_SEPERATOR)[0].trim();
		}
		Boolean iscodename = false;
		if(indexCodeString != null && indexCodeString.length > 0) {
			for(int i = 0; i < indexCodeString.length; i++) {
//				if(indexstring.contains(indexCodeString[i])) {
				if(indexstring.equalsIgnoreCase(indexCodeString[i].trim())) {
					tempcod = i;
					iscodename = true;
				}
			}
		}
		

		Log.d("zz", "iscodename= "+iscodename +" tempcod= "+tempcod +" mItemStockerCode="+mItemStockerCode +" stockName="+stockName +" indexstring="+indexstring+" names[tempcod]="+names[tempcod]);
		StockLargeViewPort.tempname = names[tempcod];
//		if(mStockLargeListAdapter != null && stocklist != null) {
			if(iscodename) {
				mStockLargeListAdapter = new StockLargeListAdapter(mContext, names, names[tempcod]);
			} else {
				String codeName = "noName";
				mStockLargeListAdapter = new StockLargeListAdapter(mContext, names, codeName);
				
			}
//		}
			stocklist.setAdapter(mStockLargeListAdapter);
			mStockLargeListAdapter.notifyDataSetChanged();
			stocklist.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					startonclik(position);
					leftPanel.setOpen(false, true);
					fetchFristInfo(mContext);
				}
				
			});
	}

	//获取完毕股票K图之后刷新界面
	public void updateStockItem(Context context, boolean updateInfo) {
		int RightViewid = -1;
		viewgroup.postInvalidate();

		if (mItemStockerCode != null) {
			RightViewid = getRefreshId();
			String ImagestockCode = mItemStockerCode.trim();
			final Configuration configuration = context.getResources().getConfiguration();
			String tmpLang = configuration.locale.getLanguage();
			Bitmap bitmap = StockLargeViewPort.stockerEnvelop.getMStockerBitMap(ImagestockCode);
			String ImagestockName = StockLargeViewPort.stockerEnvelop.getMStockerName(ImagestockCode);
			if (tmpLang.equals("zh")) {
				Image.setImageResource(( R.drawable.erro_cn));
			} else {
				Image.setImageResource((R.drawable.error));
			}
			if (bitmap != null && ImagestockName != null) {
				Bitmap mBitmap = Bitmap.createScaledBitmap(bitmap, 344, 192, true);
				Image.setImageBitmap( mBitmap);
			}
		}
		String nullTag = context.getResources().getString(R.string.nullTag);
		String stockCode = mItemStockerCode;
		String stockName = StockLargeViewPort.stockerEnvelop.getMStockerName(stockCode);
		smallstockName = stockName;
		stockercode.setText(mItemStockerCode==null?nullTag:mItemStockerCode);
		stockerItemName.setText(stockName==null?nullTag:stockName);
		if(mItemStockerCode != null && stockName != null) {
			stockerItemName.setText(stockName);
			String stockTime =  StockLargeViewPort.stockerEnvelop.mStockerUpdateTime;
			if (stockTime == null || NULLSTRING.equals(stockTime.trim())) {
				stockTime = nullTag;
			} else {
				int len = stockTime.lastIndexOf(":");
				if(len != -1) {
					stockTime=stockTime.subSequence(0, len)+NULLSTRING;
				}
			}

			updateStockTime.setText(context.getResources().getString(R.string.timeTitle) + " " + stockTime);
			if (updateInfo) {
				mJinkai.setText((StockLargeViewPort.stockerEnvelop.sStockerJinkai==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerJinkai));
				mZuigao.setText((StockLargeViewPort.stockerEnvelop.sStockerZuigao==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuigao));
				mZuidi.setText((StockLargeViewPort.stockerEnvelop.sStockerZuidi==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuidi));
				mChengjiaoliang.setText((StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang));
				mZuoshou.setText((StockLargeViewPort.stockerEnvelop.sStockerZuoshou==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuoshou));
				mZuixin.setText((StockLargeViewPort.stockerEnvelop.sStockerZuixin==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuixin));
				stockerTopZX.setText((StockLargeViewPort.stockerEnvelop.sStockerZuixin==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZuixin));
				String zdf = StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu;
				if((!zdf.contains("+"))&&(!zdf.contains(CODE_TYPE_SEPERATOR))){
					zdf="+"+zdf;
				}
				mZhangdiefu.setText(zdf);
				stockerTopZDF.setText((zdf));
				if(zdf.equals(nullTag)) {
					stockerTopZDF.setTextColor(getResources().getColor(R.color.stock_one_text));
				} else if(zdf.contains("+")) {
					stockerTopZDF.setTextColor(getResources().getColor(R.color.stock_text_color_zhang));
				} else if(zdf.contains("-")) {
					stockerTopZDF.setTextColor(getResources().getColor(R.color.stock_text_color_die));
				}
				mZhangdiee.setText((StockLargeViewPort.stockerEnvelop.sStockerZhangdiee==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerZhangdiee));
				mChengjiaoe.setText((StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe==null?nullTag:StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe));
				String city = StockLargeViewPort.stockerEnvelop.getMStockerStype(mItemStockerCode);

				if(city != null && city.equals("gn")) {
					String tempcity = StockLargeViewPort.stockerEnvelop.getMStockerCity(mItemStockerCode);
					if(tempcity != null && tempcity.length() > 0) {
						city = tempcity;
					}
					String newcity = mItemStockerCode+"."+city.toUpperCase();
					stockercode.setText(newcity);
				}
				String newcity = mItemStockerCode+"."+city.toUpperCase(); ;
				stockercode.setText(newcity);
				if(StockLargeViewPort.stockerEnvelop.sOpen != null
						&& StockLargeViewPort.stockerEnvelop.sOpen.equals(STATUS_OPEN)) {
					mOpen.setText( context.getResources().getString(R.string.open1));
				} else if (StockLargeViewPort.stockerEnvelop.sOpen != null
						&& StockLargeViewPort.stockerEnvelop.sOpen.equals(STATUS_SUSPENSION)) {
					mOpen.setText(context.getResources().getString(R.string.stop));
				} else {
					mOpen.setText(context.getResources().getString(R.string.close));
				}
			}
		}
		if (!updateInfo) {
			stockerItemName.setText(nullTag);
			updateStockTime.setText(nullTag);
			mJinkai.setText(nullTag);
			mZuigao.setText(nullTag);
			mZuidi.setText(nullTag);
			mChengjiaoliang.setText(nullTag);
			mZuoshou.setText(nullTag);
			mZuixin.setText(nullTag);
			mZhangdiefu.setText(nullTag);
			mZhangdiee.setText(nullTag);
			mChengjiaoe.setText(nullTag);
			mOpen.setText(nullTag);
		}
		us.setVisibility(GONE);
		fn.setVisibility(GONE);
		gn.setVisibility(GONE);
		if(RightViewid == SHOW_GN_TIMES_IMAGE){
			us.setVisibility(GONE);
			fn.setVisibility(GONE);
			gn.setVisibility(VISIBLE);
		}else if(RightViewid == SHOW_US_ONE_DAY_IMAGE){
			us.setVisibility(VISIBLE);
			fn.setVisibility(GONE);
			gn.setVisibility(GONE);
		}else if(RightViewid  ==SHOW_FUND_CURRENT_IMAGE){
			us.setVisibility(GONE);
			fn.setVisibility(VISIBLE);
			gn.setVisibility(GONE);
		}

		if(INDEX==1){
            fenshitu.setBackgroundResource(R.drawable.index_bg_p);
            dayk.setBackgroundResource(R.drawable.index_bg);
            weekk.setBackgroundResource(R.drawable.index_bg);
            monthk.setBackgroundResource(R.drawable.index_bg);
            oneday.setBackgroundResource(R.drawable.index_bg_p);
            onemonth.setBackgroundResource(R.drawable.index_bg);
            threemonth.setBackgroundResource(R.drawable.index_bg);
            oneyear.setBackgroundResource(R.drawable.index_bg);

            fenshitu.setTextColor(getResources().getColor(R.color.stock_text_color));
    		dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
    		weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
    		monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

    		oneday.setTextColor(getResources().getColor(R.color.stock_text_color));
    		onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
    		threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
    		oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==2){
	            fenshitu.setBackgroundResource(R.drawable.index_bg);
	            dayk.setBackgroundResource(R.drawable.index_bg_p);
	            weekk.setBackgroundResource(R.drawable.index_bg);
	            monthk.setBackgroundResource(R.drawable.index_bg);
	            oneday.setBackgroundResource(R.drawable.index_bg);
	            onemonth.setBackgroundResource(R.drawable.index_bg_p);
	            threemonth.setBackgroundResource(R.drawable.index_bg);
	            oneyear.setBackgroundResource(R.drawable.index_bg);
	
	            fenshitu.setTextColor(getResources().getColor(R.color.stock_one_text));
				dayk.setTextColor(getResources().getColor(R.color.stock_text_color));
				weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
				monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

				oneday.setTextColor(getResources().getColor(R.color.stock_one_text));
				onemonth.setTextColor(getResources().getColor(R.color.stock_text_color));
				threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==3){
	            fenshitu.setBackgroundResource(R.drawable.index_bg);
	            dayk.setBackgroundResource(R.drawable.index_bg);
	            weekk.setBackgroundResource(R.drawable.index_bg_p);
	            monthk.setBackgroundResource(R.drawable.index_bg);
	            oneday.setBackgroundResource(R.drawable.index_bg);
	            onemonth.setBackgroundResource(R.drawable.index_bg);
	            threemonth.setBackgroundResource(R.drawable.index_bg_p);
	            oneyear.setBackgroundResource(R.drawable.index_bg);
	
	            fenshitu.setTextColor(getResources().getColor(R.color.stock_one_text));
				dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
				weekk.setTextColor(getResources().getColor(R.color.stock_text_color));
				monthk.setTextColor(getResources().getColor(R.color.stock_one_text));

				oneday.setTextColor(getResources().getColor(R.color.stock_one_text));
				onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				threemonth.setTextColor(getResources().getColor(R.color.stock_text_color));
				oneyear.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==4){
		    	fenshitu.setBackgroundResource(R.drawable.index_bg);
	            dayk.setBackgroundResource(R.drawable.index_bg);
	            weekk.setBackgroundResource(R.drawable.index_bg);
	            monthk.setBackgroundResource(R.drawable.index_bg_p);
	            oneday.setBackgroundResource(R.drawable.index_bg);
	            onemonth.setBackgroundResource(R.drawable.index_bg);
	            threemonth.setBackgroundResource(R.drawable.index_bg);
	            oneyear.setBackgroundResource(R.drawable.index_bg_p);
	            
	            fenshitu.setTextColor(getResources().getColor(R.color.stock_one_text));
				dayk.setTextColor(getResources().getColor(R.color.stock_one_text));
				weekk.setTextColor(getResources().getColor(R.color.stock_one_text));
				monthk.setTextColor(getResources().getColor(R.color.stock_text_color));

				oneday.setTextColor(getResources().getColor(R.color.stock_one_text));
				onemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				threemonth.setTextColor(getResources().getColor(R.color.stock_one_text));
				oneyear.setTextColor(getResources().getColor(R.color.stock_text_color));
	    }else if(INDEX==5){
	            curr.setBackgroundResource(R.drawable.index_bg_p);
	            history.setBackgroundResource(R.drawable.index_bg);
	
	            curr.setTextColor(getResources().getColor(R.color.stock_text_color));
		        history.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }else if(INDEX==6){
	            curr.setBackgroundResource(R.drawable.index_bg);
	            history.setBackgroundResource(R.drawable.index_bg_p);
	            
	            history.setTextColor(getResources().getColor(R.color.stock_text_color));
		        curr.setTextColor(getResources().getColor(R.color.stock_one_text));
	    }
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

	//根据股票类型判断先显示哪个K图
	private int getRefreshId() {
		String stockType = StockLargeViewPort.stockerEnvelop.getMStockerStype(mItemStockerCode);
		if (stockType != null
				&& (stockType.equals(STOCK_TYPE_GN)
						|| stockType.equals(STOCK_TYPE_HK))) {
			RefreshId = SHOW_GN_TIMES_IMAGE;
		}
		if (stockType != null
				&& stockType.equals(STOCK_TYPE_US)) {
			RefreshId = SHOW_US_ONE_DAY_IMAGE;
		}
		if (stockType != null
				&& stockType.equals(STOCK_TYPE_FUND)) {
			RefreshId = SHOW_FUND_CURRENT_IMAGE;
		}

		return RefreshId;
	}

	//刷新股票列表界面
	public void updateStaticStockerInfo() {
		String[] codesAndValues;
		String[] values;
		String code = null;
		String[] selectindex;
		String selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
		if (selecedStockerCodeAndValues != null
				&& selecedStockerCodeAndValues.length() != 0 ) {
			codesAndValues = selecedStockerCodeAndValues.split(VALUES_SEPERATOR);
			int n = codesAndValues.length;
			selectindex = new String[n];
			String[] names = new String[n];
			String[] zuixins = new String[n];
			String[] zhangdiees = new String[n];
			String[] zhangdiefus = new String[n];
			String[] chengjiaoliangs = new String[n];

			for (int i = 0; i < n; i++) {
				if (codesAndValues[i] != null
						&& codesAndValues[i].trim().length()!=0) {
					values = codesAndValues[i].split(VALUE_VALUE_SEPERATOR);
					selectindex[i] = values[COL_CODE].trim();
					names[i] = values[COL_NAME].trim();
					zuixins[i] = values[COL_ZUIXIN].trim();
					zhangdiees[i] = values[COL_ZHANGDIEE].trim();
					zhangdiefus[i] = values[COL_ZHANGDIEFU].trim();
					chengjiaoliangs[i] = values[COL_CHENGJIAOLIANG].trim();
					code = values[COL_CODE].trim();
					StockLargeViewPort.stockerEnvelop.putMStockerName(code, names[i]);
					StockLargeViewPort.stockerEnvelop.setMStockerZuixin(code, zuixins[i]);
					StockLargeViewPort.stockerEnvelop.setMStockerZhangdiee(code, zhangdiees[i]);
					StockLargeViewPort.stockerEnvelop.setMStockerZhangdiefu(code, zhangdiefus[i]);
					StockLargeViewPort.stockerEnvelop.setMChengjiaoliang(code, chengjiaoliangs[i]);
				}
			}
			mHashMap.put(R.id.stockerName, names);
			mHashMap.put(R.id.stockerCode, zuixins);

			int  selectedStockerColume = Integer.parseInt(getSharePreferenceinfo(SELECTED_COLUMN));
			if (selectedStockerColume == SELECTED_COLUMN_RATIO) {
				mHashMap.put(R.id.stockerZuixin, zhangdiefus);
			} else if (selectedStockerColume == SELECTED_COLUMN_CHANGE) {
				mHashMap.put(R.id.stockerZuixin, zhangdiees);
			} else {
				mHashMap.put(R.id.stockerZuixin, chengjiaoliangs);
			}

			String[] name = mHashMap.get(R.id.stockerName);
			String[] stockcoder = mHashMap.get(R.id.stockerCode);
			String[] zuixin = mHashMap.get(R.id.stockerZuixin);

			for (int i = 0; i < name.length; ++i){
				Log.i("========stockername============", name[i]);
				Log.i("========stockecoder============", stockcoder[i]);
				Log.i("========stockezuixin============", zuixin[i]);
				Stockname = null;
				Stockcode = null;
				Stockzuixin = null;
			}
			Stockname = name;
			Stockcode = stockcoder;
			Stockzuixin = zuixin;
			indexCodeString = selectindex;

			String indexstring = null;
			Cursor cs = cResource.query(URI, null, FIRST_SELECED_CODES_AND_TYPES_XML, null, WidgetId+NULLSTRING);
			String[] temp = null;
			if(cs.moveToFirst()) {
				temp = cs.getColumnNames();
			}
			if(temp == null) {
				temp[COL_CODE] = DEFAULT_CODE;
			}

			indexstring = temp[COL_CODE].split(VALUE_VALUE_SEPERATOR)[0].trim();
			Boolean iscodename = false;
			int tempcod = 0;
			if(indexCodeString != null && indexCodeString.length > 0) {
				for(int i = 0; i < indexCodeString.length; i++) {
					if(indexstring.equalsIgnoreCase(indexCodeString[i].trim())) {
						tempcod = i;
						iscodename = true;
					}
				}
			}
			cs.close();
			
			if (ListAdapter != null &&listview != null ) {
				if(iscodename) {
					ListAdapter = new StockeListAdapter(mContext, Stockname, Stockcode, Stockzuixin, Stockname[tempcod], selectedStockerColume);
				} else {
					String codeName = "noName";
					ListAdapter = new StockeListAdapter(mContext, Stockname, Stockcode, Stockzuixin, codeName, selectedStockerColume);
				}
				listview.setAdapter(ListAdapter);
				ListAdapter.notifyDataSetChanged();
			}
		}
	}

	//进入股票列表时启动线程获取股票数据
	void startFetchStocker(Context context) {
		String[] codesAndValues;
		String selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
		String selectedCodeAndValue = selecedStockerCodeAndValues;
		FetchSystemTime fetchSystemTime = new FetchSystemTime(context);
		fetchSystemTime.start();

		if (selectedCodeAndValue != null
				&& selectedCodeAndValue.trim().length() != 0 ) {
			codesAndValues = selectedCodeAndValue.split(VALUES_SEPERATOR);
			List<String[]> groups = Utils.splitByMaxNum(codesAndValues, MAX_CONNECTION_NUM);
			int len = groups.size();
			for (int i = 0; i < len; i++) {
				FetchStockerInfo fetchStockList = new FetchStockerInfo(groups.get(i), context);
				fetchStockList.start();
				Log.i("fetchStockerInfo", "fetchStockerInfo==Thread1");
			}
		} else {
			initSelValXml();

			for (int i = 0; i < SPECIFIED_STOCKS.length; i++) {
				StockLargeViewPort.stockerEnvelop.stockInfoTimestamp = -1L;
				FetchStockerInfo fetchStockerInfo = new FetchStockerInfo(SPECIFIED_STOCKS[i][0].toString(),
						-1L,
						context);
				fetchStockerInfo.start();
			}
		}
	}

	private void initSelValXml() {
		if (SPECIFIED_STOCKS == null || SPECIFIED_STOCKS.length == 0) {
			return;
		}
		StringBuilder selectedValuesAndTypes = new StringBuilder(NULLSTRING);
		StringBuilder selectedCodeAndTypes = new StringBuilder(NULLSTRING);

		String nullTag = mContext.getResources().getString(R.string.nullTag);
		int len = SPECIFIED_STOCKS.length;
		for (int i = 0; i < len; i++) {
			StockerEnvelop.fetchSynch.putFetchTask(SPECIFIED_STOCKS[i][0].toString(), false);
			selectedCodeAndTypes
				.append(SPECIFIED_STOCKS[i][0])
				.append(VALUES_SEPERATOR);
			String stockName = mContext.getResources().
				getString(Integer.parseInt(SPECIFIED_STOCKS[i][1].toString()));
			StockerEnvelop.fetchSynch.saveStockName(SPECIFIED_STOCKS[i][0].toString(), stockName);
			selectedValuesAndTypes
						.append(SPECIFIED_STOCKS[i][0])
						.append(VALUE_VALUE_SEPERATOR)
						.append(stockName)
						.append(VALUE_VALUE_SEPERATOR + nullTag)
						.append(VALUE_VALUE_SEPERATOR + nullTag)
						.append(VALUE_VALUE_SEPERATOR + nullTag)
						.append(VALUE_VALUE_SEPERATOR + nullTag)
						.append(VALUES_SEPERATOR);
		}
		ContentValues values1 = new ContentValues();
		values1.put(CODETYPE, SELECTED_VALUE_XML);
		values1.put(CONRENT, selectedValuesAndTypes.toString());
		cResource.insert(URI, values1);

		ContentValues values2 = new ContentValues();
		values2.put(CODETYPE, SELECED_CODES_AND_TYPES_XML);
		values2.put(CONRENT, selectedCodeAndTypes.toString());
		cResource.insert(URI, values2);
	}

	private void noNetwork(Context context2) {
		String selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
		if(selecedStockerCodeAndValues != null
				&& selecedStockerCodeAndValues.trim().length() != 0) {
			updateStaticStockerInfo();
		} else {
			initSelValXml();
			updateStaticStockerInfo();
		}
	}

	private String getSharePreferenceinfo(String type) {
		Cursor SELECTCURSOR	= cResource.query(URI, null, type, null, null);
		String[] SELECT = null;
		String TEMP = null;

		if(SELECTCURSOR.moveToFirst()) {
			SELECT = SELECTCURSOR.getColumnNames();
		}
		if(type.equals(FIRST_SELECED_CODES_AND_TYPES_XML)) {
			TEMP = SELECT[0];
		} else if(type.equals(SELECTED_VALUE_XML)) {
			StringBuilder build=new StringBuilder();
			for(int i = 0; i < SELECT.length; i++) {
				if(SELECT[i].trim().length() > 0) {
					build.append(SELECT[i].trim()).append(VALUES_SEPERATOR);
				}
			}
			TEMP = new String(build);

		}else if(type.equals(SELECED_CODES_AND_TYPES_XML)) {
			StringBuilder build = new StringBuilder();
			for(int i = 0; i < SELECT.length; i++) {
				if(SELECT[i].trim().length() > 0) {
					build.append(SELECT[i].trim()).append(VALUES_SEPERATOR);
				}
			}
			TEMP = new String(build);

		}else if(type.equals(SELECTED_COLUMN)) {
			if(SELECTCURSOR.moveToFirst()) {
				SELECT = SELECTCURSOR.getColumnNames();
			}
			TEMP = SELECT[0];

		}else if(type.equals(SELECTED_VALUE_DRAG_XML)){
			if(SELECTCURSOR.moveToFirst()) {
				SELECT = SELECTCURSOR.getColumnNames();
			}else if(type.equals("index")) {
				
			}

			StringBuilder build=new StringBuilder();
			for(int i = 0; i < SELECT.length; i++) {
				if(SELECT[i].trim().length() > 0) {
					build.append(SELECT[i]).append(VALUES_SEPERATOR);
				}
			}
			TEMP = new String(build);
		}

		SELECTCURSOR.close();
		return TEMP;
	}

	//启动线程FetchStockerImage去获取股票K图
	private void fetchImage(String imageUrl, String itemCode, int refreshID) {
		long timestamp;
		StockLargeViewPort.stockerEnvelop.mImageType = refreshID;
		StockLargeViewPort.stockerEnvelop.currentImageUrl = imageUrl;
		if (itemCode != null
				&& !"".equals(itemCode.trim())) {
			if(getNetConnectState(mContext)){
				timestamp = System.currentTimeMillis();
				StockLargeViewPort.stockerEnvelop.imageTimestamp = timestamp;
				FetchStockerImage fetchStockerImage = new FetchStockerImage(
						itemCode,
						imageUrl,
						timestamp,
						mContext);
				fetchStockerImage.start();
			}
		}
	}

	//根据股票类别获取K图
	public void fetchImage() {
		String imageUrl = StockLargeViewPort.stockerEnvelop.currentImageUrl;
		int refreshId = StockLargeViewPort.stockerEnvelop.mImageType;
		long timeStamp = System.currentTimeMillis();
		StockLargeViewPort.stockerEnvelop.stockInfoTimestamp = timeStamp;
		if (imageUrl != null && refreshId != -1) {
			fetchImage(imageUrl, mItemStockerCode, refreshId);
		} else {
			String stockType = StockLargeViewPort.stockerEnvelop.getMStockerStype(mItemStockerCode);
			if (stockType != null &&
					(stockType.equals(STOCK_TYPE_GN) || stockType.equals(STOCK_TYPE_HK))) {
				refreshId = refreshId == -1 ? SHOW_GN_TIMES_IMAGE : refreshId;
				imageUrl = imageUrl == null ? getImageUrlWithRefreshId(TYPE_GN, refreshId, mItemStockerCode) : imageUrl;
			}
			if (stockType != null && stockType.equals(STOCK_TYPE_US)) {
				refreshId = refreshId == -1 ? SHOW_US_ONE_DAY_IMAGE : refreshId;
				imageUrl = imageUrl == null ? getImageUrlWithRefreshId(TYPE_US, refreshId, mItemStockerCode) : imageUrl;
			}
			if(stockType != null&& stockType.equals(STOCK_TYPE_FUND)) {
				refreshId = refreshId == -1 ? SHOW_FUND_CURRENT_IMAGE : refreshId;
				imageUrl = imageUrl == null ? getImageUrlWithRefreshId(TYPE_FUND, refreshId, mItemStockerCode) : imageUrl;
			}
			StockLargeViewPort.stockerEnvelop.mImageType = refreshId;
			StockLargeViewPort.stockerEnvelop.currentImageUrl = imageUrl;
			fetchImage(imageUrl, mItemStockerCode, refreshId);
		}
	}

	//根据股票类别获取股票K图的url
	private String getImageUrlWithRefreshId(int type, int refreshId, String itemCode) {
		String ret = null;
		if (refreshId == -1) {
			switch (type) {
			case TYPE_US:
				ret = StockLargeViewPort.stockerEnvelop.getMStockerDayImage(itemCode);
				break;
			case TYPE_FUND:
				ret = StockLargeViewPort.stockerEnvelop.getMStockerCurrImage(itemCode);
				break;
			default:
				ret = StockLargeViewPort.stockerEnvelop.getMStockerTimeImage(itemCode);
				break;
			}

			return ret;
		}

		switch (refreshId) {
		case SHOW_GN_DAY_K_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mDayKImage;
			break;
		case SHOW_GN_WEEK_K_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mWeekKImage;
			break;
		case SHOW_GN_MONTH_K_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mMonthKImage;
			break;
		case SHOW_US_ONE_DAY_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.getMStockerDayImage(itemCode);
			break;
		case SHOW_US_ONE_MONTH_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mOneMonthImage;
			break;
		case SHOW_US_THREE_MONTH_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mThreeMonthImage;
			break;
		case SHOW_US_ONE_YEAR_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mOneYearImage;
			break;
		case SHOW_FUND_CURRENT_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.getMStockerCurrImage(itemCode);
			break;
		case SHOW_FUND_HISTORY_IMAGE:
			ret = StockLargeViewPort.stockerEnvelop.mHistoryImage;
			break;
		default:
			ret = StockLargeViewPort.stockerEnvelop.getMStockerTimeImage(itemCode);
			break;
		}

		return ret;
	}

	public void addlist() {
		
		change2SearchView();

//		final View addlistView = this.inflate(mContext, R.layout.stocker_code_search, null);
		final View addlistView = stock_search_view;
		ImageView addback = (ImageView)addlistView.findViewById(R.id.addviewback);
		addback.setOnClickListener(new BUttonOnClickListener());
		final ListView list = (ListView)addlistView.findViewById(R.id.list1);
		searchImageView = (ImageView)addlistView.findViewById(R.id.item_edits);
		bar = (ProgressBar)addlistView.findViewById(R.id.ProgressBar);
		bar.setVisibility(View.GONE);

		selcodelist.clear();
//		selcodelist_add.clear();
//		selcodelist_add_name.clear();
//		selcodelist_del.clear();
//		selcodelist_merge.clear();
        Cursor cur = cResource.query(URI, null, SELECED_CODES_AND_TYPES_XML, null, null);
        String[] Colume = null;

        if(cur != null && cur.getCount()>0){
                Colume = cur.getColumnNames();
        }
        cur.close();
        if(Colume == null || Colume.length==0){
                int len = SPECIFIED_STOCKS.length;
                for(int i = 0; i < len; i++){
                        Colume[i] = SPECIFIED_STOCKS[i][0].toString();
                }
        }
        if(Colume != null && Colume.length > 0){
                for(int i = 0; i < Colume.length; i++){
                        selcodelist.add(Colume[i].split(VALUE_VALUE_SEPERATOR)[COL_CODE]);
//                        selcodelist_merge.add(Colume[i].split(VALUE_VALUE_SEPERATOR)[COL_CODE]);
                        Log.i("zz", "search before SELECED_CODES_AND_TYPES_XML="+Colume[i]);
                }
        }
	resetUI(list);
        searchImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				boolean isConnected = getNetConnectState(mContext);
				if(isConnected){
					String tempString = edit.getText().toString();

					Log.d("zz", "onsearching00000000000");
					if(tempString != null && list != null && tempString.trim().length() > 0) {
						if(bar != null) {
							bar.setVisibility(View.VISIBLE);
						}
						search_layout.setVisibility(INVISIBLE);
						StockerEnvelop.mStockerCodeAndType.clear();
						mAdapter.clear();
						doStocker(tempString, list);
					}
				} else {
					Toast toast = new Toast(mContext);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.makeText(mContext, R.string.newworknull, Toast.LENGTH_SHORT).show();
				}
			}
		});

		edit = (EditText)addlistView.findViewById(R.id.item_edit);
		edit.setText("");
		edit.setHint(R.string.hint);

		edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus) {
					edit.setHint("");
				} else {
					edit.setHint(R.string.hint);
					((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                                        .hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}

		});
		edit.setOnEditorActionListener(new EditText.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				boolean isConnected = getNetConnectState(mContext);
				if(isConnected){
					String tempString = edit.getText().toString();

					Log.d("zz", "onsearching1111111");
					if(tempString != null && list != null && tempString.trim().length() > 0) {
						if(bar != null) {
							bar.setVisibility(View.VISIBLE);
						}
						search_layout.setVisibility(INVISIBLE);
						StockerEnvelop.mStockerCodeAndType.clear();
						mAdapter.clear();
						doStocker(tempString, list);
					}
				} else {
					Toast toast = new Toast(mContext);
					toast.setGravity(Gravity.BOTTOM, 0, 0);
					toast.makeText(mContext, R.string.newworknull, Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
//		edit.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(event.getAction() == MotionEvent.ACTION_UP) {
////					rBounds = dLeft.getBounds();
//					final int x = (int)event.getX();
//					final int y = (int)event.getY();
//					Log.i("zz", "x======="+ x+NULLSTRING);
//					Log.i("zz", "y======="+ y+NULLSTRING);
//					Log.i("v.getLeft()+v.getPaddingLeft()=======", v.getLeft()+v.getPaddingLeft()+NULLSTRING);
//					Log.i("v.getPaddingTop()=======", v.getPaddingTop()+NULLSTRING);
//					Log.i("v.getHeight()-v.getPaddingBottom()=======", v.getHeight()-v.getPaddingBottom()+NULLSTRING);
//					if(x<=45 && x<=(v.getLeft()+v.getPaddingLeft())
//							&& y>=v.getPaddingTop()
//							&& y<=(v.getHeight()-v.getPaddingBottom())) {
//						boolean isConnected = getNetConnectState(mContext);
//						if(isConnected){
//							String tempString = edit.getText().toString();
//							if(tempString != null && list != null && tempString.trim().length() > 0) {
//								doStocker(tempString, list);
//							}
//							event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
//						}
//					}
//				}
//				return false;
//			}
//		});
//		try {
//			String selecedCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
//			Log.i("zzcao", "addlist  selecedCodeAndValues "+selecedCodeAndValues);
//			ContentValues values2 = new ContentValues();
//			values2.put(CODETYPE, SELECTED_VALUE_DRAG_XML);
//			values2.put(CONRENT, selecedCodeAndValues.toString());
//			cResource.insert(URI, values2);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	//启动线程执行股票查询然后刷新搜索结果列表
	private void doStocker(CharSequence rssUrl, ListView list) {
		try {
			String mUrl = kStockerSearchUrl + URLEncoder.encode(rssUrl.toString(),"utf-8");
			StockerWorker worker = new StockerWorker(mUrl);
			setCurrentWorker(worker);
			resetUI(list);
			worker.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//刷新搜索结果列表
	public void resetUI(ListView list) {
		final List<StockerCodeItem> items = new ArrayList<StockerCodeItem>();
		mAdapter = new StockerListAdapter(mContext, items);
		list.setAdapter(mAdapter);
		list.setOnItemClickListener(onItemClickListener);
	}

	//搜索结果点击事件
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			StockerCodeItem item = (StockerCodeItem)mAdapter.getItem(position);
			String itemCode = item.getCode().toString().trim();
			if(!selcodelist.contains(itemCode)) {
				selcodelist.add(itemCode);
//				String codeAndType = StockLargeViewPort.stockerEnvelop.getMStockerCodeAndType(itemCode);
				String codeAndType = itemCode.concat("/").concat(item.getType().toString().trim());
				String itemName = item.getName().toString().trim();
				StockerEnvelop stockerPEnvelop = new StockerEnvelop(mContext);
				stockerPEnvelop.addSelecedStockerValues(codeAndType, itemName);
				Log.d("czz", "click codeAndTypeCT== "+codeAndType+" codeAndTypeN== "+itemName);
				
				mAdapter.notifyDataSetChanged();
				fetchFristInfo(mContext);
			} else {
				Toast.makeText(mContext, R.string.have_added, 0).show();
			}
			
//			Log.d("czz", "click codeAndTypeC== "+item.getCode().toString().trim());
//			Log.d("czz", "click codeAndTypeT== "+item.getType().toString().trim());
//			initStocktListView();
//			if(!Keep.contains(codeAndType)) {
//				if(!selcodelist.contains(itemCode)) {
//					if(!selcodelist_add.contains(itemCode)) {
//						selcodelist_add.add(itemCode);
//						selcodelist_add_name.add(itemName);
//						selcodelist_merge.add(itemCode);
//					} else {
//						selcodelist_add.remove(itemCode);
//						selcodelist_add_name.remove(itemName);
//						selcodelist_merge.remove(itemCode);
//					}
//				} else {
//					if(!selcodelist_del.contains(itemCode)) {
//						selcodelist_del.add(itemCode);
//						selcodelist_merge.remove(itemCode);
//					} else {
//						selcodelist_del.remove(itemCode);
//						selcodelist_merge.add(itemCode);
//					}
//				}
//			}
			
//            if(selcodelist_merge != null && selcodelist_merge.size()>0) {
//        		for(int i=0; i<selcodelist_merge.size(); i++)
//        			Log.i("czz", "   selcodelist_merge "+i+" =  "+selcodelist_merge.get(i));
//            } else {
//            	Log.i("czz", "   selcodelist_merge is null and selcodelist.size is "+selcodelist.size());
//            }
            
		}
	};

	public void initStocktListView() {

		change2ListView();

		View StockListView = stocklist_view;
//		View StockListView = this.inflate(mContext, R.layout.stockerlist_widget, null);
		onUpdateViews(mContext);
		listview = (ListView) StockListView.findViewById(R.id.list2);

		TextView fetchTiem = (TextView)StockListView.findViewById(R.id.StockTime);
		if(edit != null) {
			((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		String stockTime = StockLargeViewPort.stockerEnvelop.mStockerUpdateTime;
		if(stockTime != null && stockTime.trim().length() > 0) {
			int len = stockTime.lastIndexOf(":");
			if(len != -1) {
				stockTime = stockTime.subSequence(0, len)+NULLSTRING;
			}
			fetchTiem.setText(mContext.getResources().getString(R.string.timeTitle) + " " + stockTime);
		}

		String indexstring = null;
		Cursor CS = cResource.query(URI, null, FIRST_SELECED_CODES_AND_TYPES_XML, null, WidgetId+NULLSTRING);
		String[] temp = null;
		if(CS.moveToFirst()) {
			temp = CS.getColumnNames();
		}
		if(temp == null) {
			temp[COL_CODE] = DEFAULT_CODE;
		}
		int tempcod = 0;
		indexstring = temp[COL_CODE].split(VALUE_VALUE_SEPERATOR)[0].trim();
		Boolean iscodename = false;
		if(indexCodeString != null && indexCodeString.length > 0) {
			for(int i = 0; i < indexCodeString.length; i++) {
				if(indexstring.equalsIgnoreCase(indexCodeString[i].trim())) {
					tempcod = i;
					iscodename = true;
				}
			}
		}
		CS.close();

		int selectedStockerColume = Integer.parseInt(getSharePreferenceinfo(SELECTED_COLUMN));
		Log.i(TAG, "selectedStockerColume="+ selectedStockerColume+NULLSTRING);

		if(iscodename) {
			//        	ListAdapter=new SwedenStocksListAdapter(mContext, Stockname, Stockcode, Stockzuixin,Stockname[tempcod],selectedStockerColume);
			ListAdapter = new StockeListAdapter(mContext, Stockname, Stockcode, Stockzuixin, Stockname[tempcod], selectedStockerColume);
		} else {
			String codename = "noName";
			ListAdapter = new StockeListAdapter(mContext, Stockname, Stockcode, Stockzuixin, codename, selectedStockerColume);
		}

		listview.setAdapter(ListAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int positions,
					long id) {
				// TODO Auto-generated method stub
				startonclik(positions);
//				SetInitView();
				INDEX = 1;
				fetchFristInfo(mContext);
			}

		});

		ImageView listback = (ImageView) StockListView.findViewById(R.id.stockerListBack);
		listback.setOnClickListener(new BUttonOnClickListener());
		ImageView Editlist = (ImageView) StockListView.findViewById(R.id.stockerSetting);
		Editlist.setOnClickListener(new BUttonOnClickListener());
		ImageView addlist = (ImageView) StockListView.findViewById(R.id.stockeradd);
		addlist.setOnClickListener(new BUttonOnClickListener());

		tag = (ImageView)StockListView.findViewById(R.id.tag);
		mZhangdieeBt = (TextView)StockListView. findViewById(R.id.zhangdiee_button);
		mZhangdiefuBt = (TextView) StockListView.findViewById(R.id.zhangdiefu_button);
		mChengjiaoliangBt = (TextView) StockListView.findViewById(R.id.chengjiaoliang_button);
		initEditButton(selectedStockerColume, mZhangdieeBt, mZhangdiefuBt, mChengjiaoliangBt);

		View.OnClickListener lZhangdiefu = new OnClickListener() {

			@Override
    		public void onClick(View view) {
				ContentValues values2 = new ContentValues();
				values2.put(CODETYPE, SELECTED_COLUMN);
				values2.put(CONRENT,SELECTED_COLUMN_RATIO+NULLSTRING);
				cResource.insert(URI, values2);

				initStocktListView();

				StockLargeViewPort.stockerEnvelop.currentVolume = SELECTED_COLUMN_RATIO;
				mTimeoutHandler.sendEmptyMessage(FETCHSTOCKER_ID_TIMEOUT);
			}
		};

		View.OnClickListener lZhangdiee = new OnClickListener() {

			@Override
			public void onClick(View view) {
				ContentValues values2 = new ContentValues();
				values2.put(CODETYPE, SELECTED_COLUMN);
				values2.put(CONRENT,SELECTED_COLUMN_CHANGE+NULLSTRING);
				cResource.insert(URI, values2);

				initStocktListView();
				
	     		StockLargeViewPort.stockerEnvelop.currentVolume = SELECTED_COLUMN_CHANGE;
	     		mTimeoutHandler.sendEmptyMessage(FETCHSTOCKER_ID_TIMEOUT);
	     	}
		};

		View.OnClickListener lChengjiaoliang = new OnClickListener() {

			@Override
			public void onClick(View view) {
				ContentValues values2 = new ContentValues();
				values2.put(CODETYPE, SELECTED_COLUMN);
				values2.put(CONRENT,SELECTED_COLUMN_VOLUME+NULLSTRING);
				cResource.insert(URI, values2);

				initStocktListView();

				StockLargeViewPort.stockerEnvelop.currentVolume = SELECTED_COLUMN_VOLUME;
				mTimeoutHandler.sendEmptyMessage(FETCHSTOCKER_ID_TIMEOUT);
			}
		};
		mZhangdieeBt.setOnClickListener(lZhangdiee);
		mChengjiaoliangBt.setOnClickListener(lChengjiaoliang);
		mZhangdiefuBt.setOnClickListener(lZhangdiefu);

		TextView textTime = (TextView) StockListView.findViewById(R.id.StockTime);
		if (StockLargeViewPort.stockerEnvelop.mStockerSystemTime != null) {
			textTime.setText(StockLargeViewPort.stockerEnvelop.mStockerSystemTime);
		}
	}

	public void initEditButton(int selectedStockerColume, TextView Zhangdiee, TextView Zhangdiefu, TextView Chengjiaoliang){

		if (selectedStockerColume == SELECTED_COLUMN_RATIO) {

			Zhangdiee.setTextColor(R.color.update_time);
//			Zhangdiefu.setTextColor(R.color.update_time);
			Chengjiaoliang.setTextColor(R.color.update_time);
			tag.setImageResource(R.drawable.tag1);
		} else if (selectedStockerColume == SELECTED_COLUMN_CHANGE) {
//			Zhangdiee.setTextColor(R.color.update_time);
			Zhangdiefu.setTextColor(R.color.update_time );
			Chengjiaoliang.setTextColor(R.color.update_time);
			tag.setImageResource(R.drawable.tag2);
		} else {
			Zhangdiee.setTextColor(R.color.update_time);
			Zhangdiefu.setTextColor(R.color.update_time );
//			Chengjiaoliang.setTextColor(R.color.update_time);
			tag.setImageResource(R.drawable.tag3);
		}
	}

	public void startonclik(int positions) {

		if(positions != 100){

			String[] Colume = null;
			Cursor CODETEMP = cResource.query(URI, null, SELECED_CODES_AND_TYPES_XML, null, null);
			if(CODETEMP.moveToFirst()) {
				Colume = CODETEMP.getColumnNames();
			}
			String [] selecedStocke = Colume;
			final Bundle extras = new Bundle();
			extras.putInt(ITEM_ENABLED, 1);
			if(selecedStocke != null && selecedStocke.length > positions){
				SelectedStockFirst = selecedStocke[positions];
				SelectStock = selecedStocke[positions];
			} else {
				SelectedStockFirst = FRISTSTOCK;
				SelectStock = FRISTSTOCK;
			}
			
			CODETEMP.close();
		}
	}

	public synchronized void setCurrentWorker(StockerWorker worker) {
		if (mWorker != null) 
			mWorker.interrupt();
		mWorker = worker;
	}

	//股票查询线程
	private class StockerWorker extends Thread {
		private CharSequence mUrl;

		public StockerWorker(CharSequence url) {
			mUrl = url;
		}

		@Override
		public void run() {
			try {
				// Standard code to make an HTTP connection.
				URL url = new URL(mUrl.toString());
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(10000);
				connection.connect();
				InputStream in = connection.getInputStream();
				parseStocker(in, mAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//对查询得到的InputStream解析并更新adapter
	void parseStocker(InputStream in, StockerListAdapter adapter) throws IOException, XmlPullParserException {
		XmlPullParser xpp = Xml.newPullParser();
		xpp.setInput(in, null);  // null = default to UTF-8

		int eventType;
		String code = NULLSTRING;
		String type = NULLSTRING;
		String name = NULLSTRING;
		eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				String tag = xpp.getName();
				if (tag.equals("stock")) {
					code = type = name = NULLSTRING;
				} else if (tag.equals("code")) {
					xpp.next();
					code = (xpp.getText() == null ? NULLSTRING : xpp.getText().trim());
				} else if (tag.equals(CODETYPE)) {
					xpp.next();
					type = (xpp.getText() == null ? NULLSTRING : xpp.getText().trim());
				} else if (tag.equals("name")) {
					xpp.next();
					name = (xpp.getText() == null ? NULLSTRING : xpp.getText().trim());
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				String tag = xpp.getName();
				if (tag.equals("stock")) {
					StockerCodeItem item = new StockerCodeItem(code, type, name);
					StockLargeViewPort.stockerEnvelop.putMStockerCodeAndType(code,
							code + VALUE_VALUE_SEPERATOR + type);
					mTimeoutHandler.post(new ItemAdder(item));
				}
			}
			eventType = xpp.next();
		}
		mTimeoutHandler.sendMessage(mTimeoutHandler.obtainMessage(REFRESH_ID));
	}

	//将查询得到的股票添加到查询结果adpter中去
	private class ItemAdder implements Runnable {
		StockerCodeItem mItem;
		ItemAdder(StockerCodeItem item) {
			mItem = item;
		}

		public void run() {
			mAdapter.add(mItem);
		}
	}

	private class StockerListAdapter extends ArrayAdapter<StockerCodeItem> {

		private LayoutInflater mInflater;

		public StockerListAdapter(Context context, List<StockerCodeItem> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.stocker_code_search_item, null);
			}
			StockerCodeItem item = this.getItem(position);
			searchCode = (TextView) convertView.findViewById(R.id.search_code);
            searchName = (TextView) convertView.findViewById(R.id.search_name);
            searchAdded = (ImageView) convertView.findViewById(R.id.search_added);
            searchCode.setText(item.getCode());
            searchName.setText(item.getName());
//            if(selcodelist_merge != null && selcodelist_merge.contains(item.getCode())) {
            if(selcodelist != null && selcodelist.contains(item.getCode())) {
                    searchAdded.setVisibility(VISIBLE);
                } else {
                    searchAdded.setVisibility(GONE);
                }
            return convertView;
		}
	}

	public void Editlist() {
		REMIN = 4;
		change2EditView();

		View EditlistView = stock_edit_view;//this.inflate(mContext, R.layout.stocker_code_list, null);
		ImageView savetext = (ImageView)EditlistView.findViewById(R.id.CodelistBack);
		savetext.setOnClickListener(new BUttonOnClickListener());
		mCompleteBt = (ImageView) EditlistView.findViewById(R.id.savebutton);
//		SelectCode=getSharePreferenceinfo(SELECTED_VALUE_XML);
//		SelectType=getSharePreferenceinfo(SELECED_CODES_AND_TYPES_XML);
		codelist.clear();
		// fix bug:164223
                CodesAndTypes_drag = null;

		mCompleteBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String dragStock = getSharePreferenceinfo(SELECTED_VALUE_DRAG_XML);
				if(dragStock.length() > 0 && CodesAndTypes_drag != null) {
					ContentValues values1 = new ContentValues();
					values1.put(CODETYPE, SELECTED_VALUE_XML);
					values1.put(CONRENT, dragStock.toString());
					cResource.insert(URI, values1);

					ContentValues values2 = new ContentValues();
					values2.put(CODETYPE, SELECED_CODES_AND_TYPES_XML);
					values2.put(CONRENT, CodesAndTypes_drag.toString());
					cResource.insert(URI, values2);
				}
				if(codelist != null && codelist.size() > 0) {
					if(codelist.contains(SelectStock)) {
						SelectStock = null;
					}
					Intent deletintent = new Intent(INTENT_DELET);
					Bundle bundle = new Bundle();
					bundle.putCharSequenceArrayList("stockid", codelist);
					bundle.putInt("WidgetId", WidgetId);
//					Toast.makeText(mContext, SelectStock+"==", 1).show();
					deletintent.putExtras(bundle);
					mContext.sendBroadcast(deletintent);
				}
//				Log.d(TAG, "SelectStock="+SelectStock);
//				initStocktListView();
				fetchFristInfo(mContext);
			}
		});

		FrameLayout con = (FrameLayout) EditlistView.findViewById(R.id.item_list);
		mStockerListView = new ListViewInterceptor(mContext);
		Resources res = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.list);
		Drawable drawable = new BitmapDrawable(bmp);
		mStockerListView.setDivider(drawable);
		mStockerListView.setVerticalFadingEdgeEnabled(false);
		con.removeAllViews();
		con.addView(mStockerListView);
		try {
			String selecedCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_XML);
//			Log.i("zzcao", "Editlist  selecedCodeAndValues "+selecedCodeAndValues);
			ContentValues values2 = new ContentValues();
			values2.put(CODETYPE, SELECTED_VALUE_DRAG_XML);
			values2.put(CONRENT, selecedCodeAndValues.toString());
			cResource.insert(URI, values2);

			this.mSelecedCodeAndValues = selecedCodeAndValues;

			refreshCodeListView(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshCodeListView(boolean renewAdapter) {
		String selecedCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_DRAG_XML);
		this.mSelecedCodeAndValues = selecedCodeAndValues;

		int len = selecedCodeAndValues.split(VALUES_SEPERATOR).length;
		String[] mStrings = new String[len];
		for (int i = 0; i < len; i++) {
//			mStrings[i] = selecedCodeAndValues.split(VALUES_SEPERATOR)[i].split(VALUE_VALUE_SEPERATOR)[COL_CODE];
			mStrings[i] = selecedCodeAndValues.split(VALUES_SEPERATOR)[i].split(VALUE_VALUE_SEPERATOR)[COL_NAME];
		}
		int  selectedStockerColume = Integer.parseInt(getSharePreferenceinfo(SELECTED_COLUMN));
		mTempValues = mStrings;
		if (mStrings[0] != null && mStrings[0].trim().length() != 0) {
			if (mQuotAdaptor == null || renewAdapter) {
				mQuotAdaptor = new QuoteAdaptor();
				mQuotAdaptor.setDataSet(mStrings, selectedStockerColume);
				mStockerListView.setAdapter(mQuotAdaptor);
				mStockerListView.setDropListener(R.id.drag, onDrop);
			} else {
				mQuotAdaptor.setDataSet(mStrings, selectedStockerColume);
				mQuotAdaptor.notifyDataSetChanged();
				mStockerListView.setDropListener(R.id.drag, onDrop);
			}
		} else {
			mQuotAdaptor = null;
			mStockerListView.setAdapter(null);
		}
	}

	public class QuoteAdaptor extends BaseAdapter {

		private String[] mStrings = null;
		private int BL;

		public void setDataSet(String[] dataset, int selectedStockerColume) {
			this.mStrings = dataset;
			this.BL = selectedStockerColume;
		}

		public int getCount() {
			return mStrings != null ? mStrings.length : 0;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) { /////????
			return mStrings != null && !mStrings[position].startsWith(VALUE_VALUE_SEPERATOR);
		}

		public Object getItem(int position) {//????
			return (mStrings != null && mStrings.length > 0) ? mStrings[position] : null; ///????position
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater;
			String temp;
			inflater = LayoutInflater.from(mContext);
			View cellLayout = (View) inflater.inflate(R.layout.quote_cell, null);
			cellLayout.setFocusable(false);
			mDelImageView = (ImageView) cellLayout.findViewById(R.id.delete);
			mStockerNameTextView = (TextView) cellLayout.findViewById(R.id.stockerName);
			mStockerZuixinTextView = (TextView) cellLayout.findViewById(R.id.stockerZuixin);
			mStockerValuesTextView = (TextView) cellLayout.findViewById(R.id.stockerValues);
			stockerValues_zhang = (TextView) cellLayout.findViewById(R.id.stockerValues_zhang);
			stockerValues_die = (TextView) cellLayout.findViewById(R.id.stockerValues_die);
			ImageView sanjiao   =   (ImageView) cellLayout.findViewById(R.id.sanjiao2);

			if(mStrings[position].trim() == null || mStrings[position].trim().equals("null") || mStrings[position].trim().length() == 0) {
				temp = CODE_TYPE_SEPERATOR;
			} else {
				temp = mStrings[position].trim();
			}
			mStockerNameTextView.setText(temp);
			if(temp.equals("上证指数")||temp.equals("深证成指")) {
              mDelImageView.setVisibility(View.INVISIBLE);
          } else {
              mDelImageView.setVisibility(View.VISIBLE);
          }

			Log.i("edit", position+"  tempname="+tempname+"  temp="+temp);
			if(tempname != null && temp != null) {
				if(tempname.equals(temp)) {
					sanjiao.setBackgroundResource(R.drawable.list_small_tag);
				} else {
					sanjiao.setBackgroundColor(Color.argb(0, 0, 255, 0));
				}
			}
			//删除事件
			View.OnClickListener lDelOnclickListener = new OnClickListener() {
				public void onClick(View view) {
					displayDelConfirmation(position);
				}
			};
			mDelImageView.setOnClickListener(lDelOnclickListener);

//			String stockerCode = mStrings == null ? null : mStrings[position].trim();
//			if(mStrings[position].trim() == null || mStrings[position].trim().equals("null")) {
//				mStrings[position] = CODE_TYPE_SEPERATOR;
//			}
//
//			if (stockerCode != null && stockerCode.trim().length() != 0) {
//				String nullTag = mContext.getResources().getString(R.string.nullTag);
//				temp = StockLargeViewPort.stockerEnvelop.getMStockerName(stockerCode);
//				mStockerNameTextView.setText(temp == null || temp.equals("null") ? stockerCode : temp);
//
//				if(tempname != null && temp != null) {
//					if(tempname.equals(temp)) {
//						sanjiao.setBackgroundResource(R.drawable.sanjiao);
//					} else {
//						sanjiao.setBackgroundColor(Color.argb(0, 0, 255, 0));
//					}
//				}
//
//				//最新价格
//				temp = StockLargeViewPort.stockerEnvelop.getMStockerZuixin(stockerCode);
//				if(temp == null || temp.equals("null")) {
//					temp = nullTag;
//				}
//				mStockerZuixinTextView.setText(temp == null ? nullTag : temp);
//
//				//删除事件
//				View.OnClickListener lDelOnclickListener = new OnClickListener() {
//					public void onClick(View view) {
//						displayDelConfirmation(position);
//					}
//				};
//				mDelImageView.setOnClickListener(lDelOnclickListener);
//
//				if (BL == SELECTED_COLUMN_RATIO) {
//					mStockerValuesTextView.setVisibility(GONE);
//
//					temp = StockLargeViewPort.stockerEnvelop.getMStockerZhangdiefu(stockerCode);
//					if(temp == null || temp.equals("null")) {
//						temp = nullTag;
//					}
//					String st = temp;
//
//					if(st.contains("+") && st.contains("%")) {
//						stockerValues_zhang.setVisibility(View.VISIBLE);
//						stockerValues_zhang.setText(temp == null ? nullTag : temp);
//					} else if(st.contains(CODE_TYPE_SEPERATOR) && st.contains("%")) {
//						stockerValues_die.setVisibility(View.VISIBLE);
//						stockerValues_die.setText(temp == null ? nullTag : temp);
//					}
//				} else if (BL == SELECTED_COLUMN_CHANGE) {
//					mStockerValuesTextView.setVisibility(GONE);
//
//					temp = StockLargeViewPort.stockerEnvelop.getMStockerZhangdiee(stockerCode);
//					if(temp == null || temp.equals("null")) {
//						temp = nullTag;
//					}
//					String st = temp;
//
//					if(st.contains(CODE_TYPE_SEPERATOR)) {
//						mStockerValuesTextView.setVisibility(View.VISIBLE);
//						mStockerValuesTextView.setText(temp == null ? nullTag : temp);
//					} else if(st.contains("+")){
//						stockerValues_zhang.setVisibility(View.VISIBLE);
//						stockerValues_zhang.setText(temp == null ? nullTag :temp);
//					} else if(st.contains(CODE_TYPE_SEPERATOR)) {
//						stockerValues_die.setVisibility(View.VISIBLE);
//						stockerValues_die.setText(temp == null ? nullTag : temp);
//					} else {
//						stockerValues_zhang.setVisibility(View.VISIBLE);
//						stockerValues_zhang.setText(temp == null ? nullTag : "+"+temp);
//					}
//				} else if (BL == SELECTED_COLUMN_VOLUME) {
//					mStockerValuesTextView.setVisibility(View.VISIBLE);
//
//					temp = StockLargeViewPort.stockerEnvelop.getMChengjiaoliang(stockerCode);
//					if(temp == null || temp.equals("null")) {
//						temp = nullTag;
//					}
//					mStockerValuesTextView.setText(temp == null ? nullTag : temp);
//				}
//
//				if(stockerValues_zhang.getVisibility() == GONE && stockerValues_die.getVisibility() == GONE) {
//					mStockerValuesTextView.setText(temp == null ? nullTag : temp);
//					mStockerValuesTextView.setVisibility(View.VISIBLE);
//				}
//			}
			cellLayout.setMinimumWidth(parent.getWidth());

			return cellLayout;
		}
	}

	private ListViewInterceptor.DropListener onDrop = new ListViewInterceptor.DropListener() {

		public void drop(int from, int to) {
			Log.i("drag===============================", from+":"+to);
			String selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_DRAG_XML);
			String selecedCodeAndValues = selecedStockerCodeAndValues;
			mSelecedCodeAndValues = selecedCodeAndValues;
			List<String> list = new ArrayList<String>();
			int len = selecedCodeAndValues.split(VALUES_SEPERATOR).length;
			String[] mStrings = new String[len];
			String[] tempsave = new String[len];
			String[]    codes = new String[len];
			String[]    names = new String[len];

			for (int i = 0; i < len; i++) {
				list.add(selecedCodeAndValues.split(VALUES_SEPERATOR)[i]);
			}
			if(to != from) {
				String temp=list.get(from);
				list.remove(from);
				list.add(to, temp);
			}

			for (int i = 0; i < len; i++) {
				tempsave[i]=list.get(i);
				codes[i]=list.get(i).split(VALUE_VALUE_SEPERATOR)[COL_TYPE];
				names[i]=list.get(i).split(VALUE_VALUE_SEPERATOR)[COL_NAME];
				mStrings[i] = list.get(i).split(VALUE_VALUE_SEPERATOR)[COL_CODE];
			}

			int  selectedStockerColume = Integer.parseInt(getSharePreferenceinfo(SELECTED_COLUMN));
			Log.i("selectedStockerColume", selectedStockerColume+NULLSTRING);

			mTempValues = mStrings;
			if (names[0] != null && names[0].trim().length() != 0) {

				if (mQuotAdaptor == null ) {
					mQuotAdaptor = new QuoteAdaptor();
					mQuotAdaptor.setDataSet(names, selectedStockerColume);
					mStockerListView.setAdapter(mQuotAdaptor);
				} else {
					mQuotAdaptor.setDataSet(names, selectedStockerColume);
					mQuotAdaptor.notifyDataSetChanged();
				}
			} else {
				mQuotAdaptor = null;
				mStockerListView.setAdapter(null);
			}

			StringBuilder CodeAndValue = new StringBuilder();
			for(int i = 0; i < tempsave.length; i++) {
				CodeAndValue.append(tempsave[i]).append(VALUES_SEPERATOR);
			}
			StringBuilder CodesAndTypes = new StringBuilder();
			for(int i = 0; i < mStrings.length; i++) {
				CodesAndTypes.append(mStrings[i]).append(VALUE_VALUE_SEPERATOR).append(codes[i]).append(VALUES_SEPERATOR);
			}
			CodesAndTypes_drag = new String(CodesAndTypes);
			ContentValues values2 = new ContentValues();
			values2.put(CODETYPE, SELECTED_VALUE_DRAG_XML);
			values2.put(CONRENT, new String(CodeAndValue));
			cResource.insert(URI, values2);
		}
	};

	public void displayDelConfirmation(final int position) {
		removeByStockIndex(position);
	}

	public void removeByStockIndex(int position) {
		String code = null;
		String codeAndValue = null;
		String[] values = null;

//		selecedStockerCodeAndValues=sh000001/1/上证指数/--/--/--/--;sz399001/1/深证成指/--/--/--/--;00992/5/联想集团/--/--/--/--;
		String selecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_DRAG_XML);
//		String selectedCodesAndTypes =getSharePreferenceinfo(SELECED_CODES_AND_TYPES_XML);

//		code=sh000001  要删除的股票
		code = selecedStockerCodeAndValues.split(VALUES_SEPERATOR)[position].split(VALUE_VALUE_SEPERATOR)[0];
//		codetemp=sh000001/1  要删除的股票
		String codetemp = code + VALUE_VALUE_SEPERATOR
								+ selecedStockerCodeAndValues.split(VALUES_SEPERATOR)[position].split(VALUE_VALUE_SEPERATOR)[1];

//		mStrings[0]=sh000001/1/上证指数/--/--/--/--
		String[] mStrings = selecedStockerCodeAndValues.split(VALUES_SEPERATOR);

		StringBuilder CodesAndTypes = new StringBuilder();
		for(int i = 0; i < mStrings.length; i++) {
			CodesAndTypes.append(
					mStrings[i].split(VALUE_VALUE_SEPERATOR)[0])
						.append(VALUE_VALUE_SEPERATOR)
						.append(mStrings[i].split(VALUE_VALUE_SEPERATOR)[1])
						.append(VALUES_SEPERATOR);
			}
//		CodesAndTypes=selectedCodesAndTypes=sh000001/1;sz399001/1;00992/5;
		String selectedCodesAndTypes = new String(CodesAndTypes);

		//上证和深证不能删除
		if(!Keep.contains(codetemp)) {
//			values=sh000001/1/上证指数/--/--/--/--要删除的股票各部分拆开的
			values = selecedStockerCodeAndValues.split(VALUES_SEPERATOR)[position].split(VALUE_VALUE_SEPERATOR);
//			codeAndValue=sh000001/1  要删除的股票
			codeAndValue = selecedStockerCodeAndValues.split(VALUES_SEPERATOR)[position].split(VALUE_VALUE_SEPERATOR)[0]
			                   + VALUE_VALUE_SEPERATOR
			                   + selecedStockerCodeAndValues.split(VALUES_SEPERATOR)[position].split(VALUE_VALUE_SEPERATOR)[1];

//			mSelecedStockerCodeAndValues=sh000001/1/上证指数/--/--/--/--;sz399001/1/深证成指/--/--/--/--;00992/5/联想集团/--/--/--/--;
			String mSelecedStockerCodeAndValues = selecedStockerCodeAndValues;
//			selectedCodeAndTypes=sh000001/1;sz399001/1;00992/5;
			String selectedCodeAndTypes = selectedCodesAndTypes;
			if (mSelecedStockerCodeAndValues != null
					&& mSelecedStockerCodeAndValues.trim().length() != 0
					&& mSelecedStockerCodeAndValues.contains(codeAndValue)) {
//				values[0]=sh000001/1/上证指数/--/--/--/--
				values = mSelecedStockerCodeAndValues.split(VALUES_SEPERATOR);

				//将删除的stock从dragXML中删除掉
				for (int i = 0; i < values.length; i ++) {
					if (values[i].contains(codeAndValue)) {
						mSelecedStockerCodeAndValues = mSelecedStockerCodeAndValues
								.replace((values[i] + VALUES_SEPERATOR), NULLSTRING);
						break;
					}
				}
			}

			//将删除的stock从CodesAndTypes_drag中删除掉，并且记住新的要删除的codelist
			if (selectedCodeAndTypes != null) {
				selectedCodeAndTypes = selectedCodeAndTypes.replace((codeAndValue + VALUES_SEPERATOR), NULLSTRING);

//				if(CodesAndTypes_drag != null && CodesAndTypes_drag.length() > 0) {
//					String[] dragtype = CodesAndTypes_drag.split(VALUES_SEPERATOR);
//					int len = dragtype.length;
//					StringBuilder build = new StringBuilder();
//					for(int i = 0; i < len; i++) {
//						String dragtype_temp = dragtype[i];
//						if(selectedCodeAndTypes.contains(dragtype_temp)) {
//							build.append(dragtype_temp).append(VALUES_SEPERATOR);
//						}
//					}
//					CodesAndTypes_drag = new String(build);
//				}
				codelist.add(codeAndValue);
			}

			//更新SELECTED_VALUE_DRAG_XML
			ContentValues values3 = new ContentValues();
			values3.put(CODETYPE, SELECTED_VALUE_DRAG_XML);
			values3.put(CONRENT, mSelecedStockerCodeAndValues);
			cResource.insert(URI, values3);

			CodesAndTypes_drag = selectedCodeAndTypes;
			StockerEnvelop.fetchSynch.fetchTasks.remove(codeAndValue);
			StockerEnvelop.fetchSynch.fetchFailure.remove(codeAndValue);
			StockerEnvelop.fetchSynch.taskDatas.remove(codeAndValue);
			StockerEnvelop.fetchSynch.removeStockName(codeAndValue);

			StockLargeViewPort.stockerEnvelop.mStockerBitMap.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerChengjiaoliang.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerCodeAndType.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerCurrImage.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerDayImage.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerName.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerStype.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerTimeImage.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerZhangdiee.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerZhangdiefu.remove(code);
			StockLargeViewPort.stockerEnvelop.mStockerZuixin.remove(code);

			refreshCodeListView(true);
		}
	}

	public void removeByStockList(ArrayList<CharSequence> delList) {
		String code = null;
		String codeAndValue = null;
		String[] values = null;

		if(delList!=null && delList.size()>0){
//			mSelecedStockerCodeAndValues=sh000001/1/上证指数/--/--/--/--;sz399001/1/深证成指/--/--/--/--;00992/5/联想集团/--/--/--/--;
			String mSelecedStockerCodeAndValues = getSharePreferenceinfo(SELECTED_VALUE_DRAG_XML);

//			mStrings[0]=sh000001/1/上证指数/--/--/--/--
			String[] mStrings = mSelecedStockerCodeAndValues.split(VALUES_SEPERATOR);

			StringBuilder CodesAndTypes = new StringBuilder();
			for(int i = 0; i < mStrings.length; i++) {
				CodesAndTypes.append(
						mStrings[i].split(VALUE_VALUE_SEPERATOR)[0])
							.append(VALUE_VALUE_SEPERATOR)
							.append(mStrings[i].split(VALUE_VALUE_SEPERATOR)[1])
							.append(VALUES_SEPERATOR);
				}
//			CodesAndTypes=selectedCodeAndTypes=sh000001/1;sz399001/1;00992/5;
			String selectedCodeAndTypes = new String(CodesAndTypes);
			Log.i("zzcao", "\n before DELETE  "+mSelecedStockerCodeAndValues+"  \n "+selectedCodeAndTypes);
			for(int i=0; i<delList.size(); i++){
				code = delList.get(i).toString();
				codeAndValue = StockLargeViewPort.stockerEnvelop.getMStockerCodeAndType(code);
				if(codeAndValue == null || codeAndValue.trim().isEmpty()) {
					for(int k = 0; k < mStrings.length; k++) {
						if(code.equalsIgnoreCase(mStrings[i].split(VALUE_VALUE_SEPERATOR)[0])){
							codeAndValue = code
								+ VALUE_VALUE_SEPERATOR
								+ mStrings[k].split(VALUE_VALUE_SEPERATOR)[1];
							break;
						}
					}
				}
				Log.i("zzcao", i+" removelist "+code+" "+codeAndValue);
				
				if (mSelecedStockerCodeAndValues != null
						&& mSelecedStockerCodeAndValues.trim().length() != 0) {
//					values[0]=sh000001/1/上证指数/--/--/--/--
					values = mSelecedStockerCodeAndValues.split(VALUES_SEPERATOR);

					//将删除的stock从dragXML中删除掉
					for (int j = 0; j < values.length; j++) {
						if (values[j].contains(codeAndValue)) {
							mSelecedStockerCodeAndValues = mSelecedStockerCodeAndValues
									.replace((values[j] + VALUES_SEPERATOR), NULLSTRING);
							break;
						}
					}
				}
				if (selectedCodeAndTypes != null) {
					selectedCodeAndTypes = selectedCodeAndTypes.replace((codeAndValue + VALUES_SEPERATOR), NULLSTRING);
					codelist.add(codeAndValue);
				}
				
				CodesAndTypes_drag = selectedCodeAndTypes;
				StockerEnvelop.fetchSynch.fetchTasks.remove(codeAndValue);
				StockerEnvelop.fetchSynch.fetchFailure.remove(codeAndValue);
				StockerEnvelop.fetchSynch.taskDatas.remove(codeAndValue);
				StockerEnvelop.fetchSynch.removeStockName(codeAndValue);

				StockLargeViewPort.stockerEnvelop.mStockerBitMap.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerChengjiaoliang.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerCodeAndType.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerCurrImage.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerDayImage.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerName.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerStype.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerTimeImage.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerZhangdiee.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerZhangdiefu.remove(code);
				StockLargeViewPort.stockerEnvelop.mStockerZuixin.remove(code);
			}
			Log.i("zzcao", "\n after DELETE  "+mSelecedStockerCodeAndValues+" \n CodesAndTypes_drag="+CodesAndTypes_drag);
			//更新SELECTED_VALUE_DRAG_XML
			if(mSelecedStockerCodeAndValues.length() > 0 && CodesAndTypes_drag != null) {
				ContentValues values3 = new ContentValues();
				values3.put(CODETYPE, SELECTED_VALUE_DRAG_XML);
				values3.put(CONRENT, mSelecedStockerCodeAndValues);
				cResource.insert(URI, values3);

				ContentValues values1 = new ContentValues();
				values1.put(CODETYPE, SELECTED_VALUE_XML);
				values1.put(CONRENT, mSelecedStockerCodeAndValues);
				cResource.insert(URI, values1);

				ContentValues values2 = new ContentValues();
				values2.put(CODETYPE, SELECED_CODES_AND_TYPES_XML);
				values2.put(CONRENT, CodesAndTypes_drag.toString());
				cResource.insert(URI, values2);
			}
			if(codelist != null && codelist.size() > 0) {
				Intent deletintent = new Intent(INTENT_DELET);
				Bundle bundle = new Bundle();
				bundle.putCharSequenceArrayList("stockid", codelist);
				bundle.putInt("WidgetId", WidgetId);
//				Toast.makeText(mContext, SelectStock+"==", 1).show();
				deletintent.putExtras(bundle);
				mContext.sendBroadcast(deletintent);
			}
		}
	}

	public class BUttonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.stockerADD) {
				leftPanel.setOpen(false, false);
				addlist();
			} else if(v.getId() == R.id.addviewback) {
//				if(selcodelist_del != null && selcodelist_del.size() > 0) {
//					for(int i = 0; i < selcodelist_del.size(); i++) {
//						String itemCode = selcodelist_del.get(i).toString().trim();
//						String codeAndType = StockLargeViewPort.stockerEnvelop.getMStockerCodeAndType(itemCode);
//						Log.i("searchDel", i+" itemCode= "+itemCode+" codeAndType= "+codeAndType);
//					}
//					removeByStockList(selcodelist_del);
//				}
//				if(selcodelist_add != null && selcodelist_add.size() > 0) {
//					StockerEnvelop stockerPEnvelop = new StockerEnvelop(mContext);
//					for(int i = 0; i < selcodelist_add.size(); i++) {
//						String itemCode = selcodelist_add.get(i).toString().trim();
//						String codeAndType = StockLargeViewPort.stockerEnvelop.getMStockerCodeAndType(itemCode);
//						stockerPEnvelop.addSelecedStockerValues(codeAndType, selcodelist_add_name.get(i).toString());
//						Log.i("searchAdd", i+" itemCode= "+itemCode+" codeAndType= "+codeAndType+" codeName="+selcodelist_add_name.get(i).toString());
//					}
//					SelectStock = StockLargeViewPort.stockerEnvelop.getMStockerCodeAndType(selcodelist_add.get(selcodelist_add.size()-1).toString());
//				} else {
//					Cursor cursor = cResource.query(URI, null, FIRST_SELECED_CODES_AND_TYPES_XML, null, WidgetId+NULLSTRING);
//					String[] temp = null;
//					if(cursor.moveToFirst()) {
//						temp = cursor.getColumnNames();
//					}
//					if(temp == null || selcodelist_del.contains(temp[COL_CODE].split(VALUE_VALUE_SEPERATOR)[COL_CODE])) {
//						temp[COL_CODE] = DEFAULT_CODE;
//					}
//					cursor.close();
//					SelectStock = temp[COL_CODE];
//				}
//				Log.i("czz", "SelectStock="+SelectStock);
				fetchFristInfo(mContext);
			} else if(v.getId() == R.id.stockerSET) {
//			 	initStocktListView();
				leftPanel.setOpen(false, false);
			 	Editlist();
			} else if(v.getId() == R.id.stockerListBack) {
				//退出list列表
				fetchFristInfo(mContext);
			} else if(v.getId() == R.id.stockerSetting) {
				//点击列表界面的编辑按钮
				Editlist();
//			}
//			else if(v.getId() == R.id.stockerItemViewBack) {
//				Log.d(TAG, " 11 return to small--"+WidgetId +"  "+SelectStock);
//				Intent intent = new Intent("com.android.launcher.hide.lenovo.widget.action");
//				mContext.sendBroadcast(intent);
//				if(SelectStock != null) {
//					Log.d(TAG, " 22 return to small--"+WidgetId +"  "+SelectStock);
//					ContentValues values2 = new ContentValues();
//					values2.put("widgetid", WidgetId+NULLSTRING);
//					values2.put("stockid",SelectStock);
//					cResource.insert(URI, values2);
//
//					Intent slectintent = new Intent(UPDATE_SAVE_COMPLETE);
//					Bundle bundle = new Bundle();
//					bundle.putString("stockerCode", SelectStock);
//					bundle.putInt("WidgetId", WidgetId);
//					slectintent.putExtras(bundle);
//					mContext.sendBroadcast(slectintent);
//				}
//				mContext.sendBroadcast(new Intent(INTENT_FINISH));
			} else if(v.getId() == R.id.fenshitu) {
				INDEX = 1;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mTimeImage
						, mItemStockerCode
						, SHOW_GN_TIMES_IMAGE);
			} else if(v.getId() == R.id.dayk) {
				INDEX = 2;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mDayKImage
						, mItemStockerCode
						, SHOW_GN_DAY_K_IMAGE);
			} else if(v.getId() == R.id.weekk) {
				INDEX = 3;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mWeekKImage
						, mItemStockerCode
						, SHOW_GN_WEEK_K_IMAGE);
			} else if(v.getId() == R.id.monthk) {
				INDEX = 4;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mMonthKImage
						, mItemStockerCode
						, SHOW_GN_MONTH_K_IMAGE);
			} else if(v.getId() == R.id.oneday) {
				INDEX = 1;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mDayImage
						, mItemStockerCode
						, SHOW_US_ONE_DAY_IMAGE);
			} else if(v.getId() == R.id.onemonth) {
				INDEX = 2;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mOneMonthImage
						, mItemStockerCode
						, SHOW_US_ONE_MONTH_IMAGE);
			} else if(v.getId() == R.id.threemonth) {
				INDEX = 3;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mThreeMonthImage
						, mItemStockerCode
						, SHOW_US_THREE_MONTH_IMAGE);
			} else if(v.getId() == R.id.oneyear) {
				INDEX = 4;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mOneYearImage
						, mItemStockerCode
						, SHOW_US_ONE_YEAR_IMAGE);
			} else if(v.getId() == R.id.curr) {
				INDEX = 5;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mCurrentImage
						, mItemStockerCode
						, SHOW_FUND_CURRENT_IMAGE);
			} else if(v.getId() == R.id.history) {
				INDEX = 6;
				updateStockItem();
				fetchImage(StockLargeViewPort.stockerEnvelop.mHistoryImage
						, mItemStockerCode
						, SHOW_FUND_HISTORY_IMAGE);
			} else if(v.getId() == R.id.stockerInfo) {
				try {
					Intent dateIntent = new Intent();
					dateIntent.setClassName("com.hexin.plat.android.gpad","com.hexin.plat.android.gpad.AndroidLogoActivity");
					dateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(dateIntent);
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else if(v.getId() == R.id.stockerKImageView) {
				try {
					Intent dateIntent = new Intent();
					dateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					dateIntent.setClassName("com.hexin.plat.android.gpad","com.hexin.plat.android.gpad.AndroidLogoActivity");
					mContext.startActivity(dateIntent);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class StockLargeListAdapter extends BaseAdapter {
		private Context mContext;
		private String[] names;
		private String index;
		private LayoutInflater inflater;

		public StockLargeListAdapter(Context mContext, String[] names,
				String index) {
			super();
			this.mContext = mContext;
			this.names = names;
			this.index = index;
			inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.stocklist_widget_large_listitem, null);
			}
			TextView stockName = (TextView) convertView.findViewById(R.id.stock_name);
			stockName.setText(names[position]);
			ImageView smallStockImg = (ImageView) convertView.findViewById(R.id.small_stock);
			if(index.equals(names[position].trim())){
				smallStockImg.setVisibility(VISIBLE);
				stockName.setTextColor(getResources().getColor(R.color.stock_one_text));
			} else {
				smallStockImg.setVisibility(GONE);
				stockName.setTextColor(getResources().getColor(R.color.stock_text_color));
			}
			return convertView;
		}
		
	}
	public class FetchStockerInfo extends Thread implements StockConstants {
		/**
		 * The code and type
		 */
		private String mSearchCode = "";

		private Context mContext;

		private boolean fetching = true;
		/**
		 * The timestamp of a thread
		 */
		private long mTimestamp = TIMESTAMP_FETCH_ALL;
		/**
		 * The stocks' list
		 */
		private String[] mValues = null;

		private static final int FETCHSTOCKER_INFO_TIMEOUT = 1;

		/**
		 * The post string
		 */
		private static final String POST_STR =
			"api_key=60bb4e103a0e425aaba000e60e5e000d&v=1.0&user=junju96@sina.com&password=f74a10e1d6b2f32a47b8bcb53dac5345&format=XML";

		private static final String URL = "http://3g.sina.com.cn/interface/f/stock/common/stock_info.php?wm=b012&cpage=1&ch=stock_info&s=";

		Handler mStockInfoHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case FETCHSTOCKER_INFO_TIMEOUT:
					// To save the data
					int finish = StockerEnvelop.fetchSynch.finishTask(mContext);
//					Log.d("zz", "handleMessage  FETCHSTOCKER_INFO_TIMEOUT finish="+finish+" REMIN="+REMIN);
					// If just take one stock Or have gotten all stocks, then refresh the stock widget screen
					if (finish != DONNOT_SAVE || StockLargeViewPort.stockerEnvelop.isItem) {
						if(StockLargeViewPort.REMIN == 1) {
							updateStockItem();
						} else if(StockLargeViewPort.REMIN == 2) {
							StockLargeViewPort.refreshList = true;
							updateStaticStockerInfo();
						}
					}
					Intent intent = new Intent(INTENT_NAME);
					if(StockLargeViewPort.REMIN == 1 && fetching){
						fetching = false;
						updateStockItem();
						fetchImage();
					}
				}
			}
		};

		public FetchStockerInfo(String codeAndType, long timestamp, Context context) {
			String[] codesAndValues = null;
			this.mTimestamp = timestamp;
			if(codeAndType != null
					&& codeAndType.trim().length() != 0) {
				codesAndValues = codeAndType.split(VALUE_VALUE_SEPERATOR);
				// such as s0000/1
				Log.i(TAG, "FetchStockerInfo codeAndType="+codeAndType);
				mSearchCode = codesAndValues[COL_CODE].trim() + VALUE_VALUE_SEPERATOR + codesAndValues[COL_TYPE].trim();
			}
			mContext = context;
		}

		/**
		 * Constructor
		 * @author wgz
		 * @date 2010-04-27
		 * @param values
		 * @param context
		 */
		public FetchStockerInfo(String[] values, Context context) {
			mValues = values;
			mContext = context;
		}

		public void run() {
			if (mTimestamp == -1 && mValues != null) {
				String[] codesAndValues = null;
				int len = mValues.length;

				for (int i = 0; i < len; i++) {
					codesAndValues = mValues[i].split(VALUE_VALUE_SEPERATOR);
					// such as s0000/1
					mSearchCode = codesAndValues[COL_CODE].trim() + VALUE_VALUE_SEPERATOR + codesAndValues[COL_TYPE].trim();

					//Save the stock's name
					StockerEnvelop.fetchSynch.saveStockName(mSearchCode, codesAndValues[COL_NAME].trim());

					//Record a task
					StockerEnvelop.fetchSynch.putFetchTask(mSearchCode, false);

					// To get stock info
					fetch(mSearchCode.replace(VALUE_VALUE_SEPERATOR, CODE_TYPE_SEPERATOR));
				}
				if (StockLargeViewPort.stockerEnvelop.isItem) {
					return;
				}
			} else {
				// To get stock info
				fetch(mSearchCode.replace(VALUE_VALUE_SEPERATOR, CODE_TYPE_SEPERATOR));

				long timestamp = StockLargeViewPort.stockerEnvelop.stockInfoTimestamp;
				if (timestamp != -1
						&& timestamp != this.mTimestamp
						&& StockLargeViewPort.stockerEnvelop.isItem) {
					return;
				}
			}
			mStockInfoHandler.sendEmptyMessage(FETCHSTOCKER_INFO_TIMEOUT);
		}

		private void fetch(String stockerCode) {
			boolean parseResult = false;
			boolean putTask = false;
			String stockName = null;
			String nullTag = mContext.getResources().getString(R.string.nullTag);

			String urlString = URL + Utils.replaceBlank(stockerCode);

			if (DEBUG) {
				Log.i(STOCK_TAG, "debug Stock 0430 by doobazgx@163.com  FetchStockInfo  Url======\n" 
						+ urlString);
//						+ " \n time is +++++++" 
//						+ System.currentTimeMillis());
			}
			try {
				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, TIMEOUT);

				HttpClient httpClient = new DefaultHttpClient(params);
				HttpGet httpGet = new HttpGet(urlString);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
					if(response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						InputStream instream = entity.getContent();
						parseResult = parseStocker(instream);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// If parsing failed, then save an invalid info
				if (!parseResult) {
					// Change the task's completion mask
					putTask = StockerEnvelop.fetchSynch.putFetchTask(mSearchCode, true);
					if (putTask) {
						String code = mSearchCode.split(VALUE_VALUE_SEPERATOR)[COL_CODE].trim();
						stockName = StockerEnvelop.fetchSynch.getStockName(mSearchCode);
						stockName = stockName == null ? code : stockName;
						// Save the failed stock info including code, type , name and null tags
						StockerEnvelop.fetchSynch.putTaskData(mSearchCode,
								mSearchCode
								+ VALUE_VALUE_SEPERATOR
								+ stockName
								+ VALUE_VALUE_SEPERATOR + nullTag
								+ VALUE_VALUE_SEPERATOR + nullTag
								+ VALUE_VALUE_SEPERATOR + nullTag
								+ VALUE_VALUE_SEPERATOR + nullTag);
						StockLargeViewPort.stockerEnvelop.putMStockerName(code, stockName);
						StockLargeViewPort.stockerEnvelop.setMStockerZuixin(code, nullTag);
						StockLargeViewPort.stockerEnvelop.setMStockerZhangdiee(code, nullTag);
						StockLargeViewPort.stockerEnvelop.setMStockerZhangdiefu(code, nullTag);
						StockLargeViewPort.stockerEnvelop.setMChengjiaoliang(code, nullTag);
						// Record the failed task
						StockerEnvelop.fetchSynch.putFetchFailure(mSearchCode);
					}
				}
			} catch (Exception e) {
				if (DEBUG) {
					Log.i(STOCK_TAG, "debug Stock 0430 by doobazgx@163.com  FetchStockInfo  exception======" 
							+ e.getMessage()+ "+++++++" 
							+ System.currentTimeMillis());
				}
				e.printStackTrace();
				// Change the task's completion mask
				putTask = StockerEnvelop.fetchSynch.putFetchTask(mSearchCode, true);
				if (putTask) {
					String code = mSearchCode.split(VALUE_VALUE_SEPERATOR)[COL_CODE].trim();
					stockName = StockerEnvelop.fetchSynch.getStockName(mSearchCode);
					stockName = stockName == null ? code : stockName;

					// If parsing failed, then save an invalid info
					StockerEnvelop.fetchSynch.putTaskData(mSearchCode,
							mSearchCode
							+ VALUE_VALUE_SEPERATOR
							+ stockName
							+ VALUE_VALUE_SEPERATOR + nullTag
							+ VALUE_VALUE_SEPERATOR + nullTag
							+ VALUE_VALUE_SEPERATOR + nullTag
							+ VALUE_VALUE_SEPERATOR + nullTag);
					StockLargeViewPort.stockerEnvelop.putMStockerName(code, stockName);
					StockLargeViewPort.stockerEnvelop.setMStockerZuixin(code, nullTag);
					StockLargeViewPort.stockerEnvelop.setMStockerZhangdiee(code, nullTag);
					StockLargeViewPort.stockerEnvelop.setMStockerZhangdiefu(code, nullTag);
					StockLargeViewPort.stockerEnvelop.setMChengjiaoliang(code, nullTag);
					// Record the failed task
					StockerEnvelop.fetchSynch.putFetchFailure(mSearchCode);
				}
			}
		}

		boolean parseStocker(InputStream mInputStream) {
			if (null == mInputStream)
				return false;
			String mStockerCode = null;
			String mStockerName = null;
			String mStockerZuixin = null;
			String mStokcerZhangdiee = null;
			String mStockerZhangdiefu = null;
			String mStockerChengjiaoliang = null;
			String zuixin = null;
			String city= null;
			String tmp = null;
			String formatStr = null;
			boolean parseRet = false;
			boolean putTask = false;

			String nullTag = mContext.getResources().getString(R.string.nullTag);
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
						if (tag.equals("stock")) {
							
						} else if (tag.equals("code")) {
							xpp.next();
							mStockerCode = mSearchCode.split(VALUE_VALUE_SEPERATOR)[COL_CODE].trim();
						} else if (tag.equals("name")) {
							xpp.next();
							mStockerName = xpp.getText();
							mStockerName = mStockerName == null ? mStockerCode : mStockerName.trim().replace(" ", "");
							StockLargeViewPort.stockerEnvelop.putMStockerName(mStockerCode, mStockerName);
						} else if (tag.equals("stype")) {
							xpp.next();
							stype = xpp.getText();
							StockLargeViewPort.stockerEnvelop.putMStockerStype(mStockerCode, stype == null ? null : stype.trim());
						} else if (tag.equals("city")) {
							xpp.next();
							city = xpp.getText();
							StockLargeViewPort.stockerEnvelop.putMStockerCity(mStockerCode, city == null ? null : city.trim());

							Log.i("set", city);
							Log.i("set", mStockerCode);
						} else if (tag.equals("zuixin")) {
							xpp.next();
							zuixin = xpp.getText() == null ? null : xpp.getText().trim();
							if (zuixin != null && !INVALID_VALUE.equals(zuixin)) {
								formatStr =  Utils.formatDoubleSize(zuixin);
							} else {
								formatStr = nullTag;
							}
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.sStockerZuixin = formatStr;
							}
							StockLargeViewPort.stockerEnvelop.setMStockerZuixin(mStockerCode, formatStr);
							mStockerZuixin = formatStr;
						} else if (tag.equals("zhangdiee")) {
							xpp.next();
							tmp = xpp.getText() == null ? null : xpp.getText().trim();
							if (!(zuixin != null && zuixin.trim().equals(INVALID_VALUE)) && tmp != null && !INVALID_VALUE.equals(tmp) ) {
								formatStr =  Utils.formatDoubleSize(tmp);
							} else {
								formatStr = nullTag;
							}
							StockLargeViewPort.stockerEnvelop.setMStockerZhangdiee(mStockerCode, formatStr);
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.sStockerZhangdiee = formatStr;
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
							StockLargeViewPort.stockerEnvelop.setMStockerZhangdiefu(mStockerCode, formatStr);
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.sStockerZhangdiefu = formatStr;
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
							StockLargeViewPort.stockerEnvelop.setMChengjiaoliang(mStockerCode, formatStr);
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.sStockerChengjiaoliang = formatStr;
							}
							mStockerChengjiaoliang = formatStr;
						} else if (tag.equals("zuoshou")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								if (tmp != null && !INVALID_VALUE.equals(tmp)) {
									formatStr =  tmp;
								}else {
									formatStr = nullTag;
								}
								StockLargeViewPort.stockerEnvelop.sStockerZuoshou = formatStr;
							}
						} else if (tag.equals("jinkai")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								if (tmp != null && !INVALID_VALUE.equals(tmp)) {
									formatStr =  tmp;
								} else {
									formatStr = nullTag;
								}
								StockLargeViewPort.stockerEnvelop.sStockerJinkai = formatStr;
							}
						} else if (tag.equals("zuidi")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								if (tmp != null && !INVALID_VALUE.equals(tmp)) {
									formatStr =  tmp;
								} else {
									formatStr = nullTag;
								}
								StockLargeViewPort.stockerEnvelop.sStockerZuidi = formatStr;
							}
						} else if (tag.equals("zuigao")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								if (tmp != null && !INVALID_VALUE.equals(tmp)) {
									formatStr =  tmp;
								} else {
									formatStr = nullTag;
								}
								StockLargeViewPort.stockerEnvelop.sStockerZuigao = formatStr;
							}
						} else if (tag.equals("chengjiaoe")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								if (tmp != null && !INVALID_VALUE.equals(tmp)) {
									formatStr =  Utils.formatSize(tmp);
								} else {
									formatStr = nullTag;
								}
								StockLargeViewPort.stockerEnvelop.sStockerChengjiaoe = formatStr;
							}
						} else if (tag.equals("open")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								if (!(zuixin != null && zuixin.equals(INVALID_VALUE)) && tmp != null && !INVALID_VALUE.equals(tmp)) {
									StockLargeViewPort.stockerEnvelop.sOpen = tmp;
								} else {
									StockLargeViewPort.stockerEnvelop.sOpen = STATUS_SUSPENSION;
								}
							}
						} else if (tag.equals("time_image")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							StockLargeViewPort.stockerEnvelop.setMStockerTimeImage(mStockerCode, tmp);
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mTimeImage = tmp;
							}
						} else if (tag.equals("day_k_image")) {
							xpp.next();
							StockLargeViewPort.stockerEnvelop.mDayKImage = (xpp.getText() == null ? null : xpp.getText().trim());
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								
							}
						} else if (tag.equals("week_k_image")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mWeekKImage = (xpp.getText() == null ? null : xpp.getText().trim());
							}
						} else if (tag.equals("month_k_image")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mMonthKImage = (xpp.getText() == null ? null : xpp.getText().trim());
							}
						} else if (tag.equals("day_image")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							StockLargeViewPort.stockerEnvelop.setMStockerDayImage(mStockerCode, tmp);
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mDayImage = tmp;
							}
						} else if (tag.equals("one_month_image")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mOneMonthImage = (xpp.getText() == null ? null : xpp.getText().trim());
							}
						} else if (tag.equals("three_month_image")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mThreeMonthImage = (xpp.getText() == null ? null : xpp.getText().trim());
							}
						} else if (tag.equals("one_year_image")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mOneYearImage = (xpp.getText() == null ? null : xpp.getText().trim());
							}
						} else if (tag.equals("curr_image")) {
							xpp.next();
							tmp = (xpp.getText() == null ? null : xpp.getText().trim());
							StockLargeViewPort.stockerEnvelop.setMStockerCurrImage(mStockerCode, tmp);
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mCurrentImage = tmp;
							}
						} else if (tag.equals("history_image")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mHistoryImage = (xpp.getText() == null ? null : xpp.getText().trim());
							}
						} else if (tag.equals("updatetime")) {
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mStockerUpdateTime = (xpp.getText() == null ? nullTag : xpp.getText().trim());
							}
						} else if(tag.equals("date")){
							xpp.next();
							if (this.mTimestamp != -1 && StockLargeViewPort.stockerEnvelop.stockInfoTimestamp == mTimestamp) {
								StockLargeViewPort.stockerEnvelop.mStockerUpdateTime = (xpp.getText() == null ? nullTag : xpp.getText().trim());
							}
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						// We have a comlete item -- post it back to the UI
						// using the mHandler (necessary because we are not
						// running on the UI thread).
						String tag = xpp.getName();
						if (tag.equals("stock")) {
							mStockerValues
								.append(mSearchCode)
								.append(VALUE_VALUE_SEPERATOR)
								.append(mStockerName)
								.append(VALUE_VALUE_SEPERATOR)
								.append(mStockerZuixin)
								.append(VALUE_VALUE_SEPERATOR)
								.append(mStokcerZhangdiee)
								.append(VALUE_VALUE_SEPERATOR)
								.append(mStockerZhangdiefu)
								.append(VALUE_VALUE_SEPERATOR)
								.append(mStockerChengjiaoliang);
							// Change the task's completion mask
							putTask = StockerEnvelop.fetchSynch.putFetchTask(mSearchCode, true);
							if (putTask) {
								// Save the stock's info
								StockerEnvelop.fetchSynch.putTaskData(mSearchCode, mStockerValues.toString());
								LenovoLeosStockWidgetProvier.CodeString = mStockerValues.toString();
								mStockerValues = new StringBuffer();
								parseRet = true;
							}
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

	public class FetchStockerImage extends Thread implements StockConstants {
		private String mStockerImageUrl = "";
		private String mStockerCode = "";
		private Context mContext;
		private static final int FETCHSTOCKER_IMAGE_TIMEOUT = 2;
		private long mTimestamp = -1L;
		private static final String POST_STR = 
			"api_key=60bb4e103a0e425aaba000e60e5e000d&v=1.0&user=junju96@sina.com&password=f74a10e1d6b2f32a47b8bcb53dac5345&format=XML";

		Handler mImageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case FETCHSTOCKER_IMAGE_TIMEOUT:
					Log.d(TAG, "mImageHandler--------------FetchStockerImage OK");
					updataStockItem();
				}
			}
		};

		public FetchStockerImage(String stockerCode, String stockerImageUrl, long timestamp, Context context) {
			mStockerImageUrl = stockerImageUrl;
			mStockerCode = stockerCode;
			mContext = context;
			mTimestamp = timestamp;
		}

		public void run() {
			int responseCode = fetch(mStockerImageUrl);
			long timestamp = StockLargeViewPort.stockerEnvelop.imageTimestamp;
			if (timestamp != TIMESTAMP_FETCH_ALL && mTimestamp != timestamp) {
				return;
			}
			if (responseCode == RET_SUCCESS) {
				if(StockLargeViewPort.REMIN == 1) {
					mImageHandler.sendEmptyMessage(FETCHSTOCKER_IMAGE_TIMEOUT);
				}
			} else {
				sendFailureBroadcast();
			}
		}

		private int fetch(String stockerUrl) {
			int responseCode = 0;
			InputStream stockStream = null;

			if (DEBUG) {
				Log.i(STOCK_TAG,
						"debug Stock 0430 by doobazgx@163.com  FetchStockImage  Url======"
						+ stockerUrl);
			}
			if(stockerUrl == null || stockerUrl.trim().length() ==0) {
				return 0;
			}
			try {
				// Clear stored bitmap
				StockLargeViewPort.stockerEnvelop.mStockerBitMap.clear();

				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, TIMEOUT);
				HttpClient httpClient = new DefaultHttpClient(params);
				HttpGet httpGet = new HttpGet(stockerUrl);
				HttpResponse response = null;

				try {
					response = httpClient.execute(httpGet);
					responseCode = response.getStatusLine().getStatusCode();
					if(responseCode == 200) {
						HttpEntity entity = response.getEntity();
						InputStream instream = entity.getContent();
						stockStream = instream;
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Read and decode the input stream into a bitmap image
				Bitmap bmImg = BitmapFactory.decodeStream(stockStream);
				// Save the maps of the codes and bitmap image
				StockLargeViewPort.stockerEnvelop.putMStockerBitMap(mStockerCode, bmImg);
			} catch (Exception e) {
				e.printStackTrace();
				if (DEBUG) {
					Log.e(STOCK_TAG, "debug Stock 0430 by doobazgx@163.com  FetchStockImage failed ==" + e.getMessage());
				}
			}
			return responseCode;
		}

		public void sendFailureBroadcast() {
			Intent intent = new Intent(UPDATE_IMAGE_FAILURE);
			intent.putExtra("stockCode", mStockerCode);
			mContext.sendBroadcast(intent);
		}
	}

	@Override
	public void onPanelClosed(Panel panel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPanelOpened(Panel panel) {
		// TODO Auto-generated method stub
		
	}
}
