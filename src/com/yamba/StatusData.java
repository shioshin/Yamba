package com.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatusData {
	private static final String TAG = StatusData.class.getSimpleName();
	static final int VERSION = 1;
	static final String DATABASE = "timeline.db";
	static final String TABLE = "timeline";
	public static final String C_ID = "_id";
	public static final String C_CREATED_AT = "created_at";
	public static final String C_TEXT = "txt";
	public static final String C_USER = "user";
	private static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
	private static final String[] MAX_CREATED_AT_COLUMNS = { "max("
			+ StatusData.C_CREATED_AT + ")" };
	private static final String[] DB_TEXT_COLUMNS = { C_TEXT };

	private final DbHelper dbHelper;

	public StatusData(Context context) {
		this.dbHelper = new DbHelper(context);
		Log.i(TAG, "Initialized data");
	}

	public void close() {
		this.dbHelper.close();
	}

	public void insertOrIgnore(ContentValues values) {
		Log.d(TAG, "insertOrIgnore on " + values);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.insertOrThrow(TABLE, null, values);
		} catch (SQLException ignore) {
		} finally {
			db.close();
		}
	}

	public Cursor getStatusUpdates() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
	}

	public long getLatestStatusCreatedAtTime() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null, null,
					null, null);
			return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
		} catch (SQLException ignore) {

		} finally {
			cursor.close();
			db.close();
		}
		return Long.MIN_VALUE;
	}

	public String getStatusTextById(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, C_ID + "=" + id,
					null, null, null, null);
			return cursor.moveToNext() ? cursor.getString(0) : null;
		} catch (SQLException ignore) {

		} finally {
			cursor.close();
			db.close();
		}
		return null;
	}

	class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table " + TABLE + " (" + C_ID
					+ " int primary key, " + C_CREATED_AT + " int, " + C_USER
					+ " text, " + C_TEXT + " text)";

			db.execSQL(sql);
			Log.d(TAG, "onCreated sql: " + sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TABLE);
			Log.d(TAG, "onUpdated");
			onCreate(db);
		}

	}

}
