package com.feicui.jigsawpuzzle.logoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.feicui.jigsawpuzzle.R;

public class Logo extends View implements Runnable {
	
//	Bitmap bitmap[] = new Bitmap[3];// 图像
	Bitmap bitmap[] = new Bitmap[1];// 图像
	Bitmap bitmap2;// 白板
	int index=0;// logo索引
	Main macontext;


	public Logo(Context context) {
		super(context);
		macontext = (Main) context;
//		bitmap[0] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo12);// 按比例显示
//		bitmap[1] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo22);// 按比例显示
//		bitmap[3] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo3);// 按比例显示
		bitmap[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo3);// 按比例显示
		new Thread(this).start();// 开启线程
	}

	@Override
	// 画图，canvas：画板
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		// 载入图片

		// getWidth() 获取屏幕宽度
		// getHeight() 获取屏幕高度
		// bitmap.getWidth() 获取图片宽度
		// bitmap.getHeight() 获取图片高度

//		int x = (getWidth() - bitmap[index].getWidth()) / 2;
//		int y = (getHeight() - bitmap[index].getHeight()) / 2;
//		canvas.drawBitmap(bitmap[index], x, y, null);
		int x = (getWidth() - bitmap[0].getWidth()) / 2;
		int y = (getHeight() - bitmap[0].getHeight()) / 2;
		canvas.drawBitmap(bitmap[0], x, y, null);
		// canvas.drawBitmap(bitmap, left, top, paint);
		// bitmap:要画的图形，left：左上角横坐标，top：左上角纵坐标，paint：画笔
//		if (bitmap[index] != null && bitmap[index].isRecycled() && index < 2) {
//			bitmap[index].recycle();// bitmap回收
//		}
		if (bitmap[0] != null && bitmap[0].isRecycled() ) {
			bitmap[0].recycle();// bitmap回收
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		for (int i = 0; i < 3; i++) {
			postInvalidate();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if (i<2) {
//				index++;
//			}
//		}
		
		gotoMain();
	}
	/**转跳MainActivity*/
	public void gotoMain() {
		macontext.gotoMainActivity();
	}
}
