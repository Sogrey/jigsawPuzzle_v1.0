package com.feicui.jigsawpuzzle.game;

import android.graphics.Bitmap;

public class Block {
	int id;// 格子ID
	int row, col;// 行号，列号
			// row=id/3,col=id%3;
	Bitmap part;
	int step;
}
