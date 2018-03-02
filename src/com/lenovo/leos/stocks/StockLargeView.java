package com.lenovo.leos.stocks;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

public abstract class StockLargeView extends LinearLayout {

	public StockLargeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public StockLargeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public StockLargeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setPaddings(int window_width, int window_height, int posx ,int posy) {
	}
	public abstract void clear();
	public EditText edit;
}
