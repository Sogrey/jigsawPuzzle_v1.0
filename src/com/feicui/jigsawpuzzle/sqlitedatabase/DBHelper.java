package com.feicui.jigsawpuzzle.sqlitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private final static String DB_NAME="rank.db";//���ݿ���
	private final static int DB_VERSION=1;//���ݿ�汾
	public final static String TABLE_RANK="rank";//����
	public final static String COLUMN_RANK_ID="_id";//����ID
	public final static String COLUMN_RANK_MODE="mode";//����-mode
	public final static String COLUMN_RANK_LEVEL="level";//����-level
	public final static String COLUMN_RANK_NAME="name";//����-name
	public final static String COLUMN_RANK_STEPS="steps";//����-steps
	public final static String COLUMN_RANK_TIME="time";//����-time
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	/**������*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="CREATE TABLE "+TABLE_RANK+" ( "
				+COLUMN_RANK_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
				+COLUMN_RANK_MODE+" TEXT, "
				+COLUMN_RANK_LEVEL+" TEXT, "
				+COLUMN_RANK_NAME+" TEXT, "
				+COLUMN_RANK_STEPS+" INTEGER, "
				+COLUMN_RANK_TIME+" INTEGER )";//SQL���
				db.execSQL(sql);//ִ��SQL��䣬����������ֵΪ�գ�
	}
	/**
	 * ����<p>
	 * @param db �����������ݿ�
	 * @param oldVersion ԭ�����ݿ�汾
	 * @param newVersion �����ݿ�汾
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	String sql="DROP TABLE IS "+TABLE_RANK;
	db.execSQL(sql);
	onCreate(db);
		
	}
	/**
	 * ����<p>
	 * @param db �����������ݿ�
	 * @param oldVersion ԭ�����ݿ�汾
	 * @param newVersion �����ݿ�汾
	 * */
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	/**
	 * �����ݿ�ʱ������<p>
	 * @param db ���������ݿ�
	 * */
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
