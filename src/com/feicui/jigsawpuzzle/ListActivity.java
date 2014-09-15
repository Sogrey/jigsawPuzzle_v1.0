package com.feicui.jigsawpuzzle;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.feicui.jigsawpuzzle.sqlitedatabase.DBHelper;
import com.feicui.jigsawpuzzle.sqlitedatabase.RankDBWrapper;

public class ListActivity extends Activity {

	ListView mListView;
	RankDBWrapper mDbWrapper;
	DBHelper mDbHelpper;
	RankAdapter listAdapter;

	private static String mRank_List_Mode, mRank_List_Level;
	private static final String[] rank_ModeText = { "�Ե�ģʽ", "�ƶ�ģʽ" };// ��ģ�б�
	private static final String[] rank_LevelText_Exchange = { "5��5", "6��6",
			"7��7", "8��8", "9��9", "10��10" };// �Ե�ģʽ���Ѷ��б�
	private static final String[] rank_LevelText_Move = { "3��3", "4��4", "5��5",
			"6��6" };// �Ե�ģʽ���Ѷ��б�
	// private ArrayAdapter<String> arrayAdapter_mode,arrayAdapter_level;
	private Spinner mSpinner_Mode, mSpinner_Level;// ��������Spinner��������ģʽ�͵ȼ���ѡ��
	int[] rank_ImageView = { R.drawable.dish_no1, R.drawable.dish_no2,
			R.drawable.dish_no3, R.drawable.dish_no4, R.drawable.dish_no5,
			R.drawable.dish_no6, R.drawable.dish_no7, R.drawable.dish_no8,
			R.drawable.dish_no9, R.drawable.dish_no10 };
	private Context context;
	private Cursor cursor;
	private int num;//��Ŀ����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// ȫ��
		setContentView(R.layout.list);
		context = this;
		mDbWrapper = RankDBWrapper.getInstance(context);

		cursor = mDbWrapper.rawQueryRank("�Ե�ģʽ", "5��5");//Ĭ�ϲ�ѯ
		// cursor = RDBW.rawQueryRank();
		listAdapter = new RankAdapter(context, cursor);
		mListView = (ListView) findViewById(R.id.listView_score);
		mListView.setAdapter(listAdapter);
		// mListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {// arg2:�±�
		// // TODO Auto-generated method stub
		// }
		// });

		mSpinner_Mode = (Spinner) findViewById(R.id.rank_list_mode);

		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, rank_ModeText);
			mSpinner_Mode.setAdapter(adapter);
		}
		mSpinner_Level = (Spinner) findViewById(R.id.rank_list_level);
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					rank_LevelText_Exchange);
			mSpinner_Level.setAdapter(adapter);
		}

		mSpinner_Mode
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						mRank_List_Mode = (String) mSpinner_Mode
								.getSelectedItem();
						
						ArrayAdapter<String> adapter = null;
						if (mRank_List_Mode == "�Ե�ģʽ") {
							adapter = new ArrayAdapter<String>(context,
									android.R.layout.simple_spinner_item,
									rank_LevelText_Exchange);

						} else if (mRank_List_Mode == "�ƶ�ģʽ") {
							adapter = new ArrayAdapter<String>(context,
									android.R.layout.simple_spinner_item,
									rank_LevelText_Move);
						}
						mSpinner_Level.setAdapter(adapter);
						// onCheck();
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		mSpinner_Level
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						onCheck();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
//						onCheck();
					}

				});
	}

	public void onCheck() {
		num=0;
		mRank_List_Mode = (String) mSpinner_Mode.getSelectedItem();
		mRank_List_Level = (String) mSpinner_Level.getSelectedItem();
		Log.d("ListActivity", mRank_List_Mode + ":" + mRank_List_Level);
		if (!TextUtils.isEmpty(mRank_List_Mode)
				&& !TextUtils.isEmpty(mRank_List_Level)) {//Spinner��Ŀ���ݲ�Ϊ��
			cursor = mDbWrapper.rawQueryRank(mRank_List_Mode, mRank_List_Level);
			
			listAdapter = new RankAdapter(context, cursor);
			mListView.setAdapter(listAdapter);
			mListView.invalidate();//ǿ��ˢ��ListVIew
		}
	}

	/**
	 * ȫ��
	 * */
	public void setFullScreen() {
		// ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	class RankAdapter extends CursorAdapter {
		int indexName;
		int indexSteps;
		int indexTime;

		public RankAdapter(Context context, Cursor cursor) {
			super(context, cursor);
			indexName = cursor.getColumnIndex(DBHelper.COLUMN_RANK_NAME);
			indexSteps = cursor.getColumnIndex(DBHelper.COLUMN_RANK_STEPS);
			indexTime = cursor.getColumnIndex(DBHelper.COLUMN_RANK_TIME);
		}

		/** ����ListView */
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			/* ������д�õĲ���ת����View���� */
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.item_rank, null);
			num++;//ÿ����һ����Ŀ������һ
			return view;
		}

		/** Ϊ�Ѵ���ListView������ */
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			ImageView rank_dish = (ImageView) view.findViewById(R.id.rank_dish);
			TextView rank_name = (TextView) view.findViewById(R.id.rank_name);
			TextView rank_steps = (TextView) view.findViewById(R.id.rank_steps);
			TextView rank_time = (TextView) view.findViewById(R.id.rank_time);
			if (num<=11) {
				rank_dish.setBackgroundResource(rank_ImageView[num-1]);
			}
			rank_name.setText(cursor.getString(indexName));
			rank_steps.setText(cursor.getInt(indexSteps) + "");
			rank_time.setText(cursor.getInt(indexTime) + "");
		}

	}
}
