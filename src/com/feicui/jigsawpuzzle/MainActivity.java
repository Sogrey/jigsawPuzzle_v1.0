package com.feicui.jigsawpuzzle;

import com.feicui.jigsawpuzzle.game.SelectModeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {

	boolean isBack = false;
	// Bitmap bitmapMain;
	ImageView imageView_Start, imageView_List, imageView_Option,
			imageView_Help, imageView_About, imageView_Exit;

	@Override
	// 1.onCreate 创建
	// 程序第一次运行第一个都是onCreate
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// 全屏
		setContentView(R.layout.activity_main);
		Log.i("jp", "MainActivity onCreate");

		theAboutButtonCliclked();// 关于按钮游戏被点击
		theExitButtonCliclked();// 退出按钮游戏被点击
		theHelpButtonCliclked();// 帮助按钮游戏被点击
		theListButtonCliclked();// 排行榜按钮游戏被点击
		theOptionButtonCliclked();// 设置按钮游戏被点击
		theStartButtonCliclked();// 开始按钮游戏被点击
	}

	@Override
	// 2.onStart 启动
	protected void onStart() {

		super.onStart();
		Log.i("jp", "MainActivity onStart");

	}

	@Override
	// 3.onResume (中断后)继续运行
	protected void onResume() {

		super.onResume();
		Log.i("jp", "MainActivity onResume");
	}

	@Override
	// 4.onPause 暂停
	protected void onPause() {

		super.onPause();
		Log.i("jp", "MainActivity onPause");
		// if (isBack) //如果按下返回键，不显示暂停界面直接退出,如果不是back键，暂停
		// exit();//按钮退出游戏被点击

	}

	@Override
	// 5.onStop 停止
	protected void onStop() {

		super.onStop();
		Log.i("jp", "MainActivity onStop");
	}

	@Override
	// 6.onRestart 重启
	protected void onRestart() {

		super.onRestart();
		Log.i("jp", "MainActivity onRestart");

	}

	@Override
	// 7.onDestroy 销毁
	protected void onDestroy() {
		
		super.onDestroy();
		Log.i("jp", "MainActivity onDestroy");

	}

	/**
	 * new Intent(packageContext, cls) packageContext:当前所在的Activity的.this
	 * cls:要跳转到的Activity的.class startActivity(intent);启动intent
	 * 
	 * */
	// 暂停
	public void gotoPause() {
		Intent intent = new Intent(MainActivity.this, PauseActivity.class);

		startActivity(intent);
	}

	@Override
	/**
	 *判断按键
	 *onKeyDown(keyCode, event)
	 * */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isBack = true;
			System.out.println("你按下了返回键");
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 全屏
	 * */
	public void setFullScreen() {
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		System.out.println("全屏");
	}

	/**
	 * 退出对话框
	 * */
	public void exit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("退出提示");
		builder.setIcon(R.drawable.tip);
		builder.setMessage("你真的要离开我么？");
		builder.setNegativeButton("取消", null);// 右边第一个按钮，返回取消键
		// builder.setNeutralButton("中间按钮", null);//中间按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				System.out.println("你点击了确定，确定退出");
				MainActivity.this.finish();

			}
		});// 左边第一个按钮，确定键
		builder.show();
		System.out.println("弹出退出对话框");
	}

	/**
	 * 【退出游戏】按钮被点击
	 * */
	public void theExitButtonCliclked() {
		imageView_Exit = (ImageView) findViewById(R.id.imageView6);// 为imageView绑定图片ID
		// 单击（短按）
		/*
		 * imageView.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * System.out.println("短按");
		 * 
		 * exit(); } });
		 */
		// 长按
		/*
		 * imageView.setOnLongClickListener(new OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View arg0) {
		 * 
		 * //System.out.println("长按"); return true; } });
		 */
		imageView_Exit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// MotionEvent event:按下状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					imageView_Exit.setImageResource(R.drawable.exit0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 滑动
					imageView_Exit.setImageResource(R.drawable.exit0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 抬起
					imageView_Exit.setImageResource(R.drawable.exit1);
					// isBack=true;
					exit();
					System.out.println("【退出游戏】按钮被点击");
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 【开始游戏】按钮被点击
	 * */
	public void theStartButtonCliclked() {
		imageView_Start = (ImageView) findViewById(R.id.imageView1);// 为imageView绑定图片ID
		imageView_Start.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:按下状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					imageView_Start.setImageResource(R.drawable.start0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 滑动
					imageView_Start.setImageResource(R.drawable.start0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 抬起
					imageView_Start.setImageResource(R.drawable.start1);
					// TODO：此处添加进入游戏代码入口
					System.out.println("【开始游戏】按钮被点击");
					Intent intent = new Intent(MainActivity.this,
							SelectModeActivity.class);

					startActivity(intent);
					finish();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 【积分排名】按钮被点击
	 * */
	public void theListButtonCliclked() {
		imageView_List = (ImageView) findViewById(R.id.imageView2);// 为imageView绑定图片ID

		imageView_List.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:按下状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					imageView_List.setImageResource(R.drawable.list0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 滑动
					imageView_List.setImageResource(R.drawable.list0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 抬起
					imageView_List.setImageResource(R.drawable.list1);
					// TODO：此处添加查看积分排名代码入口
					System.out.println("【积分排名】按钮被点击");
					Intent intent = new Intent(MainActivity.this,
							ListActivity.class);

					startActivity(intent);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 【游戏设置】按钮被点击
	 * */
	public void theOptionButtonCliclked() {
		imageView_Option = (ImageView) findViewById(R.id.imageView3);// 为imageView绑定图片ID

		imageView_Option.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:按下状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					imageView_Option.setImageResource(R.drawable.option0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 滑动
					imageView_Option.setImageResource(R.drawable.option0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 抬起
					imageView_Option.setImageResource(R.drawable.option1);
					// TODO：此处添加设置界面入口代码
					System.out.println("【游戏设置】按钮被点击");
					Intent intent = new Intent(MainActivity.this,
							OptionActivity.class);

					startActivity(intent);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 【游戏帮助】按钮被点击
	 * */
	public void theHelpButtonCliclked() {
		imageView_Help = (ImageView) findViewById(R.id.imageView4);// 为imageView绑定图片ID

		imageView_Help.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:按下状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					imageView_Help.setImageResource(R.drawable.help0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 滑动
					imageView_Help.setImageResource(R.drawable.help0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 抬起
					imageView_Help.setImageResource(R.drawable.help1);
					// TODO：此处添加帮助界面入口代码
					System.out.println("【游戏帮助】按钮被点击");
					// setContentView(R.layout.help);
					Intent intent = new Intent(MainActivity.this,
							HelpActivity.class);

					startActivity(intent);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 【关于游戏】按钮被点击
	 * */
	public void theAboutButtonCliclked() {
		imageView_About = (ImageView) findViewById(R.id.imageView5);// 为imageView绑定图片ID

		imageView_About.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:按下状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					imageView_About.setImageResource(R.drawable.about0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// 滑动
					imageView_About.setImageResource(R.drawable.about0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 抬起
					imageView_About.setImageResource(R.drawable.about1);
					// TODO：此处添加关于界面入口代码
					System.out.println("【关于游戏】按钮被点击");

					Intent intent = new Intent(MainActivity.this,
							AboutMeActivity.class);

					startActivity(intent);
					return true;
				}
				return false;
			}
		});
	}
}
