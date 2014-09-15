package com.feicui.jigsawpuzzle.logoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.feicui.jigsawpuzzle.R;

public class Logo extends View implements Runnable {
	
//	Bitmap bitmap[] = new Bitmap[3];// ͼ��
	Bitmap bitmap[] = new Bitmap[1];// ͼ��
	Bitmap bitmap2;// �װ�
	int index=0;// logo����
	Main macontext;


	public Logo(Context context) {
		super(context);
		macontext = (Main) context;
//		bitmap[0] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo12);// ��������ʾ
//		bitmap[1] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo22);// ��������ʾ
//		bitmap[3] = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo3);// ��������ʾ
		bitmap[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo3);// ��������ʾ
		new Thread(this).start();// �����߳�
	}

	@Override
	// ��ͼ��canvas������
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		// ����ͼƬ

		// getWidth() ��ȡ��Ļ���
		// getHeight() ��ȡ��Ļ�߶�
		// bitmap.getWidth() ��ȡͼƬ���
		// bitmap.getHeight() ��ȡͼƬ�߶�

//		int x = (getWidth() - bitmap[index].getWidth()) / 2;
//		int y = (getHeight() - bitmap[index].getHeight()) / 2;
//		canvas.drawBitmap(bitmap[index], x, y, null);
		int x = (getWidth() - bitmap[0].getWidth()) / 2;
		int y = (getHeight() - bitmap[0].getHeight()) / 2;
		canvas.drawBitmap(bitmap[0], x, y, null);
		// canvas.drawBitmap(bitmap, left, top, paint);
		// bitmap:Ҫ����ͼ�Σ�left�����ϽǺ����꣬top�����Ͻ������꣬paint������
//		if (bitmap[index] != null && bitmap[index].isRecycled() && index < 2) {
//			bitmap[index].recycle();// bitmap����
//		}
		if (bitmap[0] != null && bitmap[0].isRecycled() ) {
			bitmap[0].recycle();// bitmap����
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
	/**ת��MainActivity*/
	public void gotoMain() {
		macontext.gotoMainActivity();
	}
}
