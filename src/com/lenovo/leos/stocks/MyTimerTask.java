package com.lenovo.leos.stocks;

import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public  class MyTimerTask extends TimerTask{
	Context mContext;
	Intent  intent;
	public MyTimerTask(Context mContext,Intent intent){
		this.mContext=mContext;
		this.intent=intent;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//		boolean screenOn = pm.isScreenOn();
//		Log.i("pm", screenOn+"");
//		if(screenOn){
		mContext.sendBroadcast(intent);
//		}
	}
	
	
}