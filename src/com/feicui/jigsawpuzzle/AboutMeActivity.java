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
		setFullScreen();//ȫ��
		setContentView(R.layout.about);// ���ص�һ��xml�ļ���about,����
//		imageView_Back=(ImageView) findViewById(R.id.imageViewAboutBack);
//		theBackButtonCilcked();
	}
	
/*	private void theBackButtonCilcked() {
		// TODO Auto-generated method stub
		imageView_Back.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction()==MotionEvent.ACTION_DOWN) {//����
					imageView_Back.setImageResource(R.drawable.back1);
					return true;
				}
				if (event.getAction()==MotionEvent.ACTION_MOVE) {//����
					imageView_Back.setImageResource(R.drawable.back1);
					return true;
				}
				if (event.getAction()==MotionEvent.ACTION_UP) {//̧��
					imageView_Back.setImageResource(R.drawable.back0);
//					TODO���˴���ӹ��ڽ�����ڴ���
					System.out.println("�����ء���ť�����");

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
	 * ȫ��
	 * */
	public void setFullScreen(){
		//ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}
	
	
	/**����ʽ����-����*/
	public void about(){
		AlertDialog.Builder aboutMe=new AlertDialog.Builder(this);
		aboutMe.setIcon(R.drawable.tip);
		aboutMe.setTitle("������");
		aboutMe.setMessage("ƴͼ��Ϸ\n�汾��v1.0\n\n������Sogrey\n�ͷ���·��ѷ(����ͷ�)\n" +
				"�ͷ��绰����029��88888888\n��ַ��www.Sogrey.com\nEmil:408270653@qq.com\n\n" +
				"copyright(C)Sogrey EMERALD WIT");
		aboutMe.setPositiveButton("ȷ��", null);
		aboutMe.show();
	}
}
