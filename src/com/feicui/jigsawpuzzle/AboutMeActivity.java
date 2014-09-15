package com.feicui.jigsawpuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AboutMeActivity extends Activity {

//	ImageView imageView_Back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();//全屏
		setContentView(R.layout.about);// 加载的一个xml文件：about,关于
//		imageView_Back=(ImageView) findViewById(R.id.imageViewAboutBack);
//		theBackButtonCilcked();
	}
	
/*	private void theBackButtonCilcked() {
		// TODO Auto-generated method stub
		imageView_Back.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction()==MotionEvent.ACTION_DOWN) {//按下
					imageView_Back.setImageResource(R.drawable.back1);
					return true;
				}
				if (event.getAction()==MotionEvent.ACTION_MOVE) {//滑动
					imageView_Back.setImageResource(R.drawable.back1);
					return true;
				}
				if (event.getAction()==MotionEvent.ACTION_UP) {//抬起
					imageView_Back.setImageResource(R.drawable.back0);
//					TODO：此处添加关于界面入口代码
					System.out.println("【返回】按钮被点击");

					Intent intent=new  Intent(AboutMeActivity.this,
							MainActivity.class);
					
					startActivity(intent);
					AboutMeActivity.this.finish();
					return true;
				}
				return false;
			}
		});
	}*/

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
	
	
	/**弹出式窗口-关于*/
	public void about(){
		AlertDialog.Builder aboutMe=new AlertDialog.Builder(this);
		aboutMe.setIcon(R.drawable.tip);
		aboutMe.setTitle("关于我");
		aboutMe.setMessage("拼图游戏\n版本：v1.0\n\n制作：Sogrey\n客服：路博逊(义务客服)\n" +
				"客服电话：（029）88888888\n网址：www.Sogrey.com\nEmil:408270653@qq.com\n\n" +
				"copyright(C)Sogrey EMERALD WIT");
		aboutMe.setPositiveButton("确定", null);
		aboutMe.show();
	}
}
