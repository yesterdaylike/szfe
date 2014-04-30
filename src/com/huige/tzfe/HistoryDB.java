package com.huige.tzfe;

import android.content.Context;
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

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS history" +  
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, month TEXT, day TEXT, date TEXT, step TEXT, score TEXT, maxnumber TEXT)");  
	}

	public void add(){
		db = getWritableDatabase();
		//db.execSQL("INSERT INTO history VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[]{person.name, person.age, person.info}); 
		db.close();
	}
	
	public void query(){
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
