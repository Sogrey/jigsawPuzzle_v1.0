package com.feicui.jigsawpuzzle.game;

import android.app.Activity;
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

import com.feicui.jigsawpuzzle.MainActivity;
import com.feicui.jigsawpuzzle.R;


public class SelectModeActivity extends Activity {

	ImageView imageView_Move, imageView_Exchange;
	int mode ;//游戏模式
	
	private void setMode(int mode) {
		// TODO Auto-generated method stub
		this.mode=mode;
	}
	
	/**返回游戏模式*/
	public int getMode(){
		return mode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();// 全屏
		setContentView(R.layout.playlist);
		Log.i("jp", "PlayListActivity onCreate");
		theModeMoveButtonClicked();
		theModeExchangeButtonClicked();
	}

	private void theModeExchangeButtonClicked() {
		// TODO Auto-generated method stub
		imageView_Exchange = (ImageView) findViewById(R.id.imageView_mode_exchange_button1);
		imageView_Exchange.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					imageView_Exchange
							.setImageResource(R.drawable.mode_exchange_button0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					imageView_Exchange
							.setImageResource(R.drawable.mode_exchange_button0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					imageView_Exchange
							.setImageResource(R.drawable.mode_exchange_button1);
					System.out.println("你点击了【对调模式】");
					mode = 0;// 对调模式
					SelectModeActivity.this.setMode(mode);
					System.out.println(mode);
					gotoSelectPictureAndLevel();
//					theLevelOfMode(mode);//选择此模式下难度级别
					return true;
				}
				return false;
			}


		});
	}

	private void theModeMoveButtonClicked() {
		// TODO Auto-generated method stub
		imageView_Move = (ImageView) findViewById(R.id.imageView_mode_move_button1);
		imageView_Move.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					imageView_Move
							.setImageResource(R.drawable.mode_move_button0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					imageView_Move
							.setImageResource(R.drawable.mode_move_button0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					imageView_Move
							.setImageResource(R.drawable.mode_move_button1);
					System.out.println("你点击了【移动模式】");
					mode = 1;// 移动模式
					SelectModeActivity.this.setMode(mode);
					System.out.println(mode);
					gotoSelectPictureAndLevel();
//					theLevelOfMode(mode);//选择此模式下难度级别
					return true;
				}
				return false;
			}
		});
	}
	/**跳转至图片和难度等级选择页面*/
	private void gotoSelectPictureAndLevel() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(SelectModeActivity.this,SelectPictureAndLevel.class);
		intent.putExtra("mode",mode);
		startActivity(intent);
		finish();
	}

	/**点击返回键，返回到PlayListActivity*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK){
			System.out.println("你按下了返回键");
			Intent intent=new Intent(SelectModeActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
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

	}
}
