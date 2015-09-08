package com.popo.dailyopen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class RecordHelper {

	private static String TAG = "RecordHelper";
	private static boolean OUTPUT_DB = false;
	private RecordDatabase mRecordDatabase;
	private SQLiteDatabase mWriteRecord;
	private SQLiteDatabase mReadRecord;
	private Context mContext;

	public RecordHelper(Context context) {
		mContext = context;
		mRecordDatabase = new RecordDatabase(mContext, Def.APP_NAME, null,
				Def.DB.VERSION);
		mWriteRecord = mRecordDatabase.getWritableDatabase();
		mReadRecord = mRecordDatabase.getReadableDatabase();
	}

	public void addDailyRecord(String id, long openTime, long closeTime) {

		ContentValues values = new ContentValues();
		values.put(Def.DB.RECORD_ID, id);
		values.put(Def.DB.RECORD_OPEN_TIME, openTime);
		values.put(Def.DB.RECORD_CLOSE_TIME, closeTime);
		mWriteRecord.insert(Def.DB.RECORD_TABLE, null, values);
		outputDB();

	}

	public OpenDay getOneDayRecord(String id) {
		Cursor cursor;
		OpenDay oneDay = new OpenDay(id);
		String sql = "SELECT " + Def.DB.RECORD_OPEN_TIME + ","
				+ Def.DB.RECORD_CLOSE_TIME + " FROM " + Def.DB.RECORD_TABLE
				+ " WHERE " + Def.DB.RECORD_ID + "='" + id+"'";
		cursor = mReadRecord.rawQuery(sql, null);
		cursor.moveToFirst();
		int cursorSize = cursor.getCount();
		for (int i = 0; i < cursorSize; i++) {
			oneDay.addTime(cursor.getInt(0), cursor.getInt(1));
			cursor.moveToNext();
		}

		return oneDay;
	}

	public LinkedList<OpenDay> getHistoryRecord() {
		Cursor cursor;
		String date = "";
		LinkedList<OpenDay> manyDay = new LinkedList<OpenDay>();
		String sql = "SELECT " + Def.DB.RECORD_ID + ","
				+ Def.DB.RECORD_OPEN_TIME + "," + Def.DB.RECORD_CLOSE_TIME
				+ " FROM " + Def.DB.RECORD_TABLE+ " ORDER BY "+Def.DB.RECORD_ID+" DESC ";
		cursor = mReadRecord.rawQuery(sql, null);
		cursor.moveToFirst();

		int cursorSize = cursor.getCount();
		if (cursorSize == 0) {
			return null;
		}
		OpenDay day = null;
		for (int i = 0; i < cursorSize; i++) {
			String tmp_date = cursor.getString(0);
			if (!tmp_date.equals(date)) {
				date = tmp_date;
				if (day != null) {
					manyDay.add(day);
				}
				day = new OpenDay(date);
			}
			if (day == null) {
				Log.e(TAG, "day is null");
				break;
			}
			day.addTime(cursor.getInt(1), cursor.getInt(2));
			cursor.moveToNext();
			if(i == (cursorSize-1)){
				manyDay.add(day);
			}
			
		}
		return manyDay;
	}

	private void outputDB() { // for test
		if(OUTPUT_DB){
			Log.v(TAG, "Database Output");
			File file = new File(Environment.getDataDirectory()
					+ "/data/com.popo.dailyopen/databases/"
					+ mRecordDatabase.getDatabaseName());
			File fileBackup = new File(Environment.getExternalStorageDirectory(),
					"/obb/Test.db");
			if (file.exists()) {
				try {
					fileBackup.createNewFile();
					FileOutputStream out = new FileOutputStream(fileBackup);
					FileInputStream in = new FileInputStream(file);
	
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
				} catch (IOException e) {
	
					Log.v(TAG, "OutPut:" + e.getMessage());
				}
	
			}
		}

	}

}
