package com.feicui.jigsawpuzzle.game;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.jigsawpuzzle.HelpActivity;
import com.feicui.jigsawpuzzle.MainActivity;
import com.feicui.jigsawpuzzle.OptionActivity;
import com.feicui.jigsawpuzzle.R;
import com.feicui.jigsawpuzzle.game.GameView.GoToOtherActivityListener;
import com.feicui.jigsawpuzzle.game.GameView.OnWinListener;
import com.feicui.jigsawpuzzle.sqlitedatabase.RankDBWrapper;

public class GameSceneActivity extends Activity implements
		DialogInterface.OnClickListener, OnWinListener,
		GoToOtherActivityListener {

	public static int mode;// ��Ϸģʽ
	public static int level;// �Ѷȵȼ�
	public static int imageId;// ͼƬ�ɣ�
	public static boolean isPause = false;// ��ͣ��Ϸ��

	private final static int DIALOG_ID_WIN = 0x1;// ��Ϸʤ���Ի���ID

	private TextView mWin_Mode;// ��Ϸģʽ��win_Dialog��
	private TextView mWin_Level;// �Ѷȼ���win_Dialog��
	private TextView mWin_Steps;// ��Ϸ������win_Dialog��
	private TextView mWin_Time;// ��Ϸʱ�䣨win_Dialog��
	private EditText mWin_Name;// ������ƣ�win_Dialog��

	private String mModeText;// ��Ϸģʽ���ı���
	private String mLevelText;// ��Ϸ�Ѷȣ��ı���
	private int mTime;// ��Ϸʱ�䣨���ݣ�
	private int mSteps;// ��Ϸ���������ݣ�
	private String mNameText;// �������(�ı�)

	private RankDBWrapper mDb;// ����RankDBWrapper����

	MediaPlayer mPlayer;
	private static final int mPlayList[] = { R.raw.annies_wonderland,
			R.raw.chuxue, R.raw.just_a_little_smile_bandari,
			R.raw.one_day_in_spring, R.raw.snowdream, R.raw.tongnian,
			R.raw.write_to_the_ocean };// ���������б�
	private static final int mPlayList_Win = R.raw.win;// ʤ������
	Random random = new Random();
	private int mRankCount;
	private String mRankPositionText;;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// ȫ��
		mPlayer = MediaPlayer.create(this,
				mPlayList[random.nextInt(mPlayList.length)]);
		mPlayer.setLooping(true);

		mode = getIntent().getIntExtra("mode", mode);
		System.out.println("ѡ�����Ϸģʽ" + mode);
		level = getIntent().getIntExtra("level", level);
		System.out.println("ѡ�����Ϸ�Ѷȵȼ�" + level);
		imageId = getIntent().getIntExtra("imageId", imageId);
		System.out.println("ѡ���ͼƬID" + imageId);
		System.out.println("GameScene====" + mode + ":" + level + ":" + imageId
				+ "======");
		setContentView(com.feicui.jigsawpuzzle.R.layout.gameview);// ����gameview.xml����
		((GameView) findViewById(R.id.gameView)).setOnWinListener(this);// ����
		((GameView) findViewById(R.id.gameView))
				.GoToOtherActivityListener(this);// ����
		mDb = RankDBWrapper.getInstance(this);
	}

	protected void onResume() {
		super.onResume();
		if (OptionActivity.isPlay) {
			mPlayer.start();
		}
	}

	@Override
	protected void onPause() {
		if (OptionActivity.isPlay) {
			mPlayer.pause();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mPlayer.release();
		super.onDestroy();
	}

	/** ����xmlѡ��˵� */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);// ����xmlѡ��˵�
		// ��ͣ��Ϸ
		return true;
	}

	/** xmlѡ��˵�ѡ���� */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.backtogame:

			return true;
		case R.id.selectagain:
			ExitGame_Back2SelectPicture();
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			return true;
		case R.id.help:
			isPause = true;
			Intent intent_Help = new Intent(GameSceneActivity.this, HelpActivity.class);
			startActivity(intent_Help);
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			return true;
		case R.id.option:
			isPause = true;
			Intent intent_Option = new Intent(GameSceneActivity.this,
					OptionActivity.class);
			startActivity(intent_Option);
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			return true;
		case R.id.backtomain:
			ExitGame();
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}

	/** ѡ��˵����ر� */
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// ������Ϸor����or����or�������˵�
		super.onOptionsMenuClosed(menu);
	}

	/** ȫ�� */
	private void setFullScreen() {
		// ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("�㰴���˷��ؼ�");
			isPause = true;
			ExitGame();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * �˳���ǰ��Ϸ��
	 * <p>
	 * ����������
	 * */
	public void ExitGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("�˳���Ϸ��ʾ");
		builder.setIcon(R.drawable.game_exit_tip);
		if (GameView.isWin) {
			System.out.println(GameView.isWin);
			builder.setMessage("����Ҫ����ѡͼ��Ҫ���������棿");
		} else
			builder.setMessage("�����Ҫ������ǰ��Ϸ��");
		builder.setNegativeButton("ȡ��", null);// �ұߵ�һ����ť������ȡ����
		isPause = false;
		builder.setNeutralButton("����ѡͼ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(GameSceneActivity.this,
						SelectPictureAndLevel.class);
				intent.putExtra("mode", mode);// ���Σ�����int��������key����ǩ��Value��ֵ
				startActivity(intent);
				GameSceneActivity.this.finish();
			}
		});// �м䰴ť
		builder.setPositiveButton("����������",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {// ȷ���˳���ǰ��Ϸ������������
						System.out.println("������ȷ����ȷ���˳�");
						Intent intent_Main = new Intent(GameSceneActivity.this,
								MainActivity.class);
						startActivity(intent_Main);
						GameSceneActivity.this.finish();
					}
				});// ��ߵ�һ����ť��ȷ����
		builder.show();
		System.out.println("�˳���Ϸ������������");
	}

	/**
	 * �˳���ǰ��Ϸ��
	 * <p>
	 * ������ѡͼƬ
	 * */
	public void ExitGame_Back2SelectPicture() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("�˳���Ϸ��ʾ");
		builder.setIcon(R.drawable.game_selectagain_tip);
		if (GameView.isWin) {
			System.out.println(GameView.isWin);
			builder.setMessage("����Ҫ����ѡͼ������Ϸ��");
		} else
			builder.setMessage("�����Ҫ������ǰ��Ϸ,����ѡ��ͼƬ��");

		builder.setNegativeButton("ȡ��", null);// �ұߵ�һ����ť������ȡ����
		isPause = false;
		// builder.setNeutralButton("�м䰴ť", null);//�м䰴ť
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {// ȷ���˳���ǰ��Ϸ����������ѡ��ͼƬ
				System.out.println("������ȷ����ȷ���˳�");
				Intent intent = new Intent(GameSceneActivity.this,
						SelectPictureAndLevel.class);
				intent.putExtra("mode", mode);// ���Σ�����int��������key����ǩ��Value��ֵ
				startActivity(intent);
				GameSceneActivity.this.finish();
			}
		});// ��ߵ�һ����ť��ȷ����
		builder.show();
		System.out.println("�˳���Ϸ��������ѡͼƬ");

	}

	public void set_IsPause(boolean gameback) {
		this.isPause = gameback;
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_ID_WIN:
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.dialog_win, null);
			/* LayoutInflater�ṩһ��ת�����̣���֮ǰ��xml�����ļ�ת���ɵ�Ҫ��ʾ�����ʵ�������� View */
			mWin_Mode = (TextView) view.findViewById(R.id.win_mode);
			mWin_Level = (TextView) view.findViewById(R.id.win_level);
			mWin_Steps = (TextView) view.findViewById(R.id.win_steps);
			mWin_Time = (TextView) view.findViewById(R.id.win_time);
			mWin_Name = (EditText) view.findViewById(R.id.win_name);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (mRankPositionText != null) {
				builder.setTitle("��ϲ����Ӯ�á�" + mModeText + mLevelText + "����"
						+ mRankPositionText + "��");
			} else {
				builder.setTitle("��Ϸʤ��");
			}
			builder.setIcon(R.drawable.win_tip_icon);
			builder.setView(view);// ��WIN�Ի�����view��������
			builder.setPositiveButton("ȷ��", this);
			// builder.setNegativeButton("ȡ��", null);
			return builder.create();
		default:
			return super.onCreateDialog(id, args);
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		switch (id) {
		case DIALOG_ID_WIN:
			// ���Ի����и�����textview������
			mWin_Mode.setText(mModeText);
			mWin_Level.setText(mLevelText);
			mWin_Steps.setText(mSteps + "");
			mWin_Time.setText(mTime + "");

			break;

		default:
			super.onPrepareDialog(id, dialog, args);
			break;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// private EditText mWin_Name;//������ƣ�win_Dialog��
		switch (which) {// ��ť
		case DialogInterface.BUTTON_POSITIVE:
			mNameText = mWin_Name.getText().toString();// ȡ�༭���ı���name��
			if (TextUtils.isEmpty(mNameText)) {// �ж�mName�ǲ���Ϊ�գ��������Ĭ��ֵ
				mNameText = "������";
			}
			mDb.insertRank(mModeText, mLevelText, mNameText, mSteps, mTime);// ���ݿ��������
			// Intent intent = new Intent(GameScene.this,ListActivity.class);
			// startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onWin(String modeText, String levelText, int time, int steps) {
		mModeText = modeText;
		mLevelText = levelText;
		mTime = time;
		mSteps = steps;
		mRankCount = mDb.rawQueryRank_CheckPosition(mModeText, mLevelText,
				mSteps).getCount();
		if (mRankCount + 1 <= 10) {
			mRankPositionText = (mRankCount + 1) + "";
		}
		showDialog(DIALOG_ID_WIN);// ��ʾʤ���Ի���
		// ʤ��������mPlayer������ʤ����ʾ��
		if (OptionActivity.isPlay) {
			mPlayer.reset();
			mPlayer = MediaPlayer.create(this, mPlayList_Win);
			mPlayer.start();
		}
	}

	@Override
	public void goToOtherActivity(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			ExitGame();
			break;
		case 1:
			ExitGame_Back2SelectPicture();
			break;
		}
	}
}
