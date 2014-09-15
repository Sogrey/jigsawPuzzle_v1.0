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
			R.drawable.pic30, R.drawable.pic31, R.drawable.pic32, };// ͼƬ��
	private final static int STATE_BEFORE_READY = 0x0;// ׼��ǰ
	private final static int STATE_READY = 0x01;// ׼��
	private final static int STATE_RUN = 0x02;// ��Ϸ״̬
	private final static int STATE_WIN = 0x03;// Ӯ��
	private final static int STATE_PAUSE = 0x04;// ��ͣ
	private final static int STATE_PREVIEW = 0x05;// Ԥ��
	private final static int STATE_REVIEW = 0x06;// Ԥ��
	private final static int READY_STEP = 500;// ���Ҳ���
	int mState = STATE_BEFORE_READY;
	int rStep = 0;// ׼���׶β�����¼
	int step;// ��Ϸ�׶β�����¼
	int stepForward;// ��Ϸ�׶β�����¼(ǰһ��)

	int mBitmapId;// Ҫ�и��ͼƬID
	int mLevel;// ��Ϸ�ȼ���mLevel*mLevel��
	int mMode;// ��Ϸģʽ��0���Ե�ģʽ��1���ƶ�ģʽ
	String modeText, levelText;// ģʽ���Ѷȼ����ı�

	Bitmap mBig;// ԭͼ
	Bitmap mSmall;// Сͼ
	Bitmap mPlay;// ��ʼ��Ϸ
	Bitmap mPause;// ��ͣ
	Bitmap mSave;// �浵
	Bitmap mRedo;// ����
	Bitmap mReView_0, mReView_1;// �ط�
	Bitmap mClock;// ʱ��
	Bitmap mExit;// �˳�
	Bitmap mBack_2_Select_Picture;// ������ѡͼƬ
	Bitmap mBackGround;// ����ͼƬ��480*800��
	Bitmap mNewScore;// �³ɼ�
	Bitmap mWin;// ʤ��
	Bitmap mPauseTip;// ��Ϸ��ͣ��ʾ
	Rect mPlayRect, mSaveRect, mRedoRect, mReViewRect, mSmallRect, mBlockRect,
			mExitRect, mBack_2_Select_PictureRect;// ����

	Paint mPaint = new Paint();// ����
	boolean isRun;// �Ƿ�������
	MotionEvent mEvent;// ���״̬

	long mStart;// ��ʼʱ��
	WindowManager mManager;

	BlockGroup mBlock;// ����BlockGroup����
	GameSceneActivity GS = new GameSceneActivity();

	long previewTime;// Ԥ��ʱ��
	int previewNumber = 3;// ʣ��Ԥ������
	long previewTimeStart, previewTimeEnd;// Ԥ����ʼʱ�䣬����ʱ��
	int[] previewNumberView = { R.drawable.number_1, R.drawable.number_2,
			R.drawable.number_3, R.drawable.number_4, R.drawable.number_5 };

	Bitmap bitmapReady;
	int[] readyNumberView = { R.drawable.number_big_go,
			R.drawable.number_big_1, R.drawable.number_big_2,
			R.drawable.number_big_3 };
	long readyNumber = 3;
	long readyTimeStart, readyTimeEnd;// ׼����ʱʼĩ
	boolean isReady;// ׼������û������˳��׼��
	boolean isPreview;// �Ƿ�Ԥ��
	public static boolean isReview;// �Ƿ�ط�(�ƶ����Ե�)
	public static boolean isRedo;//�Ƿ��ط�һ��(�ƶ����Ե�)
	public static boolean isWin;// Ӯ��û
	boolean isPuaseToReady;// �Ƿ��Ǵ�pauseתΪready

	String timeStr = "00:00:00";// ׼����֮ǰ����ʾ00:00:00
	String userName;// ���
	int bestRecord;// ��Ѽ�¼

	Block[][] mMap;// ��¼������ͼ

	int mReviewSteps,mReviewSteps_Exchange;// �طŲ���
	int reviewTiming;// �ط���ʱ��ʱ

	String mReview_type;
	int mReview_Exchange_Cache = -1;// �Ե�ģʽ��һ��ͼ����
	Block[][] mBlock_ExchangeBlock;// �Ե�ģʽͼ��

	long theTimeBeforeReView;// �ط�ǰ����ʱ�仺��
	long time;// ʵ����Ϸʱ��
	int thePlayButtonClickTimes;// play��ť�����������ż��play̬����ͣ������pause̬��RUN

	public static int regionLeft = -1, regionTop = -1;// �Ե�ģʽ�µ�һ��ѡ�е�ͼƬ����Χ������Ͻ�����
	private final static int OffsetX = 10, OffsetY = 190;// Blockƫ����

	OnWinListener mOnWinListener;// ����һ��OnWinListener����ʤ��������
	GoToOtherActivityListener mGoToOtherActivity;// ����һ��GoToOtherActivity����ҳ����ת������

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
		// mMode = 1;// ��ʱĬ��Ϊ�ƶ�ģʽ
		mBitmapId = GameSceneActivity.imageId;
		switch (mMode) {
		case 0:// �Ե�ģʽ
			mLevel = GameSceneActivity.level + 5;
			modeText = "�Ե�ģʽ";
			break;
		case 1:// �ƶ�ģʽ
			mLevel = GameSceneActivity.level + 3;
			modeText = "�ƶ�ģʽ";
			break;
		}
		levelText = mLevel + "��" + mLevel;
		System.out.println("GameView������" + mMode + ":" + mBitmapId + ":"
				+ mLevel);// ����������鿴�Ƿ���ȷ����

		mBig = BitmapFactory.decodeResource(getResources(),
				imageView[mBitmapId]);// ԭͼ
		mSmall = Bitmap.createScaledBitmap(mBig, // ԭͼ
				130,// ���
				170,// �߶�
				false);// ����ͼ
		mWin = BitmapFactory.decodeResource(getResources(), R.drawable.win);// WINͼƬ
		mNewScore = BitmapFactory.decodeResource(getResources(),
				R.drawable.newscore);// �¼�¼ͼƬ
		mBackGround = BitmapFactory.decodeResource(getResources(),
				R.drawable.playviewblackground);// ����ͼƬ
		mPauseTip = BitmapFactory.decodeResource(getResources(),
				R.drawable.pausetip);// ��ͣͼƬ
		// ������尴ť
		mReView_0 = getButtons(R.drawable.review_0);// �ط�0
		mReView_1 = getButtons(R.drawable.review_1);// �ط�1
		// Rect mRectReview=new Rect(left,top,right,bottom);//ԭ��
		// mRectReview.contains(x, y);
		mPlay = getButtons(R.drawable.play);// play��ť
		mPause = getButtons(R.drawable.pause);// ��ͣ��ť
		mSave = getButtons(R.drawable.save);// �浵��ť
		mRedo = getButtons(R.drawable.redo_0);// �ػ���ť
		mClock = getButtons(R.drawable.clock);// ʱ��
		mExit = getButtons(R.drawable.exit);// �˳���ť
		mBack_2_Select_Picture = getButtons(R.drawable.back_2_select_picture);// ������ѡͼƬ
		mBlock = new BlockGroup(mBig, mLevel);/*
											 * �½���������ʵ����,������(Bitmap
											 * image��Ҫ�и��ͼ��,int level���Ѷȵȼ�)
											 */
		mBlock.setOffset(OffsetX, OffsetY);// ����ƫ����
		mPlayRect = new Rect(170, 135, 220, 175);// ��ͣͼ����
		mSaveRect = new Rect(220, 135, 260, 175);// �浵ͼ����
		mRedoRect = new Rect(270, 135, 310, 175);// ����ͼ����
		mReViewRect = new Rect(320, 135, 360, 175);// �ط�ͼ����
		mBack_2_Select_PictureRect = new Rect(370, 135, 410, 175);// ������ѡͼƬͼ����
		mExitRect = new Rect(420, 135, 460, 175);// �˳���ǰ��Ϸͼ����
		mSmallRect = new Rect(10, 10, 140, 180);// ����ͼ����
		mBlockRect = new Rect(10, 190, 470, 790);// Block����
		getHolder().addCallback(this);
	}
	
	/**
	 * ��ȡ��ťͼ�겢������
	 * <p>
	 * 
	 * @param buttonImageId
	 *            ��ťͼƬID
	 *            <p>
	 *            ���Ŵ�С40*40
	 */
	private Bitmap getButtons(int buttonImageId) {
		Bitmap button = BitmapFactory.decodeResource(getResources(),
				buttonImageId);// �ط�ԭͼ
		Bitmap buttonImage = Bitmap.createScaledBitmap(button, // ԭͼ
				40,// ���
				40,// �߶�
				false);
		return buttonImage;
	}

	/** ����¼� */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {// ���̧��ʱ����
			mEvent = event;
		}
		return true;// �˴�����true��ʾ�����Լ��Ѿ��Դ����¼������˴�����ϣ��ϵͳ�ٽ��д���
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// surfaceView����ʱ����
		isRun = true;// ��Ϸ��ʼ
		// Thread thread=new Thread();
		// thread.start();
		new Thread(this).start();// �����߳�
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// surfaceView�ı�ʱ����
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// surfaceView����ʱ����
		isRun = false;// ��Ϸ����
	}

	@Override
	public void run() {
		while (isRun) {
			try {
				isPause_Menu_Or_Back();// �ж���û�е���˵��򷵻ؼ������Ƿ���Ҫ��ͣ
				touch();// ����¼�����
				update();// ������
				renderRun();// ��ͼ����Ϸ����ͼƬ�����
				switch (mState) {
				case STATE_RUN:// ��ʽ��Ϸʱ���߳�����
					Thread.sleep(100);// ����100����
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/** �ж���û�е���˵��򷵻ؼ������Ƿ���Ҫ��ͣ */
	private void isPause_Menu_Or_Back() {
		// System.out.println("GameScene.isPause:"+GameScene.isPause);
		if (GameSceneActivity.isPause) {
			mState = STATE_PAUSE;
		}
	}

	/** ����ˢ���¼� */
	private void renderRun() {
		// ����ˢ���¼�
		Canvas canvas = null;
		try {
			canvas = getHolder().lockCanvas();// ��ȡ������������
			renderRun(canvas);// ��ͼ�¼���
		} catch (Exception e) {
		} finally {
			if (canvas != null)
				getHolder().unlockCanvasAndPost(canvas);// �����������
		}
	}

	private void renderRun(Canvas canvas) {
		drawBackGround(canvas);// ���Ʊ���ͼ������
		drawTime(canvas);// ����ʱ��
		drawStep(canvas);// ��ʾ���ߵĲ���
		drawOption(canvas);// �����֣���Ϸģʽ���Ѷȼ����û���
		drawSmall(canvas);// ��������ͼ
		drawBlock(canvas);// ����ƴͼͼƬ������Ԥ��ͼ
	}

	/** �����֣���Ϸģʽ���Ѷȼ����û��� */
	private void drawOption(Canvas canvas) {
		mPaint.reset();
		mPaint.setTextSize(20);
		mPaint.setColor(0xff800909);
		if (mMode == 0) {
			canvas.drawText("��Ϸģʽ ���Ե�ģʽ", 170, 80, mPaint);// �����������廭������
		} else {
			canvas.drawText("��Ϸģʽ ���ƶ�ģʽ", 170, 80, mPaint);// �����������廭������
		}
		canvas.drawText("�Ѷȼ��� ��" + mLevel + "��" + mLevel, 170, 100, mPaint);// �����������廭������
		drawButtons(canvas);// ����ť
	}

	/** �ط� */
	private void drawButtons(Canvas canvas) {
		canvas.drawBitmap(mClock, 170, 15, null);
		// ��ʼor��ͣ���ط�ʱ������
		if (mState == STATE_RUN || mState == STATE_READY) {
			canvas.drawBitmap(mPause, 170, 135, null);// ������ͣͼ
		} else
			canvas.drawBitmap(mPlay, 170, 135, null);// ���ƿ�ʼͼ
		// �浵
//		canvas.drawBitmap(mSave, 220, 135, null);// ���ƴ浵ͼ
		// �ػ�
		canvas.drawBitmap(mRedo, 270, 135, null);// ���Ƴ���ͼ
		// �ط�
		if (mState == STATE_REVIEW) {
			canvas.drawBitmap(mReView_1, 320, 135, null);// ���ƻط�ͼ
		} else
			canvas.drawBitmap(mReView_0, 320, 135, null);// ���ƻط�ͼ
		// ������ѡͼƬ
		canvas.drawBitmap(mBack_2_Select_Picture, 370, 135, null);// ���Ʒ�����ѡͼƬ��ťͼ
		// �˳���ǰ��Ϸ
		canvas.drawBitmap(mExit, 420, 135, null);// �����˳���ǰ��Ϸ��ťͼ
	}

	/** ��ʾ���ߵĲ��� */
	private void drawStep(Canvas canvas) {
		mPaint.reset();
		mPaint.setTextSize(30);
		mPaint.setColor(0xff800909);
		step = mBlock.getStep();// ȡ�õ�ǰ����
		if (stepForward != step) {// �жϵ�ǰ������ǰһ���Ƿ���ȣ����Ϊ��Ч�����û���ƶ�ͼƬ
			stepForward = step;// �ѵ�ǰ��������stepForward��������һ���ж�
			System.out.println("������" + step);// ����ȣ���ӡ
		}
		String reviewSteps = "";
		if (mState == STATE_REVIEW) {
			if (mReview_type == "exchange") {
				reviewSteps = " �طţ�" + mReviewSteps_Exchange / 2;
			} else if (mReview_type == "move") {
				reviewSteps = " �طţ�" + mReviewSteps;
			}
		}
		canvas.drawText("������" + step + reviewSteps, 170, 130, mPaint);// �����������廭������
	}

	/** ���Ʊ���ͼ */
	private void drawBackGround(Canvas canvas) {
		canvas.drawBitmap(mBackGround, 0, 0, null);// ���Ʊ���ͼ
	}

	/** ���ƿ�ͼ�����ڶԵ�ģʽ�±�ʶ��һ��ѡ�е�ͼƬ�飩 */
	private void drawRegion(Canvas canvas) {
		if (regionLeft != -1 && regionTop != -1) {// ���Ͻ�������Ч
			mPaint.reset();
			mPaint.setStrokeWidth(2);// �߿�
			mPaint.setColor(0xff00ff00);// ��ɫ
			mPaint.setStyle(Style.STROKE);// ���ķ����ʽ

			canvas.drawRect(regionLeft, regionTop, regionLeft + mBig.getWidth()
					/ mLevel, regionTop + mBig.getHeight() / mLevel, mPaint);
		}
	}

	/** ����ƴͼͼƬ����� */
	private void drawBlock(Canvas canvas) {
		switch (mState) {
		case STATE_READY:// ׼��
			drawReady(canvas);// ��׼��̬Block
			break;
		case STATE_PREVIEW:// Ԥ��
		case STATE_BEFORE_READY://׼��ǰ
			canvas.drawBitmap(mBig, 10, 190, null);// ����ԭͼ��Ԥ��
			break;
		case STATE_RUN:// ����
			mBlock.draw(canvas);// û����Ԥ��������ƴͼ����
			if (mMode == 0) {// �Ե�ģʽ�£�����
				drawRegion(canvas);
			}
			break;
		case STATE_PAUSE:// ��ͣ
			mBlock.draw(canvas);// û����Ԥ��������ƴͼ����
			canvas.drawBitmap(mPauseTip, 10, 190, null);// ������ͣ
			break;
		case STATE_WIN:// Ӯ
			canvas.drawBitmap(mBig, 10, 190, null);// ����ԭͼ��Ԥ��
			canvas.drawBitmap(mWin, (getWidth() - mWin.getWidth()) / 2,
					(getHeight() - 180 - mWin.getHeight()) / 2 + 180, null);// ����WIN
			break;

		case STATE_REVIEW://�ط�
			mBlock.draw(canvas);// ����ƴͼ����
			if (mReview_type == "exchange") {
				Review_Exchange(canvas);
			} else if (mReview_type == "move") {
				Review();
			}
			System.out.println("�ط�ģʽ��" + mReview_type);
			break;
		}
	}

	/** �Ե�ģʽ�ط��¼� */
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
				 mReviewSteps_Exchange++;// �طŲ�������1
				System.out.println(true + ":" + mReviewSteps_Exchange);

			} else {
				System.out.println("�ط����");
				mStart = System.currentTimeMillis();// ����ȡʱ�����
				isReview = false;// �طŽ���
				if (isWin) 
					mState = STATE_WIN;// �ط����״̬�л���ʤ��״̬
			    else
					mState = STATE_RUN;// �ط����״̬�л�������״̬

			}
		} 
	}
	/** �ƶ�ģʽ�ط��¼� */
	private void Review() {
		reviewTiming++;
		if (reviewTiming % 20 == 0) {

			if (mReviewSteps < mBlock.history.size()) {
				BlockGroup.Dir dir = mBlock.history.get(mReviewSteps);
				switch (dir) {// �׸����ƶ�����
				case LEFT:// ��
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow, mBlock.mWhiteCol - 1);
					mBlock.mWhiteCol -= 1;
					break;
				case TOP:// ��
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow - 1, mBlock.mWhiteCol);
					mBlock.mWhiteRow -= 1;
					break;
				case RIGHT:// ��
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow, mBlock.mWhiteCol + 1);
					mBlock.mWhiteCol += 1;
					break;
				case BOTTOM:// ��
					mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
							mBlock.mWhiteRow + 1, mBlock.mWhiteCol);
					mBlock.mWhiteRow += 1;
					break;
				}
				mReviewSteps++;// �طŲ�������1
			} else {
				System.out.println("�ط����");
				mStart = System.currentTimeMillis();// ����ȡʱ�����
				isReview = false;// �طŽ���
				if (isWin) 
					mState = STATE_WIN;// �ط����״̬�л���ʤ��״̬
			    else
					mState = STATE_RUN;// �ط����״̬�л�������״̬
			}
		}
	}

	/** ����Ready */
	private void drawReady(Canvas canvas) {
		readyTimeEnd = System.currentTimeMillis();// ׼����ֹʱ��
		readyNumber = 3 - (readyTimeEnd - readyTimeStart) / 1000;
		if (readyNumber >= 0) {
			bitmapReady = BitmapFactory.decodeResource(getResources(),
					readyNumberView[(int) readyNumber]);// ԭͼ
			canvas.drawBitmap(mBig, 10, 190, null);// ����ԭͼ
			canvas.drawBitmap(bitmapReady, 10, 190, null);// ���ƿ�ʼ��Ϸ������
		} else {
			mState = STATE_RUN;// ����׼����ɣ��޸�״̬Ϊ��Ϸ״̬
			mStart = System.currentTimeMillis();// ȡ��Ϸ��ʽ��ʼʱ��㣬��ȡ��ǰ����������ʼʱ��1970��1��1��
			isReady = true;// ׼������ˣ�������Ϸ��
		}
	}

	/** ��������ͼ */
	private void drawSmall(Canvas canvas) {
		canvas.drawBitmap(mSmall, 10, 10, null);
		if (mState == STATE_PREVIEW) {
			drawPreviewTime(canvas);// Ԥ����ʱ
			Bitmap previewTimePicture = BitmapFactory.decodeResource(
					getResources(), previewNumberView[(int) previewTime]);// ԭͼ
			canvas.drawBitmap(previewTimePicture, 10, 10, null);// ����Ԥ��������
		}
	}

	/** Ԥ����ʱ */
	private void drawPreviewTime(Canvas canvas) {
		if (mState == STATE_PREVIEW) {
			previewTimeEnd = System.currentTimeMillis();// ��ȡԤ����ֹʱ��
			previewTime = 5 - (previewTimeEnd - previewTimeStart) / 1000;// ʣ��Ԥ��ʱ��
			if (previewTime < 0 )// Ԥ��ʱ���þ����ж��Ƿ�WIN����Ԥ��,���Ƿ���RUN
				mState = STATE_RUN;// Ԥ����ɸ���״̬Ϊ��Ϸ״̬
		}
	}

	/** ����ʱ�� */
	private void drawTime(Canvas canvas) {
		// DisplayMetrics metrics = new DisplayMetrics();
		// Display display = mManager.getDefaultDisplay();
		// display.getMetrics(metrics);
		// float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
		// metrics);
		mPaint.reset();
		mPaint.setTextSize(50);
		mPaint.setColor(0xfffba208);
		if (isReady) {// ׼����֮����ʾʵ����Ϸʱ��
			switch (mState) {

			case STATE_PREVIEW:
			case STATE_RUN:
				long mEnd = System.currentTimeMillis();// ��ȡ��ֹʱ��
				time = (mEnd - mStart) / 1000 + theTimeBeforeReView;// ʱ���룩+
																	// Ԥ��ǰʱ��
				long second = time % 60;// ��
				long minute = (time / 60) % 60;// ��
				long hour = time / 60 / 60;// ʱ
				timeStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);// ����ʱ���ַ���
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

	/** ����¼� */
	private void touch() {
		// ����¼�
		if (mEvent != null) {
			int x = (int) mEvent.getX();
			int y = (int) mEvent.getY();
			System.out.println(x + ":" + y);
			if (mPlayRect.contains(x, y)) {// ��ʼ��Ϸ��ť�������
				if (isReady) {// ׼������,����ûӮ
					if (thePlayButtonClickTimes++ % 2 == 0) {
						mState = STATE_PAUSE;// play̬����ͣ
						theTimeBeforeReView = time;// ��������ʱ��
						System.out.println("��ͣ��");
					} else {
						mState = STATE_READY;// pause̬��RUN
						System.out.println("��������");
						isPuaseToReady = true;
						GS.set_IsPause(false);// ȡ����Ϸ������ͣ��־
						if (mBlock.isWin()) {// ��ͣ�����ж��ǲ���Ӯ��
							System.out.println("��ϲ������ɹ��ˣ�");
							mState = STATE_WIN;// ʤ����־
//							isWin = true;
						}
						readyTimeStart = System.currentTimeMillis();// Ԥ����ʼʱ��
					}
				} else {// û��׼���ÿ�ʼ׼��
					mState = STATE_READY;// ״̬��ΪReady
					readyTimeStart = System.currentTimeMillis();// Ԥ����ʼʱ��
					GS.set_IsPause(false);// ȡ����Ϸ������ͣ��־
					System.out.println("��ʼ��Ϸ");
				}
			}

			if (mExitRect.contains(x, y)) {// �˳���ǰ��Ϸ��ť�����
				System.out.println("�˳���ǰ��Ϸ");
				post(new Runnable() {// ���߳������̷߳��Ͳ�������

					@Override
					public void run() {
						mGoToOtherActivity.goToOtherActivity(0);
					}
				});

			}
			if (mBack_2_Select_PictureRect.contains(x, y)) {// ������ѡͼƬ��ť�����
				System.out.println("������ѡͼƬ");
				post(new Runnable() {// ���߳������̷߳��Ͳ�������

					@Override
					public void run() {
						mGoToOtherActivity.goToOtherActivity(1);
					}
				});
			}
			if (isReady && mState != STATE_PAUSE && mState != STATE_REVIEW
					&& mState != STATE_PREVIEW) {
				// ׼����֮��(������Ԥ�����طš���ͣ���Ѿ�Winʱ)���������¼�

				if (mSaveRect.contains(x, y)) {// �浵�����Χ��
					System.out.println("�浵������");
				} else if (mSmallRect.contains(x, y)  && mState != STATE_WIN) {// ����ͼ�����Χ��
					System.out.println("Ԥ��");
					doPreview();
				} else if (mReViewRect.contains(x, y) && !isReview) {// �ط�,���ڻط�ʱ���������طŰ�ť
					System.out.println("�ط�");
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
						&& mState != STATE_WIN) {// ��������һ�Σ���һ��
					System.out.println("����");
					switch (mMode) {
					case 0:
						doRedo_Exchange();
						break;
					case 1:
					default:
						doRedo();
						break;
					}
				} else if (mBlockRect.contains(x, y) && mState != STATE_WIN) {// �ƶ����ӣ����ж��Ƿ�win
					System.out.println("ѡ���ƶ�ͼƬ�飬���ж��Ƿ�win");
					switch (mMode) {
					case 0:
					case 1:
					default:
						if (mBlock.onClick(x, y)) {
							// Toast.makeText(getContext(), "��ϲ������ɹ��ˣ�",
							// Toast.LENGTH_LONG);
							System.out.println("��ϲ������ɹ��ˣ�");
							mState = STATE_WIN;// ʤ����־
							isWin = true;
							if (mOnWinListener != null) {// ��������Ϊ��
								post(new Runnable() {// ���߳������̷߳���winDialogue��ʾ����

									@Override
									public void run() {

										// ����ʱ��,����������ͳ���ڴ�֮�󣬹��ڴ��ж�WIN֮��ʵ�ʲ���Ӧ��1��
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
	 * ʤ����������
	 * <p>
	 * */
	public void setOnWinListener(OnWinListener winListener) {
		mOnWinListener = winListener;
	}

	/** ʤ�������ӿ� */
	interface OnWinListener {
		public void onWin(String mode, String level, int time, int steps);
	}

	/**
	 * ҳ����ת��������
	 * <p>
	 * */
	public void GoToOtherActivityListener(
			GoToOtherActivityListener goToOtherActivity) {
		mGoToOtherActivity = goToOtherActivity;
	}

	/** ҳ����ת�����ӿ� */
	interface GoToOtherActivityListener {
		public void goToOtherActivity(int index);// index��0����ҳ�棬1������ѡͼ
	}

	/** ������һ���� */
	private void doRedo() {
		if (step > 0) {
			BlockGroup.Dir dir = mBlock.history.getLast();// ȡ���һ�η��ö���
			switch (dir) {// �׸����ƶ�����
			case LEFT:// ��¼�����һ���ǰ׸��������ƶ�����ô����Ӧ�����ң������෴����ͬ
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow, mBlock.mWhiteCol + 1);
				mBlock.mWhiteCol += 1;
				break;
			case TOP:// ��
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow + 1, mBlock.mWhiteCol);
				mBlock.mWhiteRow += 1;
				break;
			case RIGHT:// ��
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow, mBlock.mWhiteCol - 1);
				mBlock.mWhiteCol -= 1;
				break;
			case BOTTOM:// ��
				mBlock.swapBlock(mBlock.mWhiteRow, mBlock.mWhiteCol,
						mBlock.mWhiteRow - 1, mBlock.mWhiteCol);
				mBlock.mWhiteRow -= 1;
				break;
			}
			mBlock.history.removeLast();// �Ƴ����һ��
			mBlock.setStep(step - 1);// ȡ�õ�ǰ����
			System.out.println("����һ��");
			mState = STATE_RUN;// �ط����״̬�л�������״̬
		}
	}
	/** ������һ���� */
	private void doRedo_Exchange() {
		isRedo=true;
		if (step > 0) {
			for (int i = 0; i < 2; i++) {
				int mRedo_Exchange = mBlock.history_Exchange.getLast();// ȡ���һ�η��ö���
				mBlock.history_Exchange.removeLast();// �Ƴ����һ��
				
				int row = mRedo_Exchange / mLevel;
				int col = mRedo_Exchange % mLevel;
				
				mBlock.swapBlock(row, col);
			
				System.out.println(true + ":" + mRedo_Exchange);
			}

			mBlock.setStep(step - 1);// ȡ�õ�ǰ����
			System.out.println("����һ��");
			mState = STATE_RUN;// �ط����״̬�л�������״̬
			isRedo=false;
		}
	}

	/** �طų��� */
	private void doReview() {
		mState = STATE_REVIEW;// ״̬��Ϊ�ط�
		theTimeBeforeReView = time;
		mReviewSteps = 0;// �طŲ�������Ϊ0
		mBlock.getSet(mMap);// ���ش��Һ�����Block���лط�
		mBlock.mWhiteRow = mBlock.mWhiteRowSave;
		mBlock.mWhiteCol = mBlock.mWhiteColSave;
		isReview = true;// �Ƿ�ط�
		mReview_type = "move";
	}

	/** �Ե�ģʽ�طų��� */
	private void doReview_Exchange() {
		mState = STATE_REVIEW;// ״̬��Ϊ�ط�
		theTimeBeforeReView = time;
		mReviewSteps_Exchange = 0;// �طŲ�������Ϊ0
		mBlock.getSet(mMap);// ���ش��Һ�����Block���лط�
		mBlock.idCache=-1;
		isReview = true;// �Ƿ�ط�
		mReview_type = "exchange";
	}

	/** Ԥ������ */
	private void doPreview() {
		mState = STATE_PREVIEW;// ״̬����ΪԤ��
		previewTimeStart = System.currentTimeMillis();// Ԥ����ʼʱ��
		previewTime = 5;// ����5��Ԥ��
	}

	/** ׼�����򣬻�ȡ������ͼ */
	private void update() {
		switch (mState) {
		case STATE_READY:
			if (!isPuaseToReady) {
				// �ж��ǲ��Ǵ�pauseת��ready�����ǵĻ���false����Ҫ���ң��ǵĻ���true������Ҫ�������ԭ���ľ���
				for (; rStep < READY_STEP; rStep++) {
					mBlock.upset();
				}
				mMap = mBlock.getMap();// ��ȡ�����ͼ
				mBlock.setReadyStep(READY_STEP);
			}
			break;
		default:
			break;
		}
	}

}
