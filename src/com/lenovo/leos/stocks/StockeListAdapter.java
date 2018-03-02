package com.lenovo.leos.stocks;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StockeListAdapter extends BaseAdapter {

	private Context mContext;
	private String[] names;
	private String[] stockcoders;
	private String[] zuixins;
	private String indexs;
	private int zhangdies;
	private LayoutInflater inflater;

	public StockeListAdapter(Context c, String[] names, String[] stockcoders, String[] zuixins, String indexstring, int zhangdie) {
		mContext = c;
		this.indexs = indexstring;
		this.names = names;
		this.stockcoders = stockcoders;
		this.zuixins = zuixins;
		this.zhangdies = zhangdie;
		inflater =(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return names.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_image_stocker,null);
		}

		TextView stockname 		=   (TextView) convertView.findViewById(R.id.stockerName);
		TextView stockerCode 	=   (TextView) convertView.findViewById(R.id.stockerCode);
		TextView stockerZuixin 	=   (TextView) convertView.findViewById(R.id.stockerZuixin);
		TextView stockerZuixin1 =   (TextView) convertView.findViewById(R.id.stockerZuixin1);
		TextView stockerZuixin2 =   (TextView) convertView.findViewById(R.id.stockerZuixin2);
		ImageView sanjiao 		=   (ImageView) convertView.findViewById(R.id.sanjiao);

		if(zuixins[position] == null || zuixins[position].equals("null")) {
			zuixins[position] = "--";
		}
		if(stockcoders[position] == null || stockcoders[position].equals("null")) {
			stockcoders[position] = "--";
		}

		stockname.setText(names[position]);
		stockerCode.setText(stockcoders[position]);
		stockerZuixin.setVisibility(View.GONE);
		stockerZuixin1.setVisibility(View.GONE);
		stockerZuixin2.setVisibility(View.VISIBLE);
		String st = zuixins[position];
		stockerZuixin2.setText(zuixins[position]);

		if(zhangdies == 0 || zhangdies == 1) {
			stockerZuixin2.setVisibility(View.GONE);

			if(st.contains("--")) {
				stockerZuixin2.setVisibility(View.VISIBLE);
			} else if(st.contains("-")) {
				stockerZuixin.setVisibility(View.GONE);
				stockerZuixin1.setVisibility(View.VISIBLE);
				stockerZuixin1.setText(zuixins[position]);
			} else if(st.contains("+")) {
				stockerZuixin.setVisibility(View.VISIBLE);
				stockerZuixin1.setVisibility(View.GONE);
				stockerZuixin.setText(zuixins[position]);
			} else {
				stockerZuixin.setVisibility(View.VISIBLE);
				stockerZuixin1.setVisibility(View.GONE);
				stockerZuixin.setText("+"+zuixins[position]);
			}
		}

		if(indexs.equals(names[position].trim())) {
			sanjiao.setBackgroundResource(R.drawable.sanjiao);
			StockLargeViewLand.tempname = names[position];
			StockLargeViewPort.tempname = names[position];
		} else {
			sanjiao.setBackgroundColor(Color.argb(0, 0, 255, 0));
		}

		return convertView;
	}
}