package com.feicui.jigsawpuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class PauseActivity extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();//ȫ��
		setContentView(R.layout.pause);//���ص�һ��xml�ļ���activity_main
	}

	/**
	 * ȫ��
	 * */
	public  void setFullScreen(){
		//ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}
}
