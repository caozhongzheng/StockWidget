package com.lenovo.leos.stocks;

 
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class StockProvider extends ContentProvider implements StockConstants {
	private  SharedPreferences preferences ;
	private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int PERSONS = 1;
	private static final int PERSON = 2;
 
 
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
 
	}

	@Override
	public String getType(Uri uri) {
		switch (sMatcher.match(uri)) {
		case PERSONS:
			return "vnd.android.cursor.dir/person";
			
		case PERSON:
			return "vnd.android.cursor.item/person";
			
		default:
			throw new IllegalArgumentException("Unknown Uri:"+ uri);
		}
	}
	// /person
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		  
  		String temp=(String)values.get(WIDGETID);
		if(temp!=null&&temp.length()>0){
			
			String WidgetId=(String) values.get(WIDGETID);
			String selecedStocke=(String) values.get("stockid");
		
			DBOpenHelper	dbOpenHelper = new DBOpenHelper(this.getContext());
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	
			db.delete(DBSTOCK, "widgetid=?", new String[]{WidgetId+NULLSTRING.trim().toString()}); 
			ContentValues savevalues = new ContentValues();
			savevalues.put("stockid", selecedStocke);
			savevalues.put(WIDGETID, WidgetId);
			db.insert(DBSTOCK, null, savevalues); 
			db.close();
	  
		}else{
			
			String type=(String) values.get("type");
			String content=(String) values.get("content");
			preferences.edit().putString(type,content).commit();
			}
		Uri insertUri = ContentUris.withAppendedId(uri, 0); 
		getContext().getContentResolver().notifyChange(uri, null);
		return insertUri;
	}

	@Override
	public boolean onCreate() {
		preferences = this.getContext().getSharedPreferences(PREFERENCES,Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
		return true;
	}
 
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String[] temp =null;
         if(selection.equals(FIRST_SELECED_CODES_AND_TYPES_XML)){
    		DBOpenHelper	dbOpenHelper = new DBOpenHelper(this.getContext());
	 		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    		Cursor cursor = db.query(DBSTOCK, null, "widgetid=?", new String[]{sortOrder+NULLSTRING}, null, null, null);		 
    		String temps=null;
        	if(cursor.moveToFirst()){
				temps= cursor.getString(cursor.getColumnIndex("stockid"));
   	 		     }
        	if(temps==null){
        		 		temps=FRISTSTOCK;
        	 			}
	        	 String [] frist=new String[]{temps};
	        	 temp=frist;
	        	 cursor.close();
	        	 db.close();
//	        	 Log.i("=====first_selectedCodesAndTypes===================", temps+"=first_selectedCodesAndTypes");
        	
         }else if(selection.equals(SELECTED_VALUE_XML)){
    	  		temp=preferences.getString(selection, NULLSTRING).split(VALUES_SEPERATOR);
         }else if(selection.equals(SELECED_CODES_AND_TYPES_XML)){
    	  		temp=preferences.getString(selection, NULLSTRING).split(VALUES_SEPERATOR);
//        	  	Log.i(SELECED_CODES_AND_TYPES_XML, preferences.getString(selection, NULLSTRING));
         }else if(selection.equals(SELECTED_COLUMN)){
        	  	String frist=preferences.getString(SELECTED_COLUMN, "0");
        	  	String [] temps=new String[]{frist};
        	  	temp=temps;
         }else if(selection.equals(SELECTED_VALUE_DRAG_XML)){
    	  		temp=preferences.getString(SELECTED_VALUE_DRAG_XML, NULLSTRING).split(VALUES_SEPERATOR);
        	 
          }
         
         
         
         
 		MatrixCursor mc=new MatrixCursor(temp);
 		mc.addRow(temp);
		return mc;
	}
 
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
 
	}
}
