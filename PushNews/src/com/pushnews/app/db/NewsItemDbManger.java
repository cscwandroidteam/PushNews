package com.pushnews.app.db;

import com.pushnews.app.cofig.Constants;
import com.pushnews.app.model.NewsItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * @description 对新闻列表的数据库管理类
 * 
 * */
public class NewsItemDbManger {
	private final static String TAG = "NewsDbManger";
	private static SQLiteDatabase db;
	private static DBHelper dbHelper;

	public NewsItemDbManger(Context c) {
		dbHelper = new DBHelper(c, Constants.DATABASE_NAME, null,
				Constants.Version);
	}

	/**
	 * 关闭数据库
	 * 
	 * */

	public void close() {
		db.close();
	}

	/**
	 * 开启数据库
	 * */
	public void open() throws SQLiteException {

		try {
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage());
			db = dbHelper.getReadableDatabase();
		}

	}

	/**
	 * 增加表中数据
	 * 
	 * @param newsList
	 *            NewsList类
	 * 
	 * @return long 如果是正数则表示增加成功，反之不成功
	 * 
	 */
	public long addNews(NewsItem newsItem) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.NewsListTable.NEWS_TYPE,
					newsItem.getNewsType());
			contentValues.put(Constants.NewsListTable.NEWS_TITLE,
					newsItem.getNewsTitle());
			contentValues.put(Constants.NewsListTable.NEWS_SUMMRY,
					newsItem.getNewsSummary());
			contentValues.put(Constants.NewsListTable.NEWS_FROM,
					newsItem.getNewsFrom());
			contentValues.put(Constants.NewsListTable.NEWS_TIME,
					newsItem.getNewsTime());
			contentValues.put(Constants.NewsListTable.NEWS_ID,
					newsItem.getNews_id());
			contentValues.put(Constants.NewsListTable.NEWS_IMAGE_URL,
					newsItem.getImageUrl());
			return db.insert(Constants.NewsListTable.TABLE_NAME, null,
					contentValues);

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return -1;
		}

	}

	/**
	 * 删除表中的记录
	 * 
	 * @param whereClause
	 *            删除条件 如：( id>? and time>?)
	 * @param whereArgs
	 *            条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * @return 返回删除的条数 也可以作为判断值，如果是正数则表示删除成功，反之不成功
	 */
	public int deleteNews() {
		try {

			return db.delete(Constants.NewsListTable.TABLE_NAME, null, null);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage());
			return -1;
		}
	}

	/**
	 * 查找表中记录
	 * 
	 * @return Cursor
	 */
	public Cursor getdiaries() {
		/**
		 * 查询数据
		 * 
		 * @param table
		 *            表名
		 * @param columns
		 *            要查询的列名
		 * @param selection
		 *            查询条件 如：( id=?)
		 * @param selectionArgs
		 *            条件里的参数，用来替换"?"
		 * @param orderBy
		 *            排序 如：id desc
		 * @return 返回Cursor
		 */
		Cursor c = db.query(Constants.NewsListTable.TABLE_NAME, null, null,
				null, null, null, null);
		return c;
	}
	
	public Cursor getData(String newsType,String newTime,String limit){
		String[] selectionArgs = {newsType,newTime};
		Cursor c = db.query(true, Constants.NewsListTable.TABLE_NAME,
				new String[] {Constants.NewsListTable.ID,
				Constants.NewsListTable.TOP_LINE,
				Constants.NewsListTable.NEWS_FROM,
				Constants.NewsListTable.NEWS_ID,
				Constants.NewsListTable.NEWS_IMAGE_URL,
				Constants.NewsListTable.NEWS_SUMMRY,
				Constants.NewsListTable.NEWS_TIME,
				Constants.NewsListTable.NEWS_TITLE,
				}, 
				Constants.NewsListTable.NEWS_TYPE+"=?"+" and "+
						Constants.NewsListTable.NEWS_TIME+"<?", selectionArgs, 
				Constants.NewsListTable.NEWS_TIME, 
				null,
				Constants.NewsListTable.NEWS_TIME+" desc", limit);		
		return c;
		
	}

	/**
	 * 更改表中的记录
	 * 
	 * @param News
	 *            NewsList 类
	 * 
	 * @param whereClause
	 *            修改条件 如：( id=?)
	 * 
	 * @param whereArgs
	 *            条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * 
	 * @return 返回修改的条数 也可以作为判断值，如果是正数则表示更改成功，反之不成功
	 */
	public int updateNewsList(NewsItem newsItem, String whereClause,
			String[] whereArgs) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.NewsListTable.NEWS_TYPE,
					newsItem.getNewsType());
			contentValues.put(Constants.NewsListTable.NEWS_TITLE,
					newsItem.getNewsTitle());
			contentValues.put(Constants.NewsListTable.NEWS_SUMMRY,
					newsItem.getNewsSummary());
			contentValues.put(Constants.NewsListTable.NEWS_FROM,
					newsItem.getNewsFrom());
			contentValues.put(Constants.NewsListTable.NEWS_TIME,
					newsItem.getNewsTime());
			contentValues.put(Constants.NewsListTable.NEWS_ID,
					newsItem.getNews_id());
			contentValues.put(Constants.NewsListTable.NEWS_IMAGE_URL,
					newsItem.getImageUrl());
			return db.update(Constants.NewsListTable.TABLE_NAME, contentValues,
					whereClause, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return -1;
		}

	}

}
