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
	int mode ;//��Ϸģʽ
	
	private void setMode(int mode) {
		// TODO Auto-generated method stub
		this.mode=mode;
	}
	
	/**������Ϸģʽ*/
	public int getMode(){
		return mode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();// ȫ��
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
					System.out.println("�����ˡ��Ե�ģʽ��");
					mode = 0;// �Ե�ģʽ
					SelectModeActivity.this.setMode(mode);
					System.out.println(mode);
					gotoSelectPictureAndLevel();
//					theLevelOfMode(mode);//ѡ���ģʽ���Ѷȼ���
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
					System.out.println("�����ˡ��ƶ�ģʽ��");
					mode = 1;// �ƶ�ģʽ
					SelectModeActivity.this.setMode(mode);
					System.out.println(mode);
					gotoSelectPictureAndLevel();
//					theLevelOfMode(mode);//ѡ���ģʽ���Ѷȼ���
					return true;
				}
				return false;
			}
		});
	}
	/**��ת��ͼƬ���Ѷȵȼ�ѡ��ҳ��*/
	private void gotoSelectPictureAndLevel() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(SelectModeActivity.this,SelectPictureAndLevel.class);
		intent.putExtra("mode",mode);
		startActivity(intent);
		finish();
	}

	/**������ؼ������ص�PlayListActivity*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK){
			System.out.println("�㰴���˷��ؼ�");
			Intent intent=new Intent(SelectModeActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
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
}
