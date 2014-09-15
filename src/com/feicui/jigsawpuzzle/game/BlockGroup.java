package com.feicui.jigsawpuzzle.game;

import java.util.LinkedList;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class BlockGroup {
	public enum Dir {
		LEFT, TOP, RIGHT, BOTTOM
	};//移动模式下白格子移动方向枚举

	Random random;
	int mLevel;// 难度等级
	int mMode;// 游戏模式
	Block[][] mBlocks,mBlocks2; // 图片块矩阵,mBlocks2中介
	int idCache=-1;//对调模式下缓存第一张图片块的id，默认赋值-1即没有缓存
	Rect[][] mRegions;// 矩形矩阵
	int mWidth, mHeight;// 每个图片块宽，高
	int mOffsetX, mOffsetY;// 偏移量
	int mWhiteRow, mWhiteCol;// 空白格子行与列
	int mWhiteRowSave, mWhiteColSave;// //空白格子行与列乱序后记录
	/** 小格白图的编号 */
	private int BLOCK_WHITE_ID;

	int step = 0;// 操作步数

	String strBlockSteps = "";// 准备阶段打乱序列
	String strBlocks = "";// 准备完成后打乱的格子序列
	String strSteps = "";// 游戏开始后玩家移动白格子的序列：0：left,1：top,2：right,3：bottom

	int readyStep = 0;// 准备步数
	int reallyStep = 0;// 准备阶段实际走的步数

	LinkedList<Dir> history;// 移动模式历史记录链
	LinkedList<Integer> history_Exchange;// 对调模式历史记录链
	

	public BlockGroup(Bitmap image) {

	}

	public BlockGroup(Bitmap image, int level) {
		random = new Random();
		history = new LinkedList<BlockGroup.Dir>();// LinkedList历史记录实例化
		history_Exchange = new LinkedList<Integer>();// LinkedList历史记录实例化
		
		mLevel = level;
		mMode = GameSceneActivity.mode;
		mBlocks = new Block[level][level];// 创建格子数组
		mBlocks2 = new Block[level][level];// 图片块矩阵,mBlocks2中介
		mRegions = new Rect[level][level];// 创建矩形数组
		mWidth = image.getWidth() / level;// 取得每个小块图片宽度
		mHeight = image.getHeight() / level;// 取得每个小块图片的高度
		for (int row = 0; row < level; row++) {
			for (int col = 0; col < level; col++) {
				mBlocks[row][col] = new Block();// 创建每个格子成员
				mBlocks[row][col].id = row * level + col;// 设置每个格子ID
				mBlocks[row][col].part = Bitmap.createBitmap(image, col
						* mWidth, row * mHeight, mWidth, mHeight);// /创建每个小块图片块
				mRegions[row][col] = new Rect(col * mWidth, row * mHeight,
						(col + 1) * mWidth, (row + 1) * mHeight);// /创建每个小块矩形块
			}
		}
		if (mMode == 1) {
			/* 设置空白格子 */
			mBlocks[level - 1][level - 1].part = Bitmap.createBitmap(mWidth,
					mHeight, Bitmap.Config.ARGB_8888);// 将最后一个格子设为空白格子
			mBlocks[level - 1][level - 1].id = level * level - 1;// 设置空白格子ID
			Canvas canvas = new Canvas(mBlocks[level - 1][level - 1].part);// 基于该空白格子新建一个画布
			canvas.drawColor(Color.RED);// 设置空白格子背景色为灰色
			mWhiteRow = level - 1;// 空白格子行号（最后一行）
			mWhiteCol = level - 1;// 空白格子列号（最后一列）
			BLOCK_WHITE_ID = mBlocks[level - 1][level - 1].id;
		}
	}

	/** 偏移量 */
	public void setOffset(int x, int y) {
		mOffsetX = x;// 图片块矩阵距离父容器左边偏移
		mOffsetY = y;// 图片块矩阵距离父容器顶边偏移
	}

	/** 获取乱序后地图 */
	public Block[][] getMap() {
		Block[][] result = new Block[mLevel][mLevel];
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				result[row][col] = mBlocks[row][col];
			}
		}
		return result;
	}

	/** 获取乱序后地图开始回放 */
	public void getSet(Block[][] map) {
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				mBlocks[row][col] = map[row][col];
			}
		}
	}

	/** 画图（乱序） */
	public void draw(Canvas canvas) {
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				canvas.drawBitmap(mBlocks[row][col].part, // 位于row行col列的图片快
						mOffsetX + col * (mWidth + 1),// 该图片快宽度（+1，分割线）
						mOffsetY + row * (mHeight + 1), null);// 该图片快高度（+1，分割线）
			}
		}
	}

	/**
	 * 判断输赢与否 返回值：boolean.赢：true
	 * */
	public boolean isWin() {
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				if (mBlocks[row][col].id != row * mLevel + col) {
					return false;// 有一个位置不对返回false
				}
			}
		}
		return true;// 全部正确反对true
	}

	/** 点击事件 */
	public boolean onClick(int x, int y) {
		int dx = x - mOffsetX;
		int dy = y - mOffsetY;
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				switch (mMode) {
				case 0:
					if (mRegions[row][col].contains(dx, dy) 
							&& swapBlock(row, col)) {
						/*
						 * 先判断是第一张图片还是第二张(必须是有效选择) 第一张id先暂存下来，
						 * 第二张与暂存的第一张不同则调换，相同，则清空第一张暂存，等于取消选择，
						 * 再循环判断每个格子图片与格子id是否相等，
						 * mBlock[row][col].id=row*mLevel+col?? 
						 * 如果都相等win，有一个不相等为 !win.
						 */
						System.out.println("对调模式");
						step++;
						return isWin();
					}
					break;
				case 1:
				default:
					// contains用来判断(dx, dy)这个点是不是在第row行第col列的矩形区域内
					if (mRegions[row][col].contains(dx, dy)
							&& checkBlock(row, col)) {
						System.out.println("移动模式");
						step++;
						return isWin();
					}
					break;
				}
			}
		}
		return false;
	}

	/**
	 * 用来判断并处理第row行第col列的小块移动 返回值：boolean.true：移动成功，false：移动失败（或还未移动）
	 */
	private boolean checkBlock(int row, int col) {
		boolean result = false;// 移动前标志位，标识还未移动
		// row>0表示不是第0行，mBlocks[row - 1][col]是本格正上方格子
		// 判断正上方的方块是不是白色块，如果是则交换它们
		if (row > 0 && mBlocks[row - 1][col].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row - 1, col);
			history.add(Dir.BOTTOM);
			strSteps += 3 + ",";// 3:下：白格子下移
			result = true;
		}
		// 判断正下方的方块是不是白色块，如果是则交换它们
		else if (row < mLevel - 1 && mBlocks[row + 1][col].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row + 1, col);
			history.add(Dir.TOP);
			strSteps += 1 + ",";// 1:上,白格子上移
			result = true;
		}
		// 判断正左方的方块是不是白色块，如果是则交换它们
		else if (col > 0 && mBlocks[row][col - 1].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row, col - 1);
			history.add(Dir.RIGHT);
			strSteps += 2 + ",";// 2:右：白格子右移
			result = true;
		}
		// 判断正右方的方块是不是白色块，如果是则交换它们
		else if (col < mLevel - 1 && mBlocks[row][col + 1].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row, col + 1);
			history.add(Dir.LEFT);
			strSteps += 0 + ",";// 0:左，白格子左移
			result = true;
		}
		if (result) {// 移动成功，更改空白格子行号列号
			mWhiteRow = row;
			mWhiteCol = col;
		}
		// System.out.println("step:"+strSteps);
		return result;
	}

	/**
	 * 对调模式下移动事件
	 * <p>
	 * 
	 * @param row
	 *            要移动图片块的行号
	 * @param col
	 *            要移动图片块的列号
	 * @return boolean true则交换成功，false则交换失败
	 * */
	public boolean swapBlock(int row, int col) {
		if (idCache == -1) {// 判断缓存是否为空，为空缓存下来
			idCache = row*mLevel + col; // 第一张图
			System.out.println("第一张图");
			//画框（把第一幅图框起来），设置矩形框左上角坐标
			GameView.regionLeft=col * (mWidth+1)-1+mOffsetX;
			GameView.regionTop= row * (mHeight+1)-1+mOffsetY;

		} else {
			/*
			 * 不为空，先判断第二张图与第一张是不是同一张,
			 * 不同再把第二次选择的图片块传给mBlocks[row2][col2]
			 * 缓存则传给mBlocks[row][col]
			 * 再交换
			 */
			if (idCache != row*mLevel + col) {
				if (!GameView.isReview && !GameView.isRedo) {//非回放,非重放
					System.out.println("history_Exchange.size()="+history_Exchange.size());
					history_Exchange.add(idCache);
					history_Exchange.add(row*mLevel + col);
					System.out.println("history_Exchange.size()="+history_Exchange.size());
				}
				
				Block block = mBlocks[row][col];//把第二幅图片块传给中介block
				mBlocks[row][col] = mBlocks[idCache/mLevel][idCache%mLevel];//再把第一幅图缓存传给第二幅图
				mBlocks[idCache/mLevel][idCache%mLevel] = block;//最后把中介block的值传给第一幅图
				System.out.println("第二张与第一张不同");
				idCache = -1;//交换成功，idCache置为-1,下同
				GameView.regionLeft=-1;
				GameView.regionTop=-1;
				return true;
			}
			idCache = -1;//交换失败，因为第一张图片与第二张是同一张，idCache置为-1
			GameView.regionLeft=-1;
			GameView.regionTop=-1;
		}
		return false;
	}

	/**
	 * 移动模式下移动事件
	 * <p>
	 * 
	 * @param row
	 *            要移动图片块的行号
	 * @param col
	 *            要移动图片块的列号
	 * @param row2
	 *            要移动到的空白格子行号
	 * @param col2
	 *            要移动到的空白格子列号
	 * */
	public void swapBlock(int row, int col, int row2, int col2) {
		Block block = mBlocks[row][col];
		mBlocks[row][col] = mBlocks[row2][col2];
		mBlocks[row2][col2] = block;
	}

	/** 打乱排序 */
	public void upset() {
		switch (mMode) {
		case 0:
			int[] num = new int[mLevel * mLevel];
			for (int i = 0; i < mLevel * mLevel; i++) {
				int n = random.nextInt(mLevel * mLevel);
				num[i] = n;
				for (int j = 0; j < i; j++) {
					if (num[i] == num[j]) {
						i--;
						break;
					}
				}
			}
			// System.out.println("num.length:"+num.length);
			// sop(num);
			int k = 0;
			for (int row = 0; row < mLevel; row++) {
				for (int col = 0; col < mLevel; col++) {

					mBlocks2[row][col] = mBlocks[num[k] / mLevel][num[k]
							% mLevel];
					k++;
				}
			}
			for (int row = 0; row < mLevel; row++) {
				for (int col = 0; col < mLevel; col++) {

					mBlocks[row][col] = mBlocks2[row][col];

				}
			}
			break;
		case 1:
		default:
			int index = random.nextInt(4);
			strBlockSteps += index + ",";
			// System.out.println(strBlockSteps);
			Dir dir = Dir.values()[index];
			switch (dir) {
			case LEFT:// 左
				if (mWhiteCol - 1 >= 0) {
					swapBlock(mWhiteRow, mWhiteCol, mWhiteRow, --mWhiteCol);
				}
				break;
			case TOP:// 上
				if (mWhiteRow - 1 >= 0) {
					swapBlock(mWhiteRow, mWhiteCol, --mWhiteRow, mWhiteCol);
				}
				break;
			case RIGHT:// 右
				if (mWhiteCol + 1 < mLevel) {
					swapBlock(mWhiteRow, mWhiteCol, mWhiteRow, ++mWhiteCol);
				}
				break;
			case BOTTOM:// 下
				if (mWhiteRow + 1 < mLevel) {
					swapBlock(mWhiteRow, mWhiteCol, ++mWhiteRow, mWhiteCol);
				}
				break;
			}
			mWhiteRowSave = mWhiteRow;
			mWhiteColSave = mWhiteCol;
			break;
		}

	}



	// 获取已走多少步
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	// 从GameView获取准备多少步
	public void setReadyStep(int readyStep) {
		this.readyStep = readyStep;
	}
}