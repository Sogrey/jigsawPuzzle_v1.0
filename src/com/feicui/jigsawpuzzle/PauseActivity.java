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
		setFullScreen();//全屏
		setContentView(R.layout.pause);//加载的一个xml文件：activity_main
	}

	/**
	 * 全屏
	 * */
	public  void setFullScreen(){
		//去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}
}
