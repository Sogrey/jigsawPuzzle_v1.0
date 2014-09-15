package com.feicui.jigsawpuzzle.sqlitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private final static String DB_NAME="rank.db";//数据库名
	private final static int DB_VERSION=1;//数据库版本
	public final static String TABLE_RANK="rank";//表名
	public final static String COLUMN_RANK_ID="_id";//主键ID
	public final static String COLUMN_RANK_MODE="mode";//列名-mode
	public final static String COLUMN_RANK_LEVEL="level";//列名-level
	public final static String COLUMN_RANK_NAME="name";//列名-name
	public final static String COLUMN_RANK_STEPS="steps";//列名-steps
	public final static String COLUMN_RANK_TIME="time";//列名-time
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	/**创建表*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="CREATE TABLE "+TABLE_RANK+" ( "
				+COLUMN_RANK_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
				+COLUMN_RANK_MODE+" TEXT, "
				+COLUMN_RANK_LEVEL+" TEXT, "
				+COLUMN_RANK_NAME+" TEXT, "
				+COLUMN_RANK_STEPS+" INTEGER, "
				+COLUMN_RANK_TIME+" INTEGER )";//SQL语句
				db.execSQL(sql);//执行SQL语句，创建表（返回值为空）
	}
	/**
	 * 升级<p>
	 * @param db 被操作的数据库
	 * @param oldVersion 原先数据库版本
	 * @param newVersion 新数据库版本
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	String sql="DROP TABLE IS "+TABLE_RANK;
	db.execSQL(sql);
	onCreate(db);
		
	}
	/**
	 * 降级<p>
	 * @param db 被操作的数据库
	 * @param oldVersion 原先数据库版本
	 * @param newVersion 新数据库版本
	 * */
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	/**
	 * 打开数据库时被调用<p>
	 * @param db 被操作数据库
	 * */
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
