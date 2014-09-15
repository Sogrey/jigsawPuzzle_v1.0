package com.feicui.jigsawpuzzle.sqlitedatabase;

import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RankDBWrapper {

	private SQLiteDatabase mDb;// SQL数据库对象
	/**单例模式 对象*/
	private static RankDBWrapper sInstance;

	/**
	 * 单例模式 <br>
	 * 一个类最多只能有一个实例 <br>
	 * 1、有一个私有静态成员 <br>
	 * 2、有一个公开静态方法getInstance得到这个私有静态成员 <br>
	 * 3、有一个私有的构造方法（不允许被实例化） <br>
	 */

	public static RankDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			synchronized (RankDBWrapper.class) {
				if (sInstance == null) {
					sInstance = new RankDBWrapper(context);
				}
			}
		}
		return sInstance;
	}

	private RankDBWrapper(Context context) {
		DBHelper helpper = new DBHelper(context);
		mDb = helpper.getWritableDatabase();
	}

	/** 插入数据 */
	public void insertRank(String mode, String level, String name, int steps,
			int time) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_RANK_MODE, mode);
		values.put(DBHelper.COLUMN_RANK_LEVEL, level);
		values.put(DBHelper.COLUMN_RANK_NAME, name);
		values.put(DBHelper.COLUMN_RANK_STEPS, steps);
		values.put(DBHelper.COLUMN_RANK_TIME, time);
		// mDb.insert(table, nullColumnHack, values)/*原型*/
		mDb.insert(DBHelper.TABLE_RANK, "", values);
	}

	/** 更新数据 */
	public void updateRank(/* 条件 */String mode,/* 条件 */String level,
			String name, int steps, int time) {
		ContentValues values = new ContentValues();
		// values.put(RankDBHelpper.COLUMN_RANK_MODE, mode);
		// values.put(RankDBHelpper.COLUMN_RANK_LEVEL, level);
		values.put(DBHelper.COLUMN_RANK_NAME, name);
		values.put(DBHelper.COLUMN_RANK_STEPS, steps);
		values.put(DBHelper.COLUMN_RANK_TIME, time);
		mDb.update(/* 表名 */DBHelper.TABLE_RANK,
		/* 要更新的数据 */values,
		/* 条件 */DBHelper.COLUMN_RANK_MODE + "=? and "
				+ DBHelper.COLUMN_RANK_LEVEL + "=?",
		/* 替换上一参数里的？ */new String[] { mode, level });
	}

	/** 删除数据 */
	public void deleteRank(String mode, String level) {
		// mDb.delete(table, whereClause, whereArgs);/*原型*/
		mDb.delete(/* 表名 */DBHelper.TABLE_RANK,
		/* 条件 */DBHelper.COLUMN_RANK_MODE + "=? and "
				+ DBHelper.COLUMN_RANK_LEVEL + "=?",
		/* 替换上一参数里的？ */new String[] { mode, level });
	}

	/** 查询数据 */
	public LinkedList<RankDBWrapper.Record> queryRank(String mode, String level) {

		/* query方法查询 */
		LinkedList<RankDBWrapper.Record> recordList = new LinkedList<RankDBWrapper.Record>();
		Cursor cursor = null;
		try {
			// mDb.query(table, columns, selection, selectionArgs, groupBy,
			// having, orderBy)/*原型*/
			cursor = mDb.query(/* 表名 */DBHelper.TABLE_RANK,
			/* 需要查询的列名 */new String[] { DBHelper.COLUMN_RANK_NAME,
					DBHelper.COLUMN_RANK_STEPS,
					DBHelper.COLUMN_RANK_TIME, },
			/* 查询条件 */DBHelper.COLUMN_RANK_MODE + "=? and "
					+ DBHelper.COLUMN_RANK_LEVEL + "=?",
			/* 查询条件对应的值 */new String[] { mode, level },
			/* 分组 */null, null,
			/* 排序(按步数排序，小的在前) */DBHelper.COLUMN_RANK_STEPS + " ASC"
			// /*排序(按时间排序，小的在前)*/RankDBHelpper.COLUMN_RANK_TIME+" ASC"
					);
			int nameIndex = cursor
					.getColumnIndex(DBHelper.COLUMN_RANK_NAME);/* name列列号 */
			int stepsIndex = cursor
					.getColumnIndex(DBHelper.COLUMN_RANK_STEPS);/* steps列列号 */
			int timeIndex = cursor
					.getColumnIndex(DBHelper.COLUMN_RANK_TIME);/* time列列号 */
			// 获取总列数
			cursor.getColumnCount();
			// 获取总共有多少条数据
			cursor.getCount();

			RankDBWrapper.Record record = null;
			while (cursor.moveToNext()) {// 要查询的列
				cursor.getString(nameIndex);
				cursor.getInt(stepsIndex);
				cursor.getInt(timeIndex);
				recordList.add(record);
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();// 游标执行完判断是否为空，不为空即可关闭
			}
		}

		return recordList;
	}

	public class Record {
		String name;
		int steps;
		int time;
	}

	/** 按mode,level查询数据,返回游标Cursor */
	public Cursor rawQueryRank(String mode, String level) {
		// public Cursor rawQueryRank(){
		// public Cursor rawQueryRank(String level){

		/* SQL语句查询 */
		String sql = "select * from " + DBHelper.TABLE_RANK + " where "
				+ DBHelper.COLUMN_RANK_MODE + " = \"" + mode + "\" and "
				+ DBHelper.COLUMN_RANK_LEVEL + " = \"" + level + "\""
				+ " order by " + DBHelper.COLUMN_RANK_STEPS + " ASC"
				+ " limit " + 10;
		return mDb.rawQuery(sql, null);

	}

	/** 按mode,level查询数据,返回游标步数小于steps的Cursor */
	public Cursor rawQueryRank_CheckPosition(String mode, String level,
			int steps) {
		String sql = "select * from " + DBHelper.TABLE_RANK + " where "
				+ DBHelper.COLUMN_RANK_MODE + " = \"" + mode + "\" and "
				+ DBHelper.COLUMN_RANK_LEVEL + " = \"" + level + "\" and "
				+ DBHelper.COLUMN_RANK_STEPS + " < " + steps + " or "
				+ DBHelper.COLUMN_RANK_STEPS + " = " + steps
				+ " order by " + DBHelper.COLUMN_RANK_STEPS + " ASC"
				+ " limit " + 10;
		return mDb.rawQuery(sql, null);
	}
}
