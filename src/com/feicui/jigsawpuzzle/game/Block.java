package com.feicui.jigsawpuzzle.game;

import android.graphics.Bitmap;

public class Block {
	int id;// ����ID
	int row, col;// �кţ��к�
			// row=id/3,col=id%3;
	Bitmap part;
	int step;
}
