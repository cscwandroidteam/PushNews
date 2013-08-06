package com.pushnews.app.db;

import com.pushnews.app.cofig.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * 数据库创建类
 * */
public class DBHelper extends SQLiteOpenHelper {
	private final String TAG = "DBHelper";
	/** 频道表 */
	private final String CHANNL_TABLE = "create table "
			+ Constants.ChannelTable.TABLE_NAME + " ("
			+ Constants.ChannelTable.ID + " integer primary key, "
			+ Constants.ChannelTable.NEWS_TYPE + " text, "
			+ Constants.ChannelTable.MARK + " boolen)";
	/** 用户表 */
	private final String USER_TABLE = "create table "
			+ Constants.UserTable.TABLE_NAME + " (" + Constants.UserTable.ID
			+ " integer primary key, " + Constants.UserTable.USER_NAME
			+ " text, " + Constants.UserTable.PASSWORD + " text, "
			+ Constants.UserTable.MARK + " integer)";
	/** 新闻列表的表 */
	private final String NEWSLIST_TABLE = "create table "
			+ Constants.NewsListTable.TABLE_NAME + " ("
			+ Constants.NewsListTable.ID + " integer primary key, "
			+ Constants.NewsListTable.NEWS_TYPE + " text, "
			+ Constants.NewsListTable.NEWS_TITLE + " text, "
			+ Constants.NewsListTable.NEWS_SUMMRY + " text, "
			+ Constants.NewsListTable.NEWS_FROM + " text, "
			+ Constants.NewsListTable.NEWS_ID + " intger, "
			+ Constants.NewsListTable.NEWS_IMAGE_URL + " text, "
			+ Constants.NewsListTable.NEWS_DETAIL_URL + " text, "
			+ Constants.NewsListTable.NEWS_TIME + " text)";

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, Constants.DATABASE_NAME, null, Constants.Version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(CHANNL_TABLE);
			db.execSQL(USER_TABLE);
			db.execSQL(NEWSLIST_TABLE);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "Upgrading from version " + oldVersion + " to" + newVersion
				+ ",which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.ChannelTable.TABLE_NAME);
		db.execSQL("drop table if exists " + Constants.NewsListTable.TABLE_NAME);
		db.execSQL("drop table if exists " + Constants.UserTable.TABLE_NAME);
		onCreate(db);
	}

}
