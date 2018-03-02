package com.lenovo.leos.stocks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

public class LenovoLeosStockWidgetProvier extends AppWidgetProvider implements StockConstants {

	final static String TAG = "Small";
	static final ComponentName THIS_APPWIDGET =
		new ComponentName("com.lenovo.leos.stocks",
				"com.lenovo.leos.stocks.LenovoLeosStockWidgetProvier");
	static Context context;
	public static String  CodeString = null;
	public static Boolean Notify = false;
	public static List<Integer> reList;
	public static Map<String,TimerTasks> TaskMap = new HashMap<String,TimerTasks>();
//	public static Timer mTimer;
	final static int BUTTON_STOCK_ONE = 100;
	private View layout;
	private WindowManager.LayoutParams wmParams;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		this.context = context;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.context = context;
//		if(mAlarmManager == null){
//			Intent intetns = new Intent(context, StockSmallWidgetProvier.class);//
//			intetns.setAction(INTENT_FETCHS);
//			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intetns, 0);
//			mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//			mAlarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+1000*11,1000*11, pendingIntent);
//		}

		/*if(mTimer == null) {
			Intent intetns = new Intent(context,LenovoLeosStockWidgetProvier.class);
			intetns.setAction(INTENT_FETCHS);
			mTimer = new Timer();
			MyTimerTask timerTask = new MyTimerTask(context, intetns);
			mTimer.schedule(timerTask, 60000*11, 60000*11);
		}*/
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
			Uri data = intent.getData();
			int id = Integer.parseInt(data.getSchemeSpecificPart());
			int buttonid = id % 1000;
			Log.i(TAG, "alternative==================["+id/1000+"]");
			if(buttonid == BUTTON_STOCK_ONE) {
				int wigdetid = id / 1000;
				Rect rect = intent.getSourceBounds();
//				Rect rect2 = getArea(context, rect);
//				popupWindow(context, rect, wigdetid);
				Log.i(TAG, "startDialog (popUpWindow) here==================["+wigdetid+"]");
				startDialog(context, rect, wigdetid);
			}
		}

		String action = intent.getAction();
		Log.i(TAG, "---Action is =================="+action);
		if(null == action) {
			return;
		}

		boolean ConnectState = getNetConnectState(context);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int [] ids = appWidgetManager.getAppWidgetIds(THIS_APPWIDGET);

		clearTaskMap(context, ids);
		SharedPreferences preferences =
			context.getSharedPreferences(PREFERENCES, Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);

		if(action.equals(UPDATE_SAVE_COMPLETE)) {
			Bundle bundle = intent.getExtras();
			String firstnames = bundle.getString("stockerCode");
			int WidgetId = bundle.getInt("WidgetId");
			TimerTasks NewTask = null;
			Boolean containstaskKey = TaskMap.containsKey(WidgetId+NULLSTRING);
			if(containstaskKey) {
				TimerTasks task = TaskMap.get(WidgetId+NULLSTRING);
				Log.i(TAG, "taskID= "+task.getId());
				if(task != null && task.isAlive()) {
					task.setStatues(false);
					Log.i(TAG, "End former task and restart new task");
					TaskMap.remove(WidgetId+NULLSTRING);
				}
			}
			boolean isstart = false;
			for(int id:ids) {
				if(id == WidgetId) {
					isstart = true;
				}
			}
			if(isstart) {
				RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, WidgetId);
				NewTask = new TimerTasks(context, WidgetId, views);
				NewTask.start();
				TaskMap.put(WidgetId+NULLSTRING, NewTask);
			}

		} else if(action.equals(STANDBY)) {
			if(ConnectState) {
				for(int id:ids) {
					TimerTasks NewTask = null;
					RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, id);
					NewTask = new TimerTasks(context,id,views);
					NewTask.start();
					TaskMap.put(id+NULLSTRING, NewTask);
				}
			} else {
				for(int id:ids) {
					RemoteViews nullviews = getRemoteViews(context, R.layout.stockerlist_one_null, id);
					appWidgetManager.updateAppWidget(id,nullviews);
				}
			}
		} else if(action.equals(INTENT_FETCHS)) {
			Log.i(TAG, TaskMap.size()+ "======ssssssss==0==");
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Beijing"));
//			Calendar c = Calendar.getInstance(Locale.CHINA);
			int hour = c.getTime().getHours();
			int mint = c.getTime().getMinutes();
			if(Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)) {
				Iterator it = TaskMap.entrySet().iterator();
				Log.i(TAG, TaskMap.size()+ "======ssssssss==1==");
				while(it.hasNext()) {
					Map.Entry m = (Map.Entry)it.next();
					TimerTasks task = (TimerTasks)m.getValue();
					if(task != null && task.isAlive()) {
						task.setStatues(false);
					}
				}
				Notify = true;
			}
//			if(xiaoshi<9 || ((xiaoshi>=11&&fenzhong>=45)  && (xiaoshi==12&&fenzhong>45)) || (xiaoshi>=15&&fenzhong>=15))
//			else if(c.getTime().getMinutes()==11||c.getTime().getMinutes()==14)
			else if(hour < 9 || (hour >= 11 && mint >= 45) && ((hour == 12) && (mint > 45)) || (hour >= 15 && mint >= 15)) {
				Log.i(TAG, TaskMap.size()+ "======ssssssss==2==");
				Iterator it = TaskMap.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry m = (Map.Entry)it.next();
					TimerTasks task = (TimerTasks)m.getValue();
					if(task != null && task.isAlive()) {
						task.setStatues(false);
					}
				}
				Notify = true;
			} else {
				if(Notify) {
					Log.i(TAG, TaskMap.size()+ "======ssssssss==3==");
					TaskMap.clear();
					Notify = false;
					Intent intents = new Intent(STANDBY);
					context.sendBroadcast(intents);
				}
			}
		} else if(action.equals(INTENT_DELET)) {
			Bundle bundle = intent.getExtras();
			ArrayList<CharSequence> stockid = bundle.getCharSequenceArrayList("stockid");
			int widgetid = bundle.getInt("WidgetId");

			DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			reList = new ArrayList<Integer>();

			for(int i = 0; i < stockid.size(); i++) {
				Cursor cursor = db.rawQuery("select * from stock where stockid=?",
						new String[]{stockid.get(i)+NULLSTRING.trim()});
				while(cursor.moveToNext()) {
					int Widgetid = cursor.getInt(cursor.getColumnIndex(WIDGETID));
					reList.add(Widgetid);
					Boolean containstaskKey = TaskMap.containsKey(Widgetid+NULLSTRING);
					if(containstaskKey) {
						TimerTasks task = TaskMap.get(Widgetid+NULLSTRING);
						if(task != null && task.isAlive()) {
							task.setStatues(false);
							TaskMap.remove(Widgetid+NULLSTRING);
						}
					}
				}
				db.execSQL("update stock set stockid=? where stockid=?",
	    				new Object[]{FRISTSTOCK, stockid.get(i)+NULLSTRING.trim()});
			}
			db.close();
			for(int id:ids) {
				if(reList.contains(id)) {
					RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, id);
					TimerTasks NewTask = new TimerTasks(context, id, views);
					NewTask.start();
					TaskMap.put(id+NULLSTRING, NewTask);
				}
			}
		} else if(action.equals(ACTION_WIDGET_SHOW)) {
			Log.i(TAG, "11+ConnectState is "+ConnectState);
			Log.i(TAG, TaskMap.size()+ "======ssssssss==0==");
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Beijing"));
//			Calendar c = Calendar.getInstance(Locale.CHINA);
			int hour = c.getTime().getHours();
			int mint = c.getTime().getMinutes();
			if(Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)) {
				Iterator it = TaskMap.entrySet().iterator();
				Log.i(TAG, TaskMap.size()+ "======ssssssss==1==");
				while(it.hasNext()) {
					Map.Entry m = (Map.Entry)it.next();
					TimerTasks task = (TimerTasks)m.getValue();
					if(task != null && task.isAlive()) {
						task.setStatues(false);
					}
				}
				Notify = true;
			}
//			if(xiaoshi<9 || ((xiaoshi>=11&&fenzhong>=45)  && (xiaoshi==12&&fenzhong>45)) || (xiaoshi>=15&&fenzhong>=15))
//			else if(c.getTime().getMinutes()==11||c.getTime().getMinutes()==14)
			else if(hour < 9 || (hour >= 11 && mint >= 45) && ((hour == 12) && (mint > 45)) || (hour >= 15 && mint >= 15)) {
				Log.i(TAG, TaskMap.size()+ "======ssssssss==2==");
				Iterator it = TaskMap.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry m = (Map.Entry)it.next();
					TimerTasks task = (TimerTasks)m.getValue();
					if(task != null && task.isAlive()) {
						task.setStatues(false);
					}
				}
				Notify = true;
			} else {
				if(Notify) {
					Log.i(TAG, TaskMap.size()+ "======ssssssss==3==");
					TaskMap.clear();
					Notify = false;
					Intent intents = new Intent(STANDBY);
					context.sendBroadcast(intents);
				} else {
					if(ConnectState) {
						for(int id:ids) {
							TimerTasks NewTask = null;
							Boolean containstaskKey = TaskMap.containsKey(id+NULLSTRING);
							Log.i(TAG, "11+containstaskKey is "+containstaskKey);
							if(!containstaskKey) {
								RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, id);
								NewTask = new TimerTasks(context,id,views);
								NewTask.start();
								TaskMap.put(id+NULLSTRING, NewTask);
							} else {
								TimerTasks task = TaskMap.get(id+NULLSTRING);
//								if(task != null && task.isAlive()) {
//									Log.i(TAG, "11+22");
//									String code = preferences.getString(id+NULLSTRING, NULLSTRING);
//									if(code != null && code.length() > 0) {
//										String[] codes = code.split(VALUES_SEPERATOR);
//										task.SmallonUpdateViews(context,
//												appWidgetManager,
//												new items(codes[0], codes[1], codes[2],codes[3], codes[4], codes[5]),
//												id);
//									}
//								} else {
									Log.i(TAG, "11+33");
									task.setStatues(false);
									TaskMap.remove(task);
									RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, id);
									TimerTasks newTask = new TimerTasks(context, id, views);
									newTask.start();
									TaskMap.put(id+NULLSTRING, newTask);
//								}
							}
						}
					} else {
						for(int id:ids) {
							TimerTasks NewTask = null;
							Boolean containstaskKey = TaskMap.containsKey(id+NULLSTRING);
							if(containstaskKey) {
								TimerTasks task = TaskMap.get(id+NULLSTRING);
								if(task != null && task.isAlive()) {
									task.setStatues(false);
									TaskMap.remove(id+NULLSTRING);
								}
							}
//							TaskMap.clear();
							RemoteViews nullviews = getRemoteViews(context, R.layout.stockerlist_one_null, id);
							appWidgetManager.updateAppWidget(THIS_APPWIDGET, nullviews);
						}
					}
				}
			}
                        Intent intents = new Intent(STOCKWIDGET_UPDATE);
                        context.sendBroadcast(intents);
			
		} else if(action.equals(CONNECTIVITY_CHANGE)) {
			if(!ConnectState) {
				for(int id:ids) {
					TimerTasks NewTask = null;
					Boolean containstaskKey = TaskMap.containsKey(id+NULLSTRING);
					if(containstaskKey) {
						TimerTasks task = TaskMap.get(id+NULLSTRING);
						if(task != null && task.isAlive()) {
							task.setStatues(false);
							TaskMap.remove(id+NULLSTRING);
						}
					}
					RemoteViews nullviews = getRemoteViews(context, R.layout.stockerlist_one_null, id);
					appWidgetManager.updateAppWidget(THIS_APPWIDGET, nullviews);
				}
			} else {
				for(int id:ids) {
					RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, id);
					TimerTasks NewTask= new TimerTasks(context,id,views);
					NewTask.start();
					String code = preferences.getString(id+NULLSTRING, NULLSTRING);
					if(code != null && code.length() > 0) {
						String[] codes = code.split(VALUES_SEPERATOR);
						NewTask.SmallonUpdateViews(context,
								appWidgetManager,
								new items(codes[0], codes[1], codes[2],codes[3], codes[4], codes[5]),
								id);
					}
					TaskMap.put(id+NULLSTRING, NewTask);
				}
			}
		} else if(action.equals(APPWIDGET_UPDATE) || action.equals(STOCKWIDGET_UPDATE)) {
			if(hasInstances(context)) {
				for(int id:ids) {
					if(ConnectState) {
						TimerTasks NewTask = null;
						Boolean containstaskKey = TaskMap.containsKey(id+NULLSTRING);
						if(!containstaskKey) {
							RemoteViews views = getRemoteViews(context, R.layout.stockerlist_one_small, id);
							NewTask = new TimerTasks(context,id,views);
							NewTask.start();
							String code = preferences.getString("deletstockcode", NULLSTRING);
							String stocks= preferences.getString(SELECED_CODES_AND_TYPES_XML,NULLSTRING);
							if(code != null && code.trim().length() > 0 && stocks.contains(code.split(VALUES_SEPERATOR)[6])) {
								String[] codes = code.split(VALUES_SEPERATOR);
								NewTask.SmallonUpdateViews(context,
										appWidgetManager,
										new items(codes[0], codes[1], codes[2],codes[3], codes[4], codes[5]),
										id);
							}
							TaskMap.put(id+NULLSTRING, NewTask);
						} else {
							TimerTasks task = TaskMap.get(id+NULLSTRING);
							if(task != null && task.isAlive()) {
								String code = preferences.getString(id+NULLSTRING, NULLSTRING);
								if(code != null && code.length() > 0) {
									String[] codes = code.split(VALUES_SEPERATOR);
									task.SmallonUpdateViews(context,
											appWidgetManager,
											new items(codes[0], codes[1], codes[2],codes[3], codes[4], codes[5]),
											id);
								}
							}
						}
					} else {
						RemoteViews nullviews = getRemoteViews(context, R.layout.stockerlist_one_null, id);
						appWidgetManager.updateAppWidget(THIS_APPWIDGET,nullviews);
					}
				}
			}
		}
		super.onReceive(context, intent);
	}

	private void clearTaskMap(Context context2, int[] ids) {
		for(int id:ids) {
			TimerTasks NewTask = null;
			Boolean containstaskKey = TaskMap.containsKey(id+NULLSTRING);
			if(containstaskKey) {
				TimerTasks task = TaskMap.get(id+NULLSTRING);
				if(task != null && (!task.getStatues()||(!task.isAlive()))) {
					TaskMap.remove(id);
				}
			}
		}
	}

//	private String getcontainsStock(int widgetId, Context cx) {
//		String FirstStock = null;
//		DBOpenHelper dbOpenHelper = new DBOpenHelper(cx);
//		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//		Cursor cursor = db.query(DBSTOCK, null, "widgetid=?", new String[]{widgetId+NULLSTRING}, null, null, null);
//		if(cursor.moveToFirst()) {
//			FirstStock = cursor.getString(cursor.getColumnIndex("stockid"));
//		}
//		if(FirstStock == null) {
//			FirstStock = DEFAULT_CODE;
//		}
//		db.close();
//
//		Log.i(TAG, "==========="+FirstStock);
//		return FirstStock;
//	}

	private boolean hasInstances(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(THIS_APPWIDGET);
		return (appWidgetIds.length > 0);
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

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.i(TAG, "onDeleted");
		for(int id:appWidgetIds) {
			Boolean containstaskKey = TaskMap.containsKey(id+NULLSTRING);
			if(containstaskKey) {
				TimerTasks task = TaskMap.get(id+NULLSTRING);
				Log.i(TAG, "deleteTaskId="+task.getId());
				if(task != null && task.isAlive()) {
					task.setStatues(false);
					TaskMap.remove(id+NULLSTRING);
				}
			}
			DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			String FirstStock = null;
			Cursor cursor = db.query(DBSTOCK, null, "widgetid=?", new String[]{id+NULLSTRING}, null, null, null);
			if(cursor.moveToFirst()) {
				FirstStock = cursor.getString(cursor.getColumnIndex("stockid"));
	 		}
			SharedPreferences preferences =
				context.getSharedPreferences(PREFERENCES, Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
			String deletStock = preferences.getString(id+NULLSTRING, DEFULTSTRING);
			preferences.edit().remove(id+NULLSTRING).commit();
			if(FirstStock != null) {
				preferences.edit().putString("deletstockcode", deletStock).commit();
			}
			db.delete(DBSTOCK, "widgetid=?", new String[]{id+NULLSTRING.trim().toString()});
			db.close();
		}
	}

	private static PendingIntent getLaunchPendingIntent(Context context, int appWidgetId, int buttonId) {
		Intent launchIntent = new Intent();
		launchIntent.setClass(context, LenovoLeosStockWidgetProvier.class);
		launchIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		launchIntent.setData(Uri.parse("custom:" + appWidgetId + buttonId));
		PendingIntent pi = PendingIntent.getBroadcast(context, 0 /* no requestCode */,
				launchIntent, 0 /* no flags */);
		Log.d(TAG, "getLaunchePendingIntent widgetid= " + appWidgetId +" buttonid= "+ buttonId);
		return pi;
	}

	private RemoteViews getRemoteViews(Context ctx, int resId, int widgetId) {
		RemoteViews rv = new RemoteViews(ctx.getPackageName(), resId);
		if(resId == R.layout.stockerlist_one_small)
			rv.setOnClickPendingIntent(R.id.small, getLaunchPendingIntent(ctx, widgetId, BUTTON_STOCK_ONE));
		else if (resId == R.layout.stockerlist_one_null)
			rv.setOnClickPendingIntent(R.id.small, getLaunchPendingIntent(ctx, widgetId, BUTTON_STOCK_ONE));
		return rv;
	}

	private void startDialog(Context context, Rect rect, int widgetid){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.setClass(context, DialogActivity.class);

		Bundle b = new Bundle();
		b.putInt("widgetid", widgetid);
		b.putInt("left", rect.left);
		b.putInt("top", rect.top);
		b.putInt("right", rect.right);
		b.putInt("bottom", rect.bottom);
		b.putInt("width", rect.width());
		b.putInt("height", rect.height());
		Log.d(TAG, rect.left+", "+rect.top+", "+rect.right+", "+rect.bottom+", "+rect.width()+", "+rect.height()+" ");
		intent.putExtras(b);
		context.startActivity(intent);
	}

//	protected void popupWindow(final Context ctx, final Rect rect, int widgetId) {
//		Log.d(TAG, "small area--=="+rect.left+" , "+rect.top+" , "+rect.right+" , "+rect.bottom);
//		layout = new LeosX_Large_Reflex_StockerListAppWidgetProvier(ctx, widgetId);
//		PopupWindow pp = new PopupWindow(ctx);
//		TextView tt = new TextView(ctx);
//		tt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//
//		layout.setOnTouchListener(new OnTouchListener(){
//
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				Log.d(TAG, "touch  layout-----=========== x="+event.getRawX()+" y="+event.getRawY());
//				float x = event.getRawX();
//				float y = event.getRawY();
//				if(x < rect.left || x > rect.left+383 || y < rect.top || y > rect.top+383){
//					Log.d(TAG, "outside");
//					disappear(ctx);
//				}
//				return true;
//			}
//
//		});
//
//		WindowManager wm=(WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
//		wmParams = new WindowManager.LayoutParams();
//		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//		wmParams.format = PixelFormat.RGBA_8888;
//		wmParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
////		wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//		int width = ctx.getResources().getDisplayMetrics().widthPixels;
//		int height = ctx.getResources().getDisplayMetrics().heightPixels;
//		Log.d(TAG, "device's width=" + width + "  h=" + height);
//		wmParams.width = width;
//		wmParams.height = height;
//		wmParams.gravity = Gravity.LEFT|Gravity.TOP;
////		layout.setPadding(rect.left, rect.top, width - rect.right, height - rect.bottom);
//		if(rect.left < width/2){
//			if(rect.top < height/2){
//				//left top
//				layout.setPadding(rect.left-30, rect.top-30, 0, 0);
//			} else {
//				//left down
//				layout.setPadding(rect.left-30, rect.top-30-383+rect.height(), 0, 0);
//			}
//		} else {
//			//right top
//			if(rect.top < height/2){
//				layout.setPadding(rect.left-30-383+rect.width(), rect.top-30, 0, 0);
//			} else {
//				//right down
//				layout.setPadding(rect.left-30-383+rect.width(), rect.top-30-383+rect.height(), 0, 0);
//			}
//		}
//		wm.addView(layout, wmParams);
//	}
//
//	protected Rect getArea(Context ctx, Rect rect) {
//		// TODO Auto-generated method stub
//		int left,top,right,bottom;
//		int width = ctx.getResources().getDisplayMetrics().widthPixels;
//		int height = ctx.getResources().getDisplayMetrics().heightPixels;
//		if(rect.left < width/2){
//			left = rect.left-30;
//		} else {
//			left = rect.left-30 - 383 + rect.width();
//		}
//		if(rect.top < height/2){
//			top = rect.top-30;
//		}else{
//			top = rect.top-30 - 383 + rect.height();
//		}
//		right = left+383;
//		bottom = top+383;
//		return new Rect(left, top, right, bottom);
//	}
//
//	private void disappear(Context ctx) {
//		if(layout != null && layout.isShown()) {
//			Log.d(TAG, "oncliclk-----shown----------------");
//			WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
//			wm.removeView(layout);
//		}
//	}

}
