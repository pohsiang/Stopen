package com.popo.dailyopen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;


public class RecordDatabase extends SQLiteOpenHelper {

	private static String TAG = "RecordDatabase";
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static String CREATE_RECORD_TABLE = "CREATE TABLE "+Def.DB.RECORD_TABLE+" ("+
												Def.DB.RECORD_ID+" TEXT,"+
												Def.DB.RECORD_OPEN_TIME+" INTEGRER,"+
												Def.DB.RECORD_CLOSE_TIME+" INTEGRER)";
	private static String INSERT_ONE_RECORD;

	
	public RecordDatabase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
