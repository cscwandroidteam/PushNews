package com.pushnews.app.db;

import com.pushnews.app.cofig.Constants;
import com.pushnews.app.model.Mycollect;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * @description 对收藏列表的数据库管理类
 * 
 * */
public class MycollectDbManger {
	private final static String TAG = "MycollectDbManger";
	private static SQLiteDatabase db;
	private static DBHelper dbHelper;

	public MycollectDbManger(Context c) {
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
	 * @param myCollect
	 *            Mycollect类
	 * 
	 * @return long 如果是正数则表示增加成功，反之不成功
	 * 
	 */
	public long addCollect(Mycollect mycollect) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.MycollectTable.NEWS_TYPE,
					mycollect.getNewsType());
			contentValues.put(Constants.MycollectTable.NEWS_TITLE,
					mycollect.getNewsTitle());
			contentValues.put(Constants.MycollectTable.NEWS_SUMMRY,
					mycollect.getNewsSummary());
			contentValues.put(Constants.MycollectTable.NEWS_FROM,
					mycollect.getNewsFrom());
			contentValues.put(Constants.MycollectTable.NEWS_TIME,
					mycollect.getNewsTime());
			contentValues.put(Constants.MycollectTable.NEWS_ID,
					mycollect.getNews_id());
			contentValues.put(Constants.MycollectTable.NEWS_IMAGE_URL,
					mycollect.getImageUrl());
			return db.insert(Constants.MycollectTable.TABLE_NAME, null,
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
	public int deleteCollect(String newsTitle) {
		try {

			return db.delete(Constants.MycollectTable.TABLE_NAME,
					Constants.MycollectTable.NEWS_TITLE + " =?",
					new String[] { newsTitle });
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage());
			return -1;
		}
	}

	/**
	 * 查找表中所有记录
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
		Cursor c = db.query(true, Constants.MycollectTable.TABLE_NAME, null,
				null, null, null, null, Constants.NewsListTable.NEWS_TIME
						+ " desc", null);
		return c;
	}

	/**
	 * 通过新闻标题，查找表中指定的一条记录
	 * 
	 * @return Cursor
	 */
	public Cursor getcollect(String newsTitle) {
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
		Cursor c = db.query(true, Constants.MycollectTable.TABLE_NAME,
				new String[] { Constants.MycollectTable.ID,
						Constants.MycollectTable.NEWS_FROM,
						Constants.MycollectTable.NEWS_ID,
						Constants.MycollectTable.NEWS_IMAGE_URL,
						Constants.MycollectTable.NEWS_SUMMRY,
						Constants.MycollectTable.NEWS_TIME,
						Constants.MycollectTable.NEWS_TITLE },
				Constants.MycollectTable.NEWS_TITLE + " =?",
				new String[] { newsTitle }, null, null, null, null);
		return c;
	}

	/**
	 * 更改表中的记录
	 * 
	 * @param Mycollect
	 *            Mycollect 类
	 * 
	 * @param whereClause
	 *            修改条件 如：( id=?)
	 * 
	 * @param whereArgs
	 *            条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * 
	 * @return 返回修改的条数 也可以作为判断值，如果是正数则表示更改成功，反之不成功
	 */
	public int updateCollect(Mycollect mycollect, String whereClause,
			String[] whereArgs) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.MycollectTable.NEWS_TYPE,
					mycollect.getNewsType());
			contentValues.put(Constants.MycollectTable.NEWS_TITLE,
					mycollect.getNewsTitle());
			contentValues.put(Constants.MycollectTable.NEWS_SUMMRY,
					mycollect.getNewsSummary());
			contentValues.put(Constants.MycollectTable.NEWS_FROM,
					mycollect.getNewsFrom());
			contentValues.put(Constants.MycollectTable.NEWS_TIME,
					mycollect.getNewsTime());
			contentValues.put(Constants.MycollectTable.NEWS_ID,
					mycollect.getNews_id());
			contentValues.put(Constants.MycollectTable.NEWS_IMAGE_URL,
					mycollect.getImageUrl());
			return db.update(Constants.MycollectTable.TABLE_NAME,
					contentValues, whereClause, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return -1;
		}

	}

}
