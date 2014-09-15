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
	// 1.onCreate ����
	// �����һ�����е�һ������onCreate
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// ȫ��
		setContentView(R.layout.activity_main);
		Log.i("jp", "MainActivity onCreate");

		theAboutButtonCliclked();// ���ڰ�ť��Ϸ�����
		theExitButtonCliclked();// �˳���ť��Ϸ�����
		theHelpButtonCliclked();// ������ť��Ϸ�����
		theListButtonCliclked();// ���а�ť��Ϸ�����
		theOptionButtonCliclked();// ���ð�ť��Ϸ�����
		theStartButtonCliclked();// ��ʼ��ť��Ϸ�����
	}

	@Override
	// 2.onStart ����
	protected void onStart() {

		super.onStart();
		Log.i("jp", "MainActivity onStart");

	}

	@Override
	// 3.onResume (�жϺ�)��������
	protected void onResume() {

		super.onResume();
		Log.i("jp", "MainActivity onResume");
	}

	@Override
	// 4.onPause ��ͣ
	protected void onPause() {

		super.onPause();
		Log.i("jp", "MainActivity onPause");
		// if (isBack) //������·��ؼ�������ʾ��ͣ����ֱ���˳�,�������back������ͣ
		// exit();//��ť�˳���Ϸ�����

	}

	@Override
	// 5.onStop ֹͣ
	protected void onStop() {

		super.onStop();
		Log.i("jp", "MainActivity onStop");
	}

	@Override
	// 6.onRestart ����
	protected void onRestart() {

		super.onRestart();
		Log.i("jp", "MainActivity onRestart");

	}

	@Override
	// 7.onDestroy ����
	protected void onDestroy() {
		
		super.onDestroy();
		Log.i("jp", "MainActivity onDestroy");

	}

	/**
	 * new Intent(packageContext, cls) packageContext:��ǰ���ڵ�Activity��.this
	 * cls:Ҫ��ת����Activity��.class startActivity(intent);����intent
	 * 
	 * */
	// ��ͣ
	public void gotoPause() {
		Intent intent = new Intent(MainActivity.this, PauseActivity.class);

		startActivity(intent);
	}

	@Override
	/**
	 *�жϰ���
	 *onKeyDown(keyCode, event)
	 * */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isBack = true;
			System.out.println("�㰴���˷��ؼ�");
			exit();
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
		System.out.println("ȫ��");
	}

	/**
	 * �˳��Ի���
	 * */
	public void exit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("�˳���ʾ");
		builder.setIcon(R.drawable.tip);
		builder.setMessage("�����Ҫ�뿪��ô��");
		builder.setNegativeButton("ȡ��", null);// �ұߵ�һ����ť������ȡ����
		// builder.setNeutralButton("�м䰴ť", null);//�м䰴ť
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				System.out.println("������ȷ����ȷ���˳�");
				MainActivity.this.finish();

			}
		});// ��ߵ�һ����ť��ȷ����
		builder.show();
		System.out.println("�����˳��Ի���");
	}

	/**
	 * ���˳���Ϸ����ť�����
	 * */
	public void theExitButtonCliclked() {
		imageView_Exit = (ImageView) findViewById(R.id.imageView6);// ΪimageView��ͼƬID
		// �������̰���
		/*
		 * imageView.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * System.out.println("�̰�");
		 * 
		 * exit(); } });
		 */
		// ����
		/*
		 * imageView.setOnLongClickListener(new OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View arg0) {
		 * 
		 * //System.out.println("����"); return true; } });
		 */
		imageView_Exit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// MotionEvent event:����״̬
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					imageView_Exit.setImageResource(R.drawable.exit0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����
					imageView_Exit.setImageResource(R.drawable.exit0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// ̧��
					imageView_Exit.setImageResource(R.drawable.exit1);
					// isBack=true;
					exit();
					System.out.println("���˳���Ϸ����ť�����");
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * ����ʼ��Ϸ����ť�����
	 * */
	public void theStartButtonCliclked() {
		imageView_Start = (ImageView) findViewById(R.id.imageView1);// ΪimageView��ͼƬID
		imageView_Start.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:����״̬
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					imageView_Start.setImageResource(R.drawable.start0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����
					imageView_Start.setImageResource(R.drawable.start0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// ̧��
					imageView_Start.setImageResource(R.drawable.start1);
					// TODO���˴���ӽ�����Ϸ�������
					System.out.println("����ʼ��Ϸ����ť�����");
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
	 * ��������������ť�����
	 * */
	public void theListButtonCliclked() {
		imageView_List = (ImageView) findViewById(R.id.imageView2);// ΪimageView��ͼƬID

		imageView_List.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:����״̬
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					imageView_List.setImageResource(R.drawable.list0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����
					imageView_List.setImageResource(R.drawable.list0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// ̧��
					imageView_List.setImageResource(R.drawable.list1);
					// TODO���˴���Ӳ鿴���������������
					System.out.println("��������������ť�����");
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
	 * ����Ϸ���á���ť�����
	 * */
	public void theOptionButtonCliclked() {
		imageView_Option = (ImageView) findViewById(R.id.imageView3);// ΪimageView��ͼƬID

		imageView_Option.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:����״̬
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					imageView_Option.setImageResource(R.drawable.option0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����
					imageView_Option.setImageResource(R.drawable.option0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// ̧��
					imageView_Option.setImageResource(R.drawable.option1);
					// TODO���˴�������ý�����ڴ���
					System.out.println("����Ϸ���á���ť�����");
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
	 * ����Ϸ��������ť�����
	 * */
	public void theHelpButtonCliclked() {
		imageView_Help = (ImageView) findViewById(R.id.imageView4);// ΪimageView��ͼƬID

		imageView_Help.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:����״̬
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					imageView_Help.setImageResource(R.drawable.help0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����
					imageView_Help.setImageResource(R.drawable.help0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// ̧��
					imageView_Help.setImageResource(R.drawable.help1);
					// TODO���˴���Ӱ���������ڴ���
					System.out.println("����Ϸ��������ť�����");
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
	 * ��������Ϸ����ť�����
	 * */
	public void theAboutButtonCliclked() {
		imageView_About = (ImageView) findViewById(R.id.imageView5);// ΪimageView��ͼƬID

		imageView_About.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				// MotionEvent event:����״̬
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					imageView_About.setImageResource(R.drawable.about0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {// ����
					imageView_About.setImageResource(R.drawable.about0);
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// ̧��
					imageView_About.setImageResource(R.drawable.about1);
					// TODO���˴���ӹ��ڽ�����ڴ���
					System.out.println("��������Ϸ����ť�����");

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
