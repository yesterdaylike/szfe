package com.huige.tzfe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "history.db";  
	private static final int DATABASE_VERSION = 1; 
	private SQLiteDatabase db;  

	public HistoryDB(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	}

	static String month = "month";
	static String day = "day";
	static String date = "date";
	static String step = "step";
	static String score = "score";
	static String maxnumber = "maxnumber";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS history" +  
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, month TEXT, day TEXT, date TEXT, step TEXT, score TEXT, maxnumber TEXT)");  
	}

	public void add(int month, int day, long date, int step, int score, int maxnumber){
		if( db == null ){
			db = getWritableDatabase();
		}
		db.execSQL("INSERT INTO history VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[]{month, day, date, step, score, maxnumber}); 
		//db.close();
	}

	public Cursor query(){
		if( db == null ){
			db = getWritableDatabase();
		}
		Cursor cursor = db.query("history", null, null, null, null, null, null); 
		//db.close();
		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
