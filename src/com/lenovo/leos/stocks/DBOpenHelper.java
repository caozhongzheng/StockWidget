package com.lenovo.leos.stocks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "stock.db", null, 5);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE stock (id integer primary key autoincrement, stockid varchar(20), widgetid VARCHAR(12))");
	}
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE stock ADD amount integer");  
	}

}
