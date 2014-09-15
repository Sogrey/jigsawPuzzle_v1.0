package com.feicui.jigsawpuzzle.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.feicui.jigsawpuzzle.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {
	int[] imageView = { R.drawable.pic00, R.drawable.pic01, R.drawable.pic02,
			R.drawable.pic03, R.drawable.pic04, R.drawable.pic05,
			R.drawable.pic06, R.drawable.pic07, R.drawable.pic08,
			R.drawable.pic09, R.drawable.pic10, R.drawable.pic11,
			R.drawable.pic12, R.drawable.pic13, R.drawable.pic14,
			R.drawable.pic15, R.drawable.pic16, R.drawable.pic17,
			R.drawable.pic18, R.drawable.pic19, R.drawable.pic20,
			R.drawable.pic21, R.drawable.pic22, R.drawable.pic23,
			R.drawable.pic24, R.drawable.pic25, R.drawable.pic26,
			R.drawable.pic27, R.drawable.pic28, R.drawable.pic29,
			R.drawable.pic30, R.drawable.pic31, R.drawable.pic32, };// 图片集
	private final static int STATE_BEFORE_READY = 0x0;// 准备前
	private final static int STATE_READY = 0x01;// 准备
	private final static int STATE_RUN = 0x02;// 游戏状态
	private final static int STATE_WIN = 0x03;// 赢了
	private final static int STATE_PAUSE = 0x04;// 暂停
	private final static int STATE_PREVIEW = 0x05;// 预览
	private final static int STATE_REVIEW = 0x06;// 预览
	private final static int READY_STEP = 500;// 打乱步数
	int mState = STATE_BEFORE_READY;
	int rStep = 0;// 准备阶段步数记录
	int step;// 游戏阶段步数记录
	int stepForward;// 游戏阶段步数记录(前一步)

	int mBitmapId;// 要切割的图片ID
	int mLevel;// 游戏等级（mLevel*mLevel）
	int mMode;// 游戏模式：0：对调模式，1：移动模式
	String modeText, levelText;// 模式，难度级别文本

	Bitmap mBig;// 原图
	Bitmap mSmall;// 小图
	Bitmap mPlay;// 开始游戏
	Bitmap mPause;// 暂停
	Bitmap mSave;// 存档
	Bitmap mRedo;// 撤销
	Bitmap mReView_0, mReView_1;// 回放
	Bitmap mClock;// 时钟
	Bitmap mExit;// 退出
	Bitmap mBack_2_Select_Picture;// 返回重选图片
	Bitmap mBackGround;// 背景图片（480*800）
	Bitmap mNewScore;// 新成绩
	Bitmap mWin;// 胜利
	Bitmap mPauseTip;// 游戏暂停提示
	Rect mPlayRect, mSaveRect, mRedoRect, mReViewRect, mSmallRect, mBlockRect,
			mExitRect, mBack_2_Select_PictureRect;// 区域

	Paint mPaint = new Paint();// 画笔
	boolean isRun;// 是否在运行
	MotionEvent mEvent;// 点击状态

	long mStart;// 开始时间
	WindowManager mManager;

	BlockGroup mBlock;// 声明BlockGroup对象
	GameSceneActivity GS = new GameSceneActivity();

	long previewTime;// 预览时间
	int previewNumber = 3;// 剩余预览次数
	long previewTimeStart, previewTimeEnd;// 预览起始时间，结束时间
	int[] previewNumberView = { R.drawable.number_1, R.drawable.number_2,
			R.drawable.number_3, R.drawable.number_4, R.drawable.number_5 };

	Bitmap bitmapReady;
	int[] readyNumberView = { R.drawable.number_big_go,
			R.drawable.number_big_1, R.drawable.number_big_2,
			R.drawable.number_big_3 };
	long readyNumber = 3;
	long readyTimeStart, readyTimeEnd;// 准备计时始末
	boolean isReady;// 准备好了没？打乱顺序准备
	boolean isPreview;// 是否预览
	public static boolean isReview;// 是否回放(移动，对调)
	public static boolean isRedo;//是否重放一步(移动，对调)
	public static boolean isWin;// 赢了没
	boolean isPuaseToReady;// 是否是从pause转为ready

	String timeStr = "00:00:00";// 准备好之前都显示00:00:00
	String userName;// 玩家
	int bestRecord;// 最佳纪录

	Block[][] mMap;// 记录乱序后地图

	int mReviewSteps,mReviewSteps_Exchange;// 回放步数
	int reviewTiming;// 回放延时计时

	String mReview_type;
	int mReview_Exchange_Cache = -1;// 对调模式第一幅图缓存
	Block[][] mBlock_ExchangeBlock;// 对调模式图块

	long theTimeBeforeReView;// 回放前已用时间缓存
	long time;// 实际游戏时间
	int thePlayButtonClickTimes;// play按钮被点击次数，偶数play态，暂停；奇数pause态，RUN

	public static int regionLeft = -1, regionTop = -1;// 对调模式下第一个选中的图片块外围框的左上角坐标
	private final static int OffsetX = 10, OffsetY = 190;// Block偏移量

	OnWinListener mOnWinListener;// 声明一个OnWinListener对象（胜利监听）
	GoToOtherActivityListener mGoToOtherActivity;// 声明一个GoToOtherActivity对象（页面跳转监听）

	public GameView(Context context) {
		this(context, null);
	}

	public GameView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// Options options = new Options();
		// options.outHeight = 170;
		// options.outWidth = 130;
		// mMode=parameters.mode;

		mMode = GameSceneActivity.mode;
		// mMode = 1;// 暂时默认为移动模式
		mBitmapId = GameSceneActivity.imageId;
		switch (mMode) {
		case 0:// 对调模式
			mLevel = GameSceneActivity.level + 5;
			modeText = "对调模式";
			break;
		case 1:// 移动模式
			mLevel = GameSceneActivity.level + 3;
			modeText = "移动模式";
			break;
		}
		levelText = mLevel + "×" + mLevel;
		System.out.println("GameView参数：" + mMode + ":" + mBitmapId + ":"
				+ mLevel);// 输出参数，查看是否正确传参

		mBig = BitmapFactory.decodeResource(getResources(),
				imageView[mBitmapId]);// 原图
		mSmall = Bitmap.createScaledBitmap(mBig, // 原图
				130,// 宽度
				170,// 高度
				false);// 缩略图
		mWin = BitmapFactory.decodeResource(getResources(), R.drawable.win);// WIN图片
		mNewScore = BitmapFactory.decodeResource(getResources(),
				R.drawable.newscore);// 新纪录图片
		mBackGround = BitmapFactory.decodeResource(getResources(),
				R.drawable.playviewblackground);// 背景图片
		mPauseTip = BitmapFactory.decodeResource(getResources(),
				R.drawable.pausetip);// 暂停图片
		// 控制面板按钮
		mReView_0 = getButtons(R.drawable.review_0);// 回放0
		mReView_1 = getButtons(R.drawable.review_1);// 回放1
		// Rect mRectReview=new Rect(left,top,right,bottom);//原型
		// mRectReview.contains(x, y);
		mPlay = getButtons(R.drawable.play);// play按钮
		mPause = getButtons(R.drawable.pause);// 暂停按钮
		mSave = getButtons(R.drawable.save);// 存档按钮
		mRedo = getButtons(R.drawable.redo_0);// 重画按钮
		mClock = getButtons(R.drawable.clock);// 时钟
		mExit = getButtons(R.drawable.exit);// 退出按钮
		mBack_2_Select_Picture = getButtons(R.drawable.back_2_select_picture);// 返回重选图片
		mBlock = new BlockGroup(mBig, mLevel);/*
											 * 新建矩阵块对象并实例化,参数：(Bitmap
											 * image：要切割的图像,int level：难度等级)
											 */
		mBlock.setOffset(OffsetX, OffsetY);// 设置偏移量
		mPlayRect = new Rect(170, 135, 220, 175);// 暂停图区域
		mSaveRect = new Rect(220, 135, 260, 175);// 存档图区域
		mRedoRect = new Rect(270, 135, 310, 175);// 撤销图区域
		mReViewRect = new Rect(320, 135, 360, 175);// 回放图区域
		mBack_2_Select_PictureRect = new Rect(370, 135, 410, 175);// 返回重选图片图区域
		mExitRect = new Rect(420, 135, 460, 175);// 退出当前游戏图区域
		mSmallRect = new Rect(10, 10, 140, 180);// 缩略图区域
		mBlockRect = new Rect(10, 190, 470, 790);// Block区域
		getHolder().addCallback(this);
	}
	
	/**
	 * 获取按钮图标并且缩放
	 * <p>
	 * 
	 * @param buttonImageId
	 *            按钮图片ID
	 *            <p>
	 *            缩放大小40*40
	 */
	private Bitmap getButtons(int buttonImageId) {
		Bitmap button = BitmapFactory.decodeResource(getResources(),
				buttonImageId);// 回放原图
		Bitmap buttonImage = Bitmap.createScaledBitmap(button, // 原图
				40,// 宽度
				40,// 高度
				false);
		return buttonImage;
	}

	/** 点击事件 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {// 鼠标抬起时处理
			mEvent = event;
		}
		return true;// 此处返回true表示我们自己已经对触摸事件进行了处理，不希望系统再进行处理
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// surfaceView创建时调用
		isRun = true;// 游戏开始
		// Thread thread=new Thread();
		// thread.start();
		new Thread(this).start();// 开启线程
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// surfaceView改变时调用
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// surfaceView销毁时调用
		isRun = false;// 游戏结束
	}

	@Override
	public void run() {
		while (isRun) {
			try {
				isPause_Menu_Or_Back();// 判断有没有点击菜单或返回键，看是否需要暂停
				touch();// 点击事件处理
				update();// 乱序处理
				renderRun();// 画图（游戏区，图片块矩阵）
				switch (mState) {
				case STATE_RUN:// 正式游戏时候线程休眠
					Thread.sleep(100);// 休眠100毫秒
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/** 判断有没有点击菜单或返回键，看是否需要暂停 */
	private void isPause_Menu_Or_Back() {
		// System.out.println("GameScene.isPause:"+GameScene.isPause);
		if (GameSceneActivity.isPause) {
			mState = STATE_PAUSE;
		}
	}

	/** 界面刷新事件 */
	private void renderRun() {
		// 界面刷新事件
		Canvas canvas = null;
		try {
			canvas = getHolder().lockCanvas();// 获取画布，并锁定
			renderRun(canvas);// 绘图事件集
		} catch (Exception e) {
		} finally {
			if (canvas != null)
				getHolder().unlockCanvasAndPost(canvas);// 解除画布锁定
		}
	}

	private void renderRun(Canvas canvas) {
		drawBackGround(canvas);// 绘制背景图，清屏
		drawTime(canvas);// 绘制时间
		drawStep(canvas);// 显示已走的步数
		drawOption(canvas);// 画文字：游戏模式，难度级别，用户名
		drawSmall(canvas);// 绘制缩略图
		drawBlock(canvas);// 绘制拼图图片块矩阵或预览图
	}

	/** 画文字：游戏模式，难度级别，用户名 */
	private void drawOption(Canvas canvas) {
		mPaint.reset();
		mPaint.setTextSize(20);
		mPaint.setColor(0xff800909);
		if (mMode == 0) {
			canvas.drawText("游戏模式 ：对调模式", 170, 80, mPaint);// 在有序控制面板画出步数
		} else {
			canvas.drawText("游戏模式 ：移动模式", 170, 80, mPaint);// 在有序控制面板画出步数
		}
		canvas.drawText("难度级别 ：" + mLevel + "×" + mLevel, 170, 100, mPaint);// 在有序控制面板画出步数
		drawButtons(canvas);// 画按钮
	}

	/** 回放 */
	private void drawButtons(Canvas canvas) {
		canvas.drawBitmap(mClock, 170, 15, null);
		// 开始or暂停（回放时不画）
		if (mState == STATE_RUN || mState == STATE_READY) {
			canvas.drawBitmap(mPause, 170, 135, null);// 绘制暂停图
		} else
			canvas.drawBitmap(mPlay, 170, 135, null);// 绘制开始图
		// 存档
//		canvas.drawBitmap(mSave, 220, 135, null);// 绘制存档图
		// 重画
		canvas.drawBitmap(mRedo, 270, 135, null);// 绘制撤销图
		// 回放
		if (mState == STATE_REVIEW) {
			canvas.drawBitmap(mReView_1, 320, 135, null);// 绘制回放图
		} else
			canvas.drawBitmap(mReView_0, 320, 135, null);// 绘制回放图
		// 返回重选图片
		canvas.drawBitmap(mBack_2_Select_Picture, 370, 135, null);// 绘制返回重选图片按钮图
		// 退出当前游戏
		canvas.drawBitmap(mExit, 420, 135, null);// 绘制退出当前游戏按钮图
	}

	/** 显示已走的步数 */
	private void drawStep(Canvas canvas) {
		mPaint.reset();
		mPaint.setTextSize(30);
		mPaint.setColor(0xff800909);
		step = mBlock.getStep();// 取得当前步数
		if (stepForward != step) {// 判断当前步数与前一步是否相等，相等为无效点击，没有移动图片
			stepForward = step;// 把当前步数传给stepForward，以作下一次判断
			System.out.println("步数：" + step);// 不相等，打印
		}
		String reviewSteps = "";
		if (mState == STATE_REVIEW) {
			if (mReview_type == "exchange") {
				reviewSteps = " 回放：" + mReviewSteps_Exchange / 2;
			} else if (mReview_type == "move") {
				reviewSteps = " 回放：" + mReviewSteps;
			}
		}
		canvas.drawText("步数：" + step + reviewSteps, 170, 130, mPaint);// 在有序控制面板画出步数
	}

	/** 绘制背景图 */
	private void drawBackGround(Canvas canvas) {
		canvas.drawBitmap(mBackGround, 0, 0, null);// 绘制背景图
	}

	/** 绘制框图（用于对调模式下标识第一个选中的图片块） */
	private void drawRegion(Canvas canvas) {
		if (regionLeft != -1 && regionTop != -1) {// 左上角坐标有效
			mPaint.reset();
			mPaint.setStrokeWidth(2);// 线宽
			mPaint.setColor(0xff00ff00);// 绿色
			mPaint.setStyle(Style.STROKE);// 空心风格样式

			canvas.drawRect(regionLeft, regionTop, regionLeft + mBig.getWidth()
					/ mLevel, regionTop + mBig.getHeight() / mLevel, mPaint);
		}
	}

	/** 绘制拼图图片块矩阵 */
	private void drawBlock(Canvas canvas) {
		switch (mState) {
		case STATE_READY:// 准备
			drawReady(canvas);// 画准备态Block
			break;
		case STATE_PREVIEW:// 预览
		case STATE_BEFORE_READY://准备前
			canvas.drawBitmap(mBig, 10, 190, null);// 绘制原图，预览
			break;
		case STATE_RUN:// 运行
			mBlock.draw(canvas);// 没有在预览，画出拼图区域
			if (mMode == 0) {// 对调模式下，画框
				drawRegion(canvas);
			}
			break;
		case STATE_PAUSE:// 暂停
			mBlock.draw(canvas);// 没有在预览，画出拼图区域
			canvas.drawBitmap(mPauseTip, 10, 190, null);// 绘制暂停
			break;
		case STATE_WIN:// 赢
			canvas.drawBitmap(mBig, 10, 190, null);// 绘制原图，预览
			canvas.drawBitmap(mWin, (getWidth() - mWin.getWidth()) / 2,
					(getHeight() - 180 - mWin.getHeight()) / 2 + 180, null);// 绘制WIN
			break;

		case STATE_REVIEW://回放
			mBlock.draw(canvas);// 画出拼图区域
			if (mReview_type == "exchange") {
				Review_Exchange(canvas);
			} else if (mReview_type == "move") {
				Review();
			}
			System.out.println("回放模式：" + mReview_type);
			break;
		}
	}

	/** 对调模式回放事件 */
	private void Review_Exchange(Canvas canvas) {
		 reviewTiming++;
		 if (reviewTiming % 10 == 0) {
			
			if (mReviewSteps_Exchange < mBlock.history_Exchange.size()) {
				System.out.println(mReviewSteps_Exchange+":"+mBlock.history_Exchange.size());
				int mReview_Exchange = mBlock.history_Exchange
						.get(mReviewSteps_Exchange);
				int row = mReview_Exchange / mLevel;
				int col = mReview_Exchange % mLevel;

				 mBlock.swapBlock(row, col);
				 mReviewSteps_Exchange++;// 回放步数自增1
				System.out.println(true + ":" + mReviewSteps_Exchange);

			} else {
				System.out.println("回放完成");
				mStart = System.currentTimeMillis();// 重新取时间起点
				isReview = false;// 回放结束
				if (isWin) 
					mState = STATE_WIN;// 回放完成状态切换回胜利状态
			    else
					mState = STATE_RUN;// 回放完成状态切换回运行状态

			}
		} 
	}
	/** 移动模式回放事件 */
	private void Review() {
		reviewTiming++;
		if (reviewTiming % 20 == 0) {

			if (mReviewSteps < mBlock.history.size()) {
				BlockGroup.Dir dir = mBlock.history.get(mReviewSteps);
				switch (dir) {// 白格子移动方向
				case LEFT:// 左
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow, mBlock.mWhiteCol - 1);
					mBlock.mWhiteCol -= 1;
					break;
				case TOP:// 上
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow - 1, mBlock.mWhiteCol);
					mBlock.mWhiteRow -= 1;
					break;
				case RIGHT:// 右
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow, mBlock.mWhiteCol + 1);
					mBlock.mWhiteCol += 1;
					break;
				case BOTTOM:// 下
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow + 1, mBlock.mWhiteCol);
					mBlock.mWhiteRow += 1;
					break;
				}
				mReviewSteps++;// 回放步数自增1
			} else {
				System.out.println("回放完成");
				mStart = System.currentTimeMillis();// 重新取时间起点
				isReview = false;// 回放结束
				if (isWin) 
					mState = STATE_WIN;// 回放完成状态切换回胜利状态
			    else
					mState = STATE_RUN;// 回放完成状态切换回运行状态
			}
		}
	}

	/** 绘制Ready */
	private void drawReady(Canvas canvas) {
		readyTimeEnd = System.currentTimeMillis();// 准备终止时间
		readyNumber = 3 - (readyTimeEnd - readyTimeStart) / 1000;
		if (readyNumber >= 0) {
			bitmapReady = BitmapFactory.decodeResource(getResources(),
					readyNumberView[(int) readyNumber]);// 原图
			canvas.drawBitmap(mBig, 10, 190, null);// 绘制原图
			canvas.drawBitmap(bitmapReady, 10, 190, null);// 绘制开始游戏倒计数
		} else {
			mState = STATE_RUN;// 乱序准备完成，修改状态为游戏状态
			mStart = System.currentTimeMillis();// 取游戏正式开始时间点，获取当前毫秒数，起始时间1970年1月1日
			isReady = true;// 准备完成了，可以游戏了
		}
	}

	/** 绘制缩略图 */
	private void drawSmall(Canvas canvas) {
		canvas.drawBitmap(mSmall, 10, 10, null);
		if (mState == STATE_PREVIEW) {
			drawPreviewTime(canvas);// 预览计时
			Bitmap previewTimePicture = BitmapFactory.decodeResource(
					getResources(), previewNumberView[(int) previewTime]);// 原图
			canvas.drawBitmap(previewTimePicture, 10, 10, null);// 绘制预览倒计数
		}
	}

	/** 预览计时 */
	private void drawPreviewTime(Canvas canvas) {
		if (mState == STATE_PREVIEW) {
			previewTimeEnd = System.currentTimeMillis();// 获取预览终止时间
			previewTime = 5 - (previewTimeEnd - previewTimeStart) / 1000;// 剩余预览时间
			if (previewTime < 0 )// 预览时间用尽，判断是否WIN结束预览,不是返回RUN
				mState = STATE_RUN;// 预览完成更改状态为游戏状态
		}
	}

	/** 绘制时间 */
	private void drawTime(Canvas canvas) {
		// DisplayMetrics metrics = new DisplayMetrics();
		// Display display = mManager.getDefaultDisplay();
		// display.getMetrics(metrics);
		// float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
		// metrics);
		mPaint.reset();
		mPaint.setTextSize(50);
		mPaint.setColor(0xfffba208);
		if (isReady) {// 准备好之后显示实际游戏时间
			switch (mState) {

			case STATE_PREVIEW:
			case STATE_RUN:
				long mEnd = System.currentTimeMillis();// 获取终止时间
				time = (mEnd - mStart) / 1000 + theTimeBeforeReView;// 时间差（秒）+
																	// 预览前时间
				long second = time % 60;// 秒
				long minute = (time / 60) % 60;// 分
				long hour = time / 60 / 60;// 时
				timeStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);// 完整时间字符串
				break;
			case STATE_REVIEW:
			case STATE_WIN:
			case STATE_PAUSE:
				mPaint.reset();
				mPaint.setTextSize(50);
				mPaint.setColor(0xffffffff);
				break;
			}
		}
		canvas.drawText(timeStr, 220, 50, mPaint);
	}

	/** 点击事件 */
	private void touch() {
		// 点击事件
		if (mEvent != null) {
			int x = (int) mEvent.getX();
			int y = (int) mEvent.getY();
			System.out.println(x + ":" + y);
			if (mPlayRect.contains(x, y)) {// 开始游戏按钮被点击，
				if (isReady) {// 准备好了,但还没赢
					if (thePlayButtonClickTimes++ % 2 == 0) {
						mState = STATE_PAUSE;// play态，暂停
						theTimeBeforeReView = time;// 缓存已用时间
						System.out.println("暂停中");
					} else {
						mState = STATE_READY;// pause态，RUN
						System.out.println("重新运行");
						isPuaseToReady = true;
						GS.set_IsPause(false);// 取消游戏场景暂停标志
						if (mBlock.isWin()) {// 暂停后，先判断是不是赢了
							System.out.println("恭喜大神，你成功了！");
							mState = STATE_WIN;// 胜利标志
//							isWin = true;
						}
						readyTimeStart = System.currentTimeMillis();// 预览起始时间
					}
				} else {// 没有准备好开始准备
					mState = STATE_READY;// 状态设为Ready
					readyTimeStart = System.currentTimeMillis();// 预览起始时间
					GS.set_IsPause(false);// 取消游戏场景暂停标志
					System.out.println("开始游戏");
				}
			}

			if (mExitRect.contains(x, y)) {// 退出当前游戏按钮被点击
				System.out.println("退出当前游戏");
				post(new Runnable() {// 子线程向主线程发送操作请求

					@Override
					public void run() {
						mGoToOtherActivity.goToOtherActivity(0);
					}
				});

			}
			if (mBack_2_Select_PictureRect.contains(x, y)) {// 返回重选图片按钮被点击
				System.out.println("返回重选图片");
				post(new Runnable() {// 子线程向主线程发送操作请求

					@Override
					public void run() {
						mGoToOtherActivity.goToOtherActivity(1);
					}
				});
			}
			if (isReady && mState != STATE_PAUSE && mState != STATE_REVIEW
					&& mState != STATE_PREVIEW) {
				// 准备好之后(不能在预览、回放、暂停、已经Win时)才允许点击事件

				if (mSaveRect.contains(x, y)) {// 存档点击范围内
					System.out.println("存档。。。");
				} else if (mSmallRect.contains(x, y)  && mState != STATE_WIN) {// 缩略图点击范围内
					System.out.println("预览");
					doPreview();
				} else if (mReViewRect.contains(x, y) && !isReview) {// 回放,正在回放时不允许点击回放按钮
					System.out.println("回放");
					switch (mMode) {
					case 0:
						doReview_Exchange();
						break;
					case 1:
					default:
						doReview();
						break;
					}
				} else if (mRedoRect.contains(x, y) && !isReview
						&& mState != STATE_WIN) {// 撤销，点一次，退一步
					System.out.println("撤销");
					switch (mMode) {
					case 0:
						doRedo_Exchange();
						break;
					case 1:
					default:
						doRedo();
						break;
					}
				} else if (mBlockRect.contains(x, y) && mState != STATE_WIN) {// 移动格子，并判断是否win
					System.out.println("选择移动图片块，并判断是否win");
					switch (mMode) {
					case 0:
					case 1:
					default:
						if (mBlock.onClick(x, y)) {
							// Toast.makeText(getContext(), "恭喜大神，你成功了！",
							// Toast.LENGTH_LONG);
							System.out.println("恭喜大神，你成功了！");
							mState = STATE_WIN;// 胜利标志
							isWin = true;
							if (mOnWinListener != null) {// 监听器不为空
								post(new Runnable() {// 子线程向主线程发送winDialogue显示请求

									@Override
									public void run() {

										// 传入时间,步数（步数统计在此之后，故在此判断WIN之后，实际步数应加1）
										mOnWinListener
												.onWin(modeText, levelText,
														(int) time, step + 1);
									}
								});
							}
						}
						break;
					}
				}
			}
			mEvent = null;
		}
	}

	/**
	 * 胜利监听方法
	 * <p>
	 * */
	public void setOnWinListener(OnWinListener winListener) {
		mOnWinListener = winListener;
	}

	/** 胜利监听接口 */
	interface OnWinListener {
		public void onWin(String mode, String level, int time, int steps);
	}

	/**
	 * 页面跳转监听方法
	 * <p>
	 * */
	public void GoToOtherActivityListener(
			GoToOtherActivityListener goToOtherActivity) {
		mGoToOtherActivity = goToOtherActivity;
	}

	/** 页面跳转监听接口 */
	interface GoToOtherActivityListener {
		public void goToOtherActivity(int index);// index：0：主页面，1：重新选图
	}

	/** 撤销（一步） */
	private void doRedo() {
		if (step > 0) {
			BlockGroup.Dir dir = mBlock.history.getLast();// 取最后一次放置动作
			switch (dir) {// 白格子移动方向
			case LEFT:// 记录中最后一次是白格子向左移动，那么撤销应该向右，正好相反，下同
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow, mBlock.mWhiteCol + 1);
				mBlock.mWhiteCol += 1;
				break;
			case TOP:// 上
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow + 1, mBlock.mWhiteCol);
				mBlock.mWhiteRow += 1;
				break;
			case RIGHT:// 右
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow, mBlock.mWhiteCol - 1);
				mBlock.mWhiteCol -= 1;
				break;
			case BOTTOM:// 下
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow - 1, mBlock.mWhiteCol);
				mBlock.mWhiteRow -= 1;
				break;
			}
			mBlock.history.removeLast();// 移除最后一步
			mBlock.setStep(step - 1);// 取得当前步数
			System.out.println("撤销一步");
			mState = STATE_RUN;// 回放完成状态切换回运行状态
		}
	}
	/** 撤销（一步） */
	private void doRedo_Exchange() {
		isRedo=true;
		if (step > 0) {
			for (int i = 0; i < 2; i++) {
				int mRedo_Exchange = mBlock.history_Exchange.getLast();// 取最后一次放置动作
				mBlock.history_Exchange.removeLast();// 移除最后一步
				
				int row = mRedo_Exchange / mLevel;
				int col = mRedo_Exchange % mLevel;
				
				mBlock.swapBlock(row, col);
			
				System.out.println(true + ":" + mRedo_Exchange);
			}

			mBlock.setStep(step - 1);// 取得当前步数
			System.out.println("撤销一步");
			mState = STATE_RUN;// 回放完成状态切换回运行状态
			isRedo=false;
		}
	}

	/** 回放初设 */
	private void doReview() {
		mState = STATE_REVIEW;// 状态改为回放
		theTimeBeforeReView = time;
		mReviewSteps = 0;// 回放步数初设为0
		mBlock.getSet(mMap);// 加载打乱后的最初Block进行回放
		mBlock.mWhiteRow = mBlock.mWhiteRowSave;
		mBlock.mWhiteCol = mBlock.mWhiteColSave;
		isReview = true;// 是否回放
		mReview_type = "move";
	}

	/** 对调模式回放初设 */
	private void doReview_Exchange() {
		mState = STATE_REVIEW;// 状态改为回放
		theTimeBeforeReView = time;
		mReviewSteps_Exchange = 0;// 回放步数初设为0
		mBlock.getSet(mMap);// 加载打乱后的最初Block进行回放
		mBlock.idCache=-1;
		isReview = true;// 是否回放
		mReview_type = "exchange";
	}

	/** 预览初设 */
	private void doPreview() {
		mState = STATE_PREVIEW;// 状态更改为预览
		previewTimeStart = System.currentTimeMillis();// 预览起始时间
		previewTime = 5;// 设置5秒预览
	}

	/** 准备乱序，获取乱序后地图 */
	private void update() {
		switch (mState) {
		case STATE_READY:
			if (!isPuaseToReady) {
				// 判断是不是从pause转到ready，不是的话（false）需要打乱，是的话（true）不需要打乱输出原来的矩阵
				for (; rStep < READY_STEP; rStep++) {
					mBlock.upset();
				}
				mMap = mBlock.getMap();// 获取乱序地图
				mBlock.setReadyStep(READY_STEP);
			}
			break;
		default:
			break;
		}
	}

}
