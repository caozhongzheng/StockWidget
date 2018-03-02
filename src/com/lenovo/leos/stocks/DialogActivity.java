package com.lenovo.leos.stocks;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class DialogActivity extends Activity implements StockConstants {
	static final String TAG = "Dialog";

	public static int WID;
	public static int ORI;

	private int mleft;
	private int mtop;

	private static int WINDOW_WIDTH = 341;
	private static int WINDOW_HEIGHT = 261;
	private static final int WINDOW_WIDTH_SHADLE = 0;
	private static final int WINDOW_HEIGHT_SHADLE = 0;

	private static int window_screen_width ; 
	private static int window_screen_height ;
	private static float window_screen_desity ;

	StockLargeView mlayout;

	private int DURATIONTIME = 700;
	private Animation alphain;
	private Animation antialphain;

	private Animation inscaleanim;
	private Animation outscaleanim;

	private AnimationSet antianimationSet;
	private AnimationSet animationSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initregisterreceiver(); // DON'T forget to unregister receiver
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		float den = this.getResources().getDisplayMetrics().density;
		Log.i("den", ""+den);
		den = 1.5f;
		Configuration config = getResources().getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			ORI = LANDSCAPE;
			WINDOW_WIDTH = (int) (341*den+0.5);
			WINDOW_HEIGHT = (int) (261*den+0.5);
		} else if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			ORI = PORTRAIT;
			WINDOW_WIDTH = (int) (256*den+0.5);
			WINDOW_HEIGHT = (int) (341*den+0.5);
		}
		Log.i("den", WINDOW_WIDTH+" / "+WINDOW_HEIGHT);
		Bundle bundle = this.getIntent().getExtras();
		WID = bundle.getInt("widgetid", -1);
		final int left 		= bundle.getInt("left", -1);
		final int top 		= bundle.getInt("top", -1);
		final int right 	= bundle.getInt("right", -1);
		final int bottom 	= bundle.getInt("bottom", -1);
		final int width 	= bundle.getInt("width", -1);
		final int height 	= bundle.getInt("height", -1);
		window_screen_width  = getResources().getDisplayMetrics().widthPixels;
		window_screen_height = getResources().getDisplayMetrics().heightPixels;
		window_screen_desity = getResources().getDisplayMetrics().density;

		final Rect LEFT_TOP_RECT =
			new Rect(0, 0, window_screen_width/2, window_screen_height/2);
		final Rect LEFT_BOTTOM_RECT =
			new Rect(0, window_screen_height/2, window_screen_width/2, window_screen_height);
		final Rect RIGHT_TOP_RECT =
			new Rect(window_screen_width/2, 0, window_screen_width, window_screen_height/2);
		final Rect RIGHT_BOTTOM_RECT =
			new Rect(window_screen_width/2, window_screen_height/2, window_screen_width, window_screen_height);

		Log.d(TAG, "WID="+WID+" ORI="+ORI);
		Log.d(TAG, left+", "+top+", "+right+", "+bottom+", "+width+", "+height+" ");
//		ORI = PORTRAIT;
//		ORI = LANDSCAPE;
//		Toast.makeText(this, "WID="+WID+" ORI="+ORI, 0).show();
		if(ORI == LANDSCAPE) {
			mlayout = new StockLargeViewLand(DialogActivity.this);
		} else if(ORI == PORTRAIT) {
			mlayout = new StockLargeViewPort(DialogActivity.this);
		}
		//for 7 inch pad
		final int posx = left + (right-left)/2;
		final int posy = top + (bottom-top)/2;
		final int stateheight = getStatusHeights(this);
		if(LEFT_TOP_RECT.contains(posx, posy)){
			Log.d("a", "LEFT_TOP_RECT");
			mlayout.setPaddings(WINDOW_WIDTH, WINDOW_HEIGHT, mleft=left-WINDOW_WIDTH_SHADLE, mtop=top-WINDOW_HEIGHT_SHADLE-stateheight);
			applyAnimationSet(left, top-stateheight);
		}else if(LEFT_BOTTOM_RECT.contains(posx, posy)){
			Log.d("a", "LEFT_BOTTOM_RECT");
			mlayout.setPaddings(WINDOW_WIDTH, WINDOW_HEIGHT, mleft=left-WINDOW_WIDTH_SHADLE, mtop=bottom-WINDOW_HEIGHT+WINDOW_HEIGHT_SHADLE-stateheight);
			applyAnimationSet(left, bottom-stateheight);
		}else if(RIGHT_TOP_RECT.contains(posx, posy)){
			Log.d("a", "RIGHT_TOP_RECT");
			mlayout.setPaddings(WINDOW_WIDTH, WINDOW_HEIGHT, mleft=right-WINDOW_WIDTH+WINDOW_WIDTH_SHADLE, mtop=top-WINDOW_HEIGHT_SHADLE-stateheight);
			applyAnimationSet(right, top-stateheight);
		}else if(RIGHT_BOTTOM_RECT.contains(posx, posy)){
			Log.d("a", "RIGHT_BOTTOM_RECT");
			mlayout.setPaddings(WINDOW_WIDTH, WINDOW_HEIGHT, mleft=right-WINDOW_WIDTH+WINDOW_WIDTH_SHADLE, mtop=bottom-WINDOW_HEIGHT+WINDOW_HEIGHT_SHADLE-stateheight);
			applyAnimationSet(right, bottom-stateheight);
		}

		if(mlayout != null && animationSet != null) {
			mlayout.startAnimation(animationSet);
		}
		setContentView(mlayout);

		super.onCreate(savedInstanceState);
	}

	private void applyAnimationSet(int posx ,int posy) {
		antianimationSet = new AnimationSet(false);
		animationSet = new AnimationSet(false);

		alphain = new AlphaAnimation(0.0f, 1.0f);
		alphain.setDuration(DURATIONTIME);
		antialphain = new AlphaAnimation(1.0f, 0.0f);
		antialphain.setDuration(DURATIONTIME);

		inscaleanim = new ScaleAnimation(0.0f ,1.0f, 0.0f ,1.0f,posx,posy);
		inscaleanim.setDuration(DURATIONTIME);
		outscaleanim = new ScaleAnimation(1.0f ,0.0f, 1.0f ,0.0f,posx,posy);
		outscaleanim.setDuration(DURATIONTIME);

        animationSet.setInterpolator(new AccelerateInterpolator(2.0f));
        animationSet.addAnimation(alphain);
        animationSet.addAnimation(inscaleanim);

        antianimationSet.setInterpolator(new AccelerateInterpolator(2.0f));
        antianimationSet.addAnimation(antialphain);
        antianimationSet.addAnimation(outscaleanim);

        antianimationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }
            public void onAnimationEnd(Animation animation) {
            	finish();
            }
            public void onAnimationRepeat(Animation animation) {}
        });
	}
	public static int getStatusHeights(Context context){
        int statusHeight = 0;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }catch(Exception e){
            	 e.printStackTrace();
            }
        }
        return statusHeight;
    }
	@Override
	public void onBackPressed() {
		Log.d("and", "onBackPressed");
		//二态窗口消失的动画(back键)
		if(!antianimationSet.hasStarted())
			mlayout.startAnimation(antianimationSet);
	}

	@Override
	protected void onPause() {
		Log.d("and", "onPause");
		super.onPause();
		// TODO Auto-generated method stub
		updateSmall(false);
		mlayout.clear();
		finish();
//		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(widgetinforeceiver != null) {
			this.unregisterReceiver(widgetinforeceiver);
		}
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d("and", "onConfigurationChanged");
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mlayout.startAnimation(antianimationSet);
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("and", "onTouchEvent");
		RectF rect = new RectF(mleft, mtop, mleft+WINDOW_WIDTH, mtop+WINDOW_HEIGHT);
		if(!rect.contains(event.getX(), event.getY())){
			if(!antianimationSet.hasStarted()){
				if(mlayout.edit != null) {
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(mlayout.edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
				mlayout.startAnimation(antianimationSet);
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

	public void initregisterreceiver() {
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("ACTION_SCREEN_SNAP");
		registerReceiver(widgetinforeceiver, intentfilter);
	}

	private BroadcastReceiver widgetinforeceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			if(INTENT_FINISH.equalsIgnoreCase(intent.getAction()))
					quit();
		}
	};

	protected void quit() {
		// TODO Auto-generated method stub
//		LeosX_Large_Reflex_StockerListAppWidgetProvier.clear();
		
		finish();
	}

	public void updateSmall(boolean overturn) {
		Log.d("and", "updateSmall---ORI= "+ORI);
		int mORI = -1;
		if(overturn) {
			mORI = ORI == LANDSCAPE ? PORTRAIT : LANDSCAPE;
		} else {
			mORI = ORI;
		}
		if(mORI == LANDSCAPE && StockLargeViewLand.SelectStock != null) {
			Log.d(TAG, "updateSmall--WidgetID= "+WID+" --stockid= "+StockLargeViewLand.SelectStock);
			Log.d("and", "updateSmall-Land-WidgetID= "+WID+" --stockid= "+StockLargeViewLand.SelectStock);
			ContentValues values2 = new ContentValues();
			values2.put("widgetid", WID+NULLSTRING);
			values2.put("stockid", StockLargeViewLand.SelectStock);
			StockLargeViewLand.cResource.insert(URI, values2);
			StockLargeViewPort.SelectStock = null;

			Intent slectintent = new Intent(UPDATE_SAVE_COMPLETE);
			Bundle bundle = new Bundle();
			bundle.putString("stockerCode", StockLargeViewLand.SelectStock);
			bundle.putInt("WidgetId", WID);
			slectintent.putExtras(bundle);
			this.sendBroadcast(slectintent);
		} else
		if(mORI == PORTRAIT && StockLargeViewPort.SelectStock != null) {
			Log.d("and", "updateSmall-Port-WidgetID= "+WID+" --stockid= "+StockLargeViewPort.SelectStock);
			Log.d(TAG, "updateSmall--WidgetID= "+WID+" --stockid= "+StockLargeViewPort.SelectStock);
			ContentValues values2 = new ContentValues();
			values2.put("widgetid", WID+NULLSTRING);
			values2.put("stockid", StockLargeViewPort.SelectStock);
			StockLargeViewPort.cResource.insert(URI, values2);
			StockLargeViewLand.SelectStock = null;

			Intent slectintent = new Intent(UPDATE_SAVE_COMPLETE);
			Bundle bundle = new Bundle();
			bundle.putString("stockerCode", StockLargeViewPort.SelectStock);
			bundle.putInt("WidgetId", WID);
			slectintent.putExtras(bundle);
			this.sendBroadcast(slectintent);
		}
	}
}
