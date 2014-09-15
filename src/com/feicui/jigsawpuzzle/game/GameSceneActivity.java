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

	public static int mode;// 游戏模式
	public static int level;// 难度等级
	public static int imageId;// 图片ＩＤ
	public static boolean isPause = false;// 暂停游戏？

	private final static int DIALOG_ID_WIN = 0x1;// 游戏胜利对话框ID

	private TextView mWin_Mode;// 游戏模式（win_Dialog）
	private TextView mWin_Level;// 难度级别（win_Dialog）
	private TextView mWin_Steps;// 游戏步数（win_Dialog）
	private TextView mWin_Time;// 游戏时间（win_Dialog）
	private EditText mWin_Name;// 玩家名称（win_Dialog）

	private String mModeText;// 游戏模式（文本）
	private String mLevelText;// 游戏难度（文本）
	private int mTime;// 游戏时间（数据）
	private int mSteps;// 游戏步数（数据）
	private String mNameText;// 玩家名称(文本)

	private RankDBWrapper mDb;// 创建RankDBWrapper对象

	MediaPlayer mPlayer;
	private static final int mPlayList[] = { R.raw.annies_wonderland,
			R.raw.chuxue, R.raw.just_a_little_smile_bandari,
			R.raw.one_day_in_spring, R.raw.snowdream, R.raw.tongnian,
			R.raw.write_to_the_ocean };// 背景音乐列表
	private static final int mPlayList_Win = R.raw.win;// 胜利音乐
	Random random = new Random();
	private int mRankCount;
	private String mRankPositionText;;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// 全屏
		mPlayer = MediaPlayer.create(this,
				mPlayList[random.nextInt(mPlayList.length)]);
		mPlayer.setLooping(true);

		mode = getIntent().getIntExtra("mode", mode);
		System.out.println("选择的游戏模式" + mode);
		level = getIntent().getIntExtra("level", level);
		System.out.println("选择的游戏难度等级" + level);
		imageId = getIntent().getIntExtra("imageId", imageId);
		System.out.println("选择的图片ID" + imageId);
		System.out.println("GameScene====" + mode + ":" + level + ":" + imageId
				+ "======");
		setContentView(com.feicui.jigsawpuzzle.R.layout.gameview);// 加载gameview.xml布局
		((GameView) findViewById(R.id.gameView)).setOnWinListener(this);// 监听
		((GameView) findViewById(R.id.gameView))
				.GoToOtherActivityListener(this);// 监听
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

	/** 加载xml选项菜单 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);// 加载xml选项菜单
		// 暂停游戏
		return true;
	}

	/** xml选项菜单选项被点击 */
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

	/** 选项菜单被关闭 */
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// 返回游戏or帮助or设置or返回主菜单
		super.onOptionsMenuClosed(menu);
	}

	/** 全屏 */
	private void setFullScreen() {
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("你按下了返回键");
			isPause = true;
			ExitGame();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出当前游戏？
	 * <p>
	 * 返回主界面
	 * */
	public void ExitGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("退出游戏提示");
		builder.setIcon(R.drawable.game_exit_tip);
		if (GameView.isWin) {
			System.out.println(GameView.isWin);
			builder.setMessage("你是要重新选图还要返回主界面？");
		} else
			builder.setMessage("你真的要放弃当前游戏？");
		builder.setNegativeButton("取消", null);// 右边第一个按钮，返回取消键
		isPause = false;
		builder.setNeutralButton("重新选图", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(GameSceneActivity.this,
						SelectPictureAndLevel.class);
				intent.putExtra("mode", mode);// 传参，整形int。参数：key：标签，Value：值
				startActivity(intent);
				GameSceneActivity.this.finish();
			}
		});// 中间按钮
		builder.setPositiveButton("返回主界面",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {// 确定退出当前游戏，返回主界面
						System.out.println("你点击了确定，确定退出");
						Intent intent_Main = new Intent(GameSceneActivity.this,
								MainActivity.class);
						startActivity(intent_Main);
						GameSceneActivity.this.finish();
					}
				});// 左边第一个按钮，确定键
		builder.show();
		System.out.println("退出游戏，返回主界面");
	}

	/**
	 * 退出当前游戏？
	 * <p>
	 * 返回重选图片
	 * */
	public void ExitGame_Back2SelectPicture() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("退出游戏提示");
		builder.setIcon(R.drawable.game_selectagain_tip);
		if (GameView.isWin) {
			System.out.println(GameView.isWin);
			builder.setMessage("你是要重新选图继续游戏？");
		} else
			builder.setMessage("你真的要放弃当前游戏,重新选择图片？");

		builder.setNegativeButton("取消", null);// 右边第一个按钮，返回取消键
		isPause = false;
		// builder.setNeutralButton("中间按钮", null);//中间按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {// 确定退出当前游戏，返回重新选择图片
				System.out.println("你点击了确定，确定退出");
				Intent intent = new Intent(GameSceneActivity.this,
						SelectPictureAndLevel.class);
				intent.putExtra("mode", mode);// 传参，整形int。参数：key：标签，Value：值
				startActivity(intent);
				GameSceneActivity.this.finish();
			}
		});// 左边第一个按钮，确定键
		builder.show();
		System.out.println("退出游戏，返回重选图片");

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
			/* LayoutInflater提供一个转换过程，把之前的xml布局文件转换成的要显示的类的实例，返回 View */
			mWin_Mode = (TextView) view.findViewById(R.id.win_mode);
			mWin_Level = (TextView) view.findViewById(R.id.win_level);
			mWin_Steps = (TextView) view.findViewById(R.id.win_steps);
			mWin_Time = (TextView) view.findViewById(R.id.win_time);
			mWin_Name = (EditText) view.findViewById(R.id.win_name);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (mRankPositionText != null) {
				builder.setTitle("恭喜大神赢得【" + mModeText + mLevelText + "】第"
						+ mRankPositionText + "名");
			} else {
				builder.setTitle("游戏胜利");
			}
			builder.setIcon(R.drawable.win_tip_icon);
			builder.setView(view);// 把WIN对话框与view关联起来
			builder.setPositiveButton("确定", this);
			// builder.setNegativeButton("取消", null);
			return builder.create();
		default:
			return super.onCreateDialog(id, args);
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		switch (id) {
		case DIALOG_ID_WIN:
			// 给对话框中各参数textview传数据
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
		// private EditText mWin_Name;//玩家名称（win_Dialog）
		switch (which) {// 按钮
		case DialogInterface.BUTTON_POSITIVE:
			mNameText = mWin_Name.getText().toString();// 取编辑框文本（name）
			if (TextUtils.isEmpty(mNameText)) {// 判断mName是不是为空，如果是则赋默认值
				mNameText = "无名氏";
			}
			mDb.insertRank(mModeText, mLevelText, mNameText, mSteps, mTime);// 数据库插入数据
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
		showDialog(DIALOG_ID_WIN);// 显示胜利对话框
		// 胜利，重置mPlayer，播放胜利提示音
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
