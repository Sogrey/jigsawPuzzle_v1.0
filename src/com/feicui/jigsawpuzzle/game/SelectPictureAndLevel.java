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

	CharSequence[] items_Mode_Move = { "3x3", "4x4", "5x5", "6x6" };// �ƶ�ģʽ���Ѷ��б�
	CharSequence[] items_Mode_Exchange = { "5x5", "6x6", "7x7", "8x8", "9x9",
			"10x10" };// �Ե�ģʽ���Ѷ��б�

	SelectModeActivity mPlayList = new SelectModeActivity();// PlayListActivity����
	public int mode;// ��Ϸģʽ
	public int level;// �Ѷȵȼ�
	public int imageId;// ��ѡ��ͼƬID

	Gallery gallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(com.feicui.jigsawpuzzle.R.layout.select_picture_and_level);

		// Intent intent=getIntent();//��ȡ������Activity��Intent
		// String modeStr=intent.getStringExtra("mode");//ȡ�ø�IntentЯ���Ĳ���
		// System.out.println("ѡ�����Ϸģʽ"+modeStr);
		// mode=Integer.parseInt(modeStr);
		// System.out.println("ѡ�����Ϸģʽ"+mode);
		mode = getIntent().getIntExtra("mode", 0);// ȡ�ò���
		TextView textViewSelcetTip=(TextView) findViewById(R.id.textView_select_tip);
		TextView textViewMode=(TextView) findViewById(R.id.textView_mode);
		switch (mode) {
		case 0:
			textViewMode.setText("�Ե�ģʽ");
			break;
		case 1:
		default:
			textViewMode.setText("�ƶ�ģʽ");
			break;
		}
		textViewMode.setTextSize(30);
		textViewSelcetTip.setText("��ѡ��ͼƬ");
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
		/** galleryԪ�ص�������¼� */
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewContext,
					final int position, long id) {
				imageId = position;

				System.out.println("���ݹ�����mode��" + mode);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SelectPictureAndLevel.this);

				switch (mode) {
				case 0:// �Ե�ģʽ
				default:
					builder.setTitle("��ѡ��Ե�ģʽ���Ѷȼ���");
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
									System.out.println("�Ѷȱ�ʶ��" + level);

								}
							});
					break;
				case 1:// �ƶ�ģʽ
					builder.setTitle("��ѡ���ƶ�ģʽ���Ѷȼ���");
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
									System.out.println("�Ѷȱ�ʶ��" + level);
								}
							});
					break;
				}

				builder.setNegativeButton("ȡ��", null);// �ұߵ�һ����ť������ȡ����
				// // builder.setNeutralButton("�м䰴ť", null);//�м䰴ť
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// ��ʾ�Ի��򷵻�ֵ
								switch (mode) {
								case 0:// �Ե�ģʽ
								default:
									System.out.println("��ѡ���ˡ��Ե�ģʽ��");
									System.out.println("�Ѷȣ�" + level + "->"
											+ items_Mode_Exchange[level]);
									// ������Ϸ��������Ϸģʽ���Ѷȵȼ���ѡ���ͼƬ
									// ��ת���Ե�ģʽ��Ϸ����
									gotoGameScene();
									break;
								case 1:// �ƶ�ģʽ
									System.out.println("��ѡ���ˡ��ƶ�ģʽ��");
									System.out.println("�Ѷȣ�" + level + "->"
											+ items_Mode_Move[level]);
									// ������Ϸ��������Ϸģʽ���Ѷȵȼ���ѡ���ͼƬ
									// ��ת���ƶ�ģʽ��Ϸ����
									gotoGameScene();
									break;
								}
							}
						});// ��ߵ�һ����ť��ȷ����
				builder.show();
			}
		});
		/** galleryԪ��ѡ�����¼�������gallery����ɫ */
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

	/** ��ת����Ϸ���� */
	private void gotoGameScene() {
		System.out.println(imageId);
		Intent intent = new Intent(SelectPictureAndLevel.this, GameSceneActivity.class);

		intent.putExtra("mode", mode);// ���Σ�����int��������key����ǩ��Value��ֵ
		intent.putExtra("level", level);// ���Σ�����int��������key����ǩ��Value��ֵ
		intent.putExtra("imageId", imageId);// ���Σ�����int��������key����ǩ��Value��ֵ

		startActivity(intent);
		finish();
	}
	/**������ؼ������ص�PlayListActivity*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK){
			System.out.println("�㰴���˷��ؼ�");
			Intent intent=new Intent(SelectPictureAndLevel.this,SelectModeActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setFullScreen() {
		// ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
