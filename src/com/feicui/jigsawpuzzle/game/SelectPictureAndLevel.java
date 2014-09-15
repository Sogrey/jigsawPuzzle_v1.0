package com.feicui.jigsawpuzzle.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.jigsawpuzzle.R;


@SuppressLint("ResourceAsColor")
public class SelectPictureAndLevel extends Activity {
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
			R.drawable.pic30, R.drawable.pic31, R.drawable.pic32};
	// String[] images=null;
	// ArrayList<String> imageList=new ArrayList<String>();

	CharSequence[] items_Mode_Move = { "3x3", "4x4", "5x5", "6x6" };// 移动模式下难度列表
	CharSequence[] items_Mode_Exchange = { "5x5", "6x6", "7x7", "8x8", "9x9",
			"10x10" };// 对调模式下难度列表

	SelectModeActivity mPlayList = new SelectModeActivity();// PlayListActivity对象
	public int mode;// 游戏模式
	public int level;// 难度等级
	public int imageId;// 已选择图片ID

	Gallery gallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(com.feicui.jigsawpuzzle.R.layout.select_picture_and_level);

		// Intent intent=getIntent();//获取启动该Activity的Intent
		// String modeStr=intent.getStringExtra("mode");//取得该Intent携带的参数
		// System.out.println("选择的游戏模式"+modeStr);
		// mode=Integer.parseInt(modeStr);
		// System.out.println("选择的游戏模式"+mode);
		mode = getIntent().getIntExtra("mode", 0);// 取得参数
		TextView textViewSelcetTip=(TextView) findViewById(R.id.textView_select_tip);
		TextView textViewMode=(TextView) findViewById(R.id.textView_mode);
		switch (mode) {
		case 0:
			textViewMode.setText("对调模式");
			break;
		case 1:
		default:
			textViewMode.setText("移动模式");
			break;
		}
		textViewMode.setTextSize(30);
		textViewSelcetTip.setText("请选择图片");
//		textViewSelcetTip.setTextColor(R.color.white);
		
		gallery =(Gallery)this.findViewById(R.id.gallery);
		gallery.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View viewContext, ViewGroup arg2) {

				if (viewContext == null) {
					viewContext = getLayoutInflater().inflate(
							com.feicui.jigsawpuzzle.R.layout.gallery, null);
				}
				ImageView img = (ImageView) viewContext
						.findViewById(R.id.gallery_imageView1);
				TextView tv = (TextView) viewContext
						.findViewById(R.id.gallery_textView1);
				tv.setText("" + (position + 1) + "/" + imageView.length);
				tv.setTextSize(24);
				img.setImageResource(imageView[position % imageView.length]);
				return viewContext;
			}

			@Override
			public long getItemId(int id) {
				return id;
			}

			@Override
			public Object getItem(int position) {
				return imageView[position];
			}

			@Override
			public int getCount() {
				return imageView.length;
			}
		});
		/** gallery元素点击处理事件 */
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewContext,
					final int position, long id) {
				imageId = position;

				System.out.println("传递过来的mode：" + mode);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SelectPictureAndLevel.this);

				switch (mode) {
				case 0:// 对调模式
				default:
					builder.setTitle("请选择对调模式下难度级别：");
					builder.setIcon(R.drawable.tip);

					builder.setSingleChoiceItems(items_Mode_Exchange, 0,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int position) {
									Toast.makeText(
											getApplicationContext(),
											position
													+ "->"
													+ items_Mode_Exchange[position],
											Toast.LENGTH_SHORT).show();
									level = position;
									System.out.println("难度标识：" + level);

								}
							});
					break;
				case 1:// 移动模式
					builder.setTitle("请选择移动模式下难度级别：");
					builder.setIcon(R.drawable.tip);
					System.out.println("GetModeListOfMove");

					builder.setSingleChoiceItems(items_Mode_Move, 0,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int position) {
									Toast.makeText(
											getApplicationContext(),
											position + "->"
													+ items_Mode_Move[position],
											Toast.LENGTH_SHORT).show();
									level = position;
									System.out.println("难度标识：" + level);
								}
							});
					break;
				}

				builder.setNegativeButton("取消", null);// 右边第一个按钮，返回取消键
				// // builder.setNeutralButton("中间按钮", null);//中间按钮
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// 显示对话框返回值
								switch (mode) {
								case 0:// 对调模式
								default:
									System.out.println("你选择了【对调模式】");
									System.out.println("难度：" + level + "->"
											+ items_Mode_Exchange[level]);
									// 传递游戏参数：游戏模式，难度等级，选择的图片
									// 跳转到对调模式游戏界面
									gotoGameScene();
									break;
								case 1:// 移动模式
									System.out.println("你选择了【移动模式】");
									System.out.println("难度：" + level + "->"
											+ items_Mode_Move[level]);
									// 传递游戏参数：游戏模式，难度等级，选择的图片
									// 跳转到移动模式游戏界面
									gotoGameScene();
									break;
								}
							}
						});// 左边第一个按钮，确定键
				builder.show();
			}
		});
		/** gallery元素选择处理事件，更改gallery背景色 */
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position % 5) {
				case 0:
					parent.setBackgroundColor(Color.BLACK);
					break;
				case 1:
					parent.setBackgroundColor(Color.YELLOW);
					break;
				case 2:
					parent.setBackgroundColor(Color.GREEN);
					break;
				case 3:
					parent.setBackgroundColor(Color.RED);
					break;
				case 4:
					parent.setBackgroundColor(Color.BLUE);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/** 跳转到游戏场景 */
	private void gotoGameScene() {
		System.out.println(imageId);
		Intent intent = new Intent(SelectPictureAndLevel.this, GameSceneActivity.class);

		intent.putExtra("mode", mode);// 传参，整形int。参数：key：标签，Value：值
		intent.putExtra("level", level);// 传参，整形int。参数：key：标签，Value：值
		intent.putExtra("imageId", imageId);// 传参，整形int。参数：key：标签，Value：值

		startActivity(intent);
		finish();
	}
	/**点击返回键，返回到PlayListActivity*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK){
			System.out.println("你按下了返回键");
			Intent intent=new Intent(SelectPictureAndLevel.this,SelectModeActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setFullScreen() {
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
