package com.feicui.jigsawpuzzle.logoview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.feicui.jigsawpuzzle.MainActivity;

public class Main extends Activity {
	
	boolean isBack=false;

	@Override
//	1.onCreate  创建
//	程序第一次运行第一个都是onCreate
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();//全屏
		setContentView(new Logo(this));
		Log.i("jp", "logo onCreate");

	}
	
	/**
	 * 全屏
	 * */
	public void setFullScreen(){
		//去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}

	public  void gotoMainActivity() {

		Intent intent=new  Intent(Main.this,
				MainActivity.class);
		isBack=true;
		startActivity(intent);
		Main.this.finish();
	}
}
