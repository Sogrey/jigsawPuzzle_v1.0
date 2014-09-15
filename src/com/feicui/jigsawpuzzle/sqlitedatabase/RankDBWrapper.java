package com.feicui.jigsawpuzzle.sqlitedatabase;

import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RankDBWrapper {

	private SQLiteDatabase mDb;// SQL���ݿ����
	/**����ģʽ ����*/
	private static RankDBWrapper sInstance;

	/**
	 * ����ģʽ <br>
	 * һ�������ֻ����һ��ʵ�� <br>
	 * 1����һ��˽�о�̬��Ա <br>
	 * 2����һ��������̬����getInstance�õ����˽�о�̬��Ա <br>
	 * 3����һ��˽�еĹ��췽����������ʵ������ <br>
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

	/** �������� */
	public void insertRank(String mode, String level, String name, int steps,
			int time) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_RANK_MODE, mode);
		values.put(DBHelper.COLUMN_RANK_LEVEL, level);
		values.put(DBHelper.COLUMN_RANK_NAME, name);
		values.put(DBHelper.COLUMN_RANK_STEPS, steps);
		values.put(DBHelper.COLUMN_RANK_TIME, time);
		// mDb.insert(table, nullColumnHack, values)/*ԭ��*/
		mDb.insert(DBHelper.TABLE_RANK, "", values);
	}

	/** �������� */
	public void updateRank(/* ���� */String mode,/* ���� */String level,
			String name, int steps, int time) {
		ContentValues values = new ContentValues();
		// values.put(RankDBHelpper.COLUMN_RANK_MODE, mode);
		// values.put(RankDBHelpper.COLUMN_RANK_LEVEL, level);
		values.put(DBHelper.COLUMN_RANK_NAME, name);
		values.put(DBHelper.COLUMN_RANK_STEPS, steps);
		values.put(DBHelper.COLUMN_RANK_TIME, time);
		mDb.update(/* ���� */DBHelper.TABLE_RANK,
		/* Ҫ���µ����� */values,
		/* ���� */DBHelper.COLUMN_RANK_MODE + "=? and "
				+ DBHelper.COLUMN_RANK_LEVEL + "=?",
		/* �滻��һ������ģ� */new String[] { mode, level });
	}

	/** ɾ������ */
	public void deleteRank(String mode, String level) {
		// mDb.delete(table, whereClause, whereArgs);/*ԭ��*/
		mDb.delete(/* ���� */DBHelper.TABLE_RANK,
		/* ���� */DBHelper.COLUMN_RANK_MODE + "=? and "
				+ DBHelper.COLUMN_RANK_LEVEL + "=?",
		/* �滻��һ������ģ� */new String[] { mode, level });
	}

	/** ��ѯ���� */
	public LinkedList<RankDBWrapper.Record> queryRank(String mode, String level) {

		/* query������ѯ */
		LinkedList<RankDBWrapper.Record> recordList = new LinkedList<RankDBWrapper.Record>();
		Cursor cursor = null;
		try {
			// mDb.query(table, columns, selection, selectionArgs, groupBy,
			// having, orderBy)/*ԭ��*/
			cursor = mDb.query(/* ���� */DBHelper.TABLE_RANK,
			/* ��Ҫ��ѯ������ */new String[] { DBHelper.COLUMN_RANK_NAME,
					DBHelper.COLUMN_RANK_STEPS,
					DBHelper.COLUMN_RANK_TIME, },
			/* ��ѯ���� */DBHelper.COLUMN_RANK_MODE + "=? and "
					+ DBHelper.COLUMN_RANK_LEVEL + "=?",
			/* ��ѯ������Ӧ��ֵ */new String[] { mode, level },
			/* ���� */null, null,
			/* ����(����������С����ǰ) */DBHelper.COLUMN_RANK_STEPS + " ASC"
			// /*����(��ʱ������С����ǰ)*/RankDBHelpper.COLUMN_RANK_TIME+" ASC"
					);
			int nameIndex = cursor
					.getColumnIndex(DBHelper.COLUMN_RANK_NAME);/* name���к� */
			int stepsIndex = cursor
					.getColumnIndex(DBHelper.COLUMN_RANK_STEPS);/* steps���к� */
			int timeIndex = cursor
					.getColumnIndex(DBHelper.COLUMN_RANK_TIME);/* time���к� */
			// ��ȡ������
			cursor.getColumnCount();
			// ��ȡ�ܹ��ж���������
			cursor.getCount();

			RankDBWrapper.Record record = null;
			while (cursor.moveToNext()) {// Ҫ��ѯ����
				cursor.getString(nameIndex);
				cursor.getInt(stepsIndex);
				cursor.getInt(timeIndex);
				recordList.add(record);
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();// �α�ִ�����ж��Ƿ�Ϊ�գ���Ϊ�ռ��ɹر�
			}
		}

		return recordList;
	}

	public class Record {
		String name;
		int steps;
		int time;
	}

	/** ��mode,level��ѯ����,�����α�Cursor */
	public Cursor rawQueryRank(String mode, String level) {
		// public Cursor rawQueryRank(){
		// public Cursor rawQueryRank(String level){

		/* SQL����ѯ */
		String sql = "select * from " + DBHelper.TABLE_RANK + " where "
				+ DBHelper.COLUMN_RANK_MODE + " = \"" + mode + "\" and "
				+ DBHelper.COLUMN_RANK_LEVEL + " = \"" + level + "\""
				+ " order by " + DBHelper.COLUMN_RANK_STEPS + " ASC"
				+ " limit " + 10;
		return mDb.rawQuery(sql, null);

	}

	/** ��mode,level��ѯ����,�����α경��С��steps��Cursor */
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
