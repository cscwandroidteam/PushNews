package com.pushnews.app.db;

import com.pushnews.app.cofig.Constants;
import com.pushnews.app.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * @description 对用户表的数据库管理类
 * 
 * */
public class UserDbManger {
	private final static String TAG = "UserDbManger";
	private static SQLiteDatabase db;
	private static DBHelper dbHelper;

	public UserDbManger(Context c) {
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
	 * @param user
	 *            User类
	 * 
	 * @return long 如果是正数则表示增加成功，反之不成功
	 * 
	 */
	public long addUser(User user) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues
					.put(Constants.UserTable.USER_NAME, user.getUsername());
			contentValues.put(Constants.UserTable.PASSWORD, user.getPassword());
			contentValues.put(Constants.UserTable.MARK, user.getMark());
			return db.insert(Constants.UserTable.TABLE_NAME, null,
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
	public int deleteUser(String whereClause, String[] whereArgs) {
		try {
			return db.delete(Constants.UserTable.TABLE_NAME, whereClause,
					whereArgs);
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
		Cursor c = db.query(Constants.UserTable.TABLE_NAME, null, null, null,
				null, null, null);
		return c;
	}

	/**
	 * 更改表中的记录
	 * 
	 * @param user
	 *            User类
	 * 
	 * @param whereClause
	 *            修改条件 如：( id=?)
	 * 
	 * @param whereArgs
	 *            条件里的参数 用来替换"?" 第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * 
	 * @return 返回修改的条数 也可以作为判断值，如果是正数则表示更改成功，反之不成功
	 */
	public int updateUser(User user, String whereClause, String[] whereArgs) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues
					.put(Constants.UserTable.USER_NAME, user.getUsername());
			contentValues.put(Constants.UserTable.PASSWORD, user.getPassword());
			contentValues.put(Constants.UserTable.MARK, user.getMark());
			return db.update(Constants.UserTable.TABLE_NAME, contentValues,
					whereClause, whereArgs);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return -1;
		}

	}

}
