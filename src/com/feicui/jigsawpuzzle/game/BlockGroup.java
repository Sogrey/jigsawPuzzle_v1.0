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
	};//�ƶ�ģʽ�°׸����ƶ�����ö��

	Random random;
	int mLevel;// �Ѷȵȼ�
	int mMode;// ��Ϸģʽ
	Block[][] mBlocks,mBlocks2; // ͼƬ�����,mBlocks2�н�
	int idCache=-1;//�Ե�ģʽ�»����һ��ͼƬ���id��Ĭ�ϸ�ֵ-1��û�л���
	Rect[][] mRegions;// ���ξ���
	int mWidth, mHeight;// ÿ��ͼƬ�����
	int mOffsetX, mOffsetY;// ƫ����
	int mWhiteRow, mWhiteCol;// �հ׸���������
	int mWhiteRowSave, mWhiteColSave;// //�հ׸���������������¼
	/** С���ͼ�ı�� */
	private int BLOCK_WHITE_ID;

	int step = 0;// ��������

	String strBlockSteps = "";// ׼���׶δ�������
	String strBlocks = "";// ׼����ɺ���ҵĸ�������
	String strSteps = "";// ��Ϸ��ʼ������ƶ��׸��ӵ����У�0��left,1��top,2��right,3��bottom

	int readyStep = 0;// ׼������
	int reallyStep = 0;// ׼���׶�ʵ���ߵĲ���

	LinkedList<Dir> history;// �ƶ�ģʽ��ʷ��¼��
	LinkedList<Integer> history_Exchange;// �Ե�ģʽ��ʷ��¼��
	

	public BlockGroup(Bitmap image) {

	}

	public BlockGroup(Bitmap image, int level) {
		random = new Random();
		history = new LinkedList<BlockGroup.Dir>();// LinkedList��ʷ��¼ʵ����
		history_Exchange = new LinkedList<Integer>();// LinkedList��ʷ��¼ʵ����
		
		mLevel = level;
		mMode = GameSceneActivity.mode;
		mBlocks = new Block[level][level];// ������������
		mBlocks2 = new Block[level][level];// ͼƬ�����,mBlocks2�н�
		mRegions = new Rect[level][level];// ������������
		mWidth = image.getWidth() / level;// ȡ��ÿ��С��ͼƬ���
		mHeight = image.getHeight() / level;// ȡ��ÿ��С��ͼƬ�ĸ߶�
		for (int row = 0; row < level; row++) {
			for (int col = 0; col < level; col++) {
				mBlocks[row][col] = new Block();// ����ÿ�����ӳ�Ա
				mBlocks[row][col].id = row * level + col;// ����ÿ������ID
				mBlocks[row][col].part = Bitmap.createBitmap(image, col
						* mWidth, row * mHeight, mWidth, mHeight);// /����ÿ��С��ͼƬ��
				mRegions[row][col] = new Rect(col * mWidth, row * mHeight,
						(col + 1) * mWidth, (row + 1) * mHeight);// /����ÿ��С����ο�
			}
		}
		if (mMode == 1) {
			/* ���ÿհ׸��� */
			mBlocks[level - 1][level - 1].part = Bitmap.createBitmap(mWidth,
					mHeight, Bitmap.Config.ARGB_8888);// �����һ��������Ϊ�հ׸���
			mBlocks[level - 1][level - 1].id = level * level - 1;// ���ÿհ׸���ID
			Canvas canvas = new Canvas(mBlocks[level - 1][level - 1].part);// ���ڸÿհ׸����½�һ������
			canvas.drawColor(Color.RED);// ���ÿհ׸��ӱ���ɫΪ��ɫ
			mWhiteRow = level - 1;// �հ׸����кţ����һ�У�
			mWhiteCol = level - 1;// �հ׸����кţ����һ�У�
			BLOCK_WHITE_ID = mBlocks[level - 1][level - 1].id;
		}
	}

	/** ƫ���� */
	public void setOffset(int x, int y) {
		mOffsetX = x;// ͼƬ�������븸�������ƫ��
		mOffsetY = y;// ͼƬ�������븸��������ƫ��
	}

	/** ��ȡ������ͼ */
	public Block[][] getMap() {
		Block[][] result = new Block[mLevel][mLevel];
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				result[row][col] = mBlocks[row][col];
			}
		}
		return result;
	}

	/** ��ȡ������ͼ��ʼ�ط� */
	public void getSet(Block[][] map) {
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				mBlocks[row][col] = map[row][col];
			}
		}
	}

	/** ��ͼ������ */
	public void draw(Canvas canvas) {
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				canvas.drawBitmap(mBlocks[row][col].part, // λ��row��col�е�ͼƬ��
						mOffsetX + col * (mWidth + 1),// ��ͼƬ���ȣ�+1���ָ��ߣ�
						mOffsetY + row * (mHeight + 1), null);// ��ͼƬ��߶ȣ�+1���ָ��ߣ�
			}
		}
	}

	/**
	 * �ж���Ӯ��� ����ֵ��boolean.Ӯ��true
	 * */
	public boolean isWin() {
		for (int row = 0; row < mLevel; row++) {
			for (int col = 0; col < mLevel; col++) {
				if (mBlocks[row][col].id != row * mLevel + col) {
					return false;// ��һ��λ�ò��Է���false
				}
			}
		}
		return true;// ȫ����ȷ����true
	}

	/** ����¼� */
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
						 * ���ж��ǵ�һ��ͼƬ���ǵڶ���(��������Чѡ��) ��һ��id���ݴ�������
						 * �ڶ������ݴ�ĵ�һ�Ų�ͬ���������ͬ������յ�һ���ݴ棬����ȡ��ѡ��
						 * ��ѭ���ж�ÿ������ͼƬ�����id�Ƿ���ȣ�
						 * mBlock[row][col].id=row*mLevel+col?? 
						 * ��������win����һ�������Ϊ !win.
						 */
						System.out.println("�Ե�ģʽ");
						step++;
						return isWin();
					}
					break;
				case 1:
				default:
					// contains�����ж�(dx, dy)������ǲ����ڵ�row�е�col�еľ���������
					if (mRegions[row][col].contains(dx, dy)
							&& checkBlock(row, col)) {
						System.out.println("�ƶ�ģʽ");
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
	 * �����жϲ������row�е�col�е�С���ƶ� ����ֵ��boolean.true���ƶ��ɹ���false���ƶ�ʧ�ܣ���δ�ƶ���
	 */
	private boolean checkBlock(int row, int col) {
		boolean result = false;// �ƶ�ǰ��־λ����ʶ��δ�ƶ�
		// row>0��ʾ���ǵ�0�У�mBlocks[row - 1][col]�Ǳ������Ϸ�����
		// �ж����Ϸ��ķ����ǲ��ǰ�ɫ�飬������򽻻�����
		if (row > 0 && mBlocks[row - 1][col].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row - 1, col);
			history.add(Dir.BOTTOM);
			strSteps += 3 + ",";// 3:�£��׸�������
			result = true;
		}
		// �ж����·��ķ����ǲ��ǰ�ɫ�飬������򽻻�����
		else if (row < mLevel - 1 && mBlocks[row + 1][col].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row + 1, col);
			history.add(Dir.TOP);
			strSteps += 1 + ",";// 1:��,�׸�������
			result = true;
		}
		// �ж����󷽵ķ����ǲ��ǰ�ɫ�飬������򽻻�����
		else if (col > 0 && mBlocks[row][col - 1].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row, col - 1);
			history.add(Dir.RIGHT);
			strSteps += 2 + ",";// 2:�ң��׸�������
			result = true;
		}
		// �ж����ҷ��ķ����ǲ��ǰ�ɫ�飬������򽻻�����
		else if (col < mLevel - 1 && mBlocks[row][col + 1].id == BLOCK_WHITE_ID) {
			swapBlock(row, col, row, col + 1);
			history.add(Dir.LEFT);
			strSteps += 0 + ",";// 0:�󣬰׸�������
			result = true;
		}
		if (result) {// �ƶ��ɹ������Ŀհ׸����к��к�
			mWhiteRow = row;
			mWhiteCol = col;
		}
		// System.out.println("step:"+strSteps);
		return result;
	}

	/**
	 * �Ե�ģʽ���ƶ��¼�
	 * <p>
	 * 
	 * @param row
	 *            Ҫ�ƶ�ͼƬ����к�
	 * @param col
	 *            Ҫ�ƶ�ͼƬ����к�
	 * @return boolean true�򽻻��ɹ���false�򽻻�ʧ��
	 * */
	public boolean swapBlock(int row, int col) {
		if (idCache == -1) {// �жϻ����Ƿ�Ϊ�գ�Ϊ�ջ�������
			idCache = row*mLevel + col; // ��һ��ͼ
			System.out.println("��һ��ͼ");
			//���򣨰ѵ�һ��ͼ�������������þ��ο����Ͻ�����
			GameView.regionLeft=col * (mWidth+1)-1+mOffsetX;
			GameView.regionTop= row * (mHeight+1)-1+mOffsetY;

		} else {
			/*
			 * ��Ϊ�գ����жϵڶ���ͼ���һ���ǲ���ͬһ��,
			 * ��ͬ�ٰѵڶ���ѡ���ͼƬ�鴫��mBlocks[row2][col2]
			 * �����򴫸�mBlocks[row][col]
			 * �ٽ���
			 */
			if (idCache != row*mLevel + col) {
				if (!GameView.isReview && !GameView.isRedo) {//�ǻط�,���ط�
					System.out.println("history_Exchange.size()="+history_Exchange.size());
					history_Exchange.add(idCache);
					history_Exchange.add(row*mLevel + col);
					System.out.println("history_Exchange.size()="+history_Exchange.size());
				}
				
				Block block = mBlocks[row][col];//�ѵڶ���ͼƬ�鴫���н�block
				mBlocks[row][col] = mBlocks[idCache/mLevel][idCache%mLevel];//�ٰѵ�һ��ͼ���洫���ڶ���ͼ
				mBlocks[idCache/mLevel][idCache%mLevel] = block;//�����н�block��ֵ������һ��ͼ
				System.out.println("�ڶ������һ�Ų�ͬ");
				idCache = -1;//�����ɹ���idCache��Ϊ-1,��ͬ
				GameView.regionLeft=-1;
				GameView.regionTop=-1;
				return true;
			}
			idCache = -1;//����ʧ�ܣ���Ϊ��һ��ͼƬ��ڶ�����ͬһ�ţ�idCache��Ϊ-1
			GameView.regionLeft=-1;
			GameView.regionTop=-1;
		}
		return false;
	}

	/**
	 * �ƶ�ģʽ���ƶ��¼�
	 * <p>
	 * 
	 * @param row
	 *            Ҫ�ƶ�ͼƬ����к�
	 * @param col
	 *            Ҫ�ƶ�ͼƬ����к�
	 * @param row2
	 *            Ҫ�ƶ����Ŀհ׸����к�
	 * @param col2
	 *            Ҫ�ƶ����Ŀհ׸����к�
	 * */
	public void swapBlock(int row, int col, int row2, int col2) {
		Block block = mBlocks[row][col];
		mBlocks[row][col] = mBlocks[row2][col2];
		mBlocks[row2][col2] = block;
	}

	/** �������� */
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
			case LEFT:// ��
				if (mWhiteCol - 1 >= 0) {
					swapBlock(mWhiteRow, mWhiteCol, mWhiteRow, --mWhiteCol);
				}
				break;
			case TOP:// ��
				if (mWhiteRow - 1 >= 0) {
					swapBlock(mWhiteRow, mWhiteCol, --mWhiteRow, mWhiteCol);
				}
				break;
			case RIGHT:// ��
				if (mWhiteCol + 1 < mLevel) {
					swapBlock(mWhiteRow, mWhiteCol, mWhiteRow, ++mWhiteCol);
				}
				break;
			case BOTTOM:// ��
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



	// ��ȡ���߶��ٲ�
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	// ��GameView��ȡ׼�����ٲ�
	public void setReadyStep(int readyStep) {
		this.readyStep = readyStep;
	}
}