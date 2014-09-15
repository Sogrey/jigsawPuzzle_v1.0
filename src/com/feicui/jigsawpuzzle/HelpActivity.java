package com.feicui.jigsawpuzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HelpActivity extends Activity{


	private String[] mTitles={"��Ϸ���ݼ�飺","�Ե�ģʽ","�ƶ�ģʽ","��Ϸ���湦�ܽ���","��ͼ�Ŵ���","������������","��������"};
	private String[] mDialogue={"\n����Ϊ�˸��õ�Ϊ�����ҷ����ش��Ƴ���\"ƴͼ\"���������Ů���˵�����������Ϸ��\n������ƴͼ��Ϸ��Ϊ�Ե����ƶ�������Ϸģʽ��ÿ��ģʽ���ָ����зֵĿ�����Ϊ��ͬ���Ѷȡ�",
			"\n������ģʽ�Ѷȷ�Ϊ5x5��6x6��7x7��8x8��9x9��10x10 ���֡�\n������������ͼ����ʾ����ƴͼ����У��������ƶ���Сͼ����������Ϊ��ѡСͼ��ԭ����λ�ã�������ƶ���ͼ�鼴��ʵ�ֶԵ����ظ����Ϲ��̣�ֱ��ƴ��ԭͼ��",
			"\n������ģʽ�Ѷȷ�Ϊ3x3��4x4��5x5��6x6 ���֡�\n������������ͼ����ʾ����ƴͼ����У��������ͼ�鸽����Сͼ�飬��Сͼ�齫�ƶ�������ͼ���λ�á��ظ����Ϲ��̣�ֱ��ƴ��ԭͼ��",
			"\n����\"�Ե�ģʽ\"��\"�ƶ�ģʽ\"��Ϸ���湦�����ơ�\n�����������Ͻ�Ϊ\"��ͼ\"������Ϊƴͼ��ʾ��\n����������Ϸ���Ͻǵ�\"���������\"����ʾ��ǰ��Ϸģʽ����Ϸ������Ϸ��ʱ���Ʋ���Ϣ��\n�����������ײ���һ�Ź��ܰ�ť����ʼ����ͣ����Ϸ���浵����δʵ�֣���������һ�������طţ���ͷ�ط���ҵ��߲���ͬʱ����ʾ�طŲ���������������ѡͼ��ģʽ���䣩���˳���ǰ��Ϸ",
			"\n�������\"��ͼ\"���Գ��ַŴ����ͼ���Ŵ���ͼ��ԭͼ��С��ͬ����ʾʱ��Ϊ5���ӡ�",
			"\n����ĿǰֻΪ��Ϸ��������˱������֣���7�ף�������ţ���Annies Wonderland��Just a little smile bandari��Snow Dream ��Write to the ocean��One day in spring�����գ�����ѩ��ͯ�ꡣ\n�����������ֲ������������С������\"��Ϸ����\"�����������������Ϸ�У�Ҳ��ֱ�ӵ���˵���[MENU]��ת�����ý������ã���ʱ��Ϸ��������ͣ״̬���ٰ����ؼ�������Ϸ�У�������ʼ��Ϸ��ť������Ϸ��",
			"\n    ��������ƴͼ��ʱ�����ƣ�����ɼ����㣬������ǰ10�����а�\n����ע������ͬ����Ϊ�Ե�ģʽ���ƶ�ģʽ������Ӧ���Ե��Ѷ�����"};
	ListView listView;
	boolean bView[]={false,false,false,false,false,false,false};
	MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();//ȫ��
		setContentView(R.layout.help);
		myAdapter=new MyAdapter(this);
		System.out.println(mTitles.length+","+mDialogue.length);
		listView=(ListView) findViewById(R.id.listView_help);
		listView.setAdapter(myAdapter);
		
//		listView�Ĵ����¼�
		listView.setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {//arg2:�±�
						// TODO Auto-generated method stub
						bView[arg2]=!bView[arg2];
						myAdapter.upData();
					}
				});
	}
	
	/**
	 * ȫ��
	 * */
	public void setFullScreen(){
		//ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}

	public class MyAdapter extends BaseAdapter{ 
		Context mcontext;
		public MyAdapter(Context context){
			mcontext=context;
		}
		/**ListView���ж��ٸ�Ԫ��*/
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTitles.length;//����mTitles����
		}
		/**ListView���Ԫ������*/
		@Override
		public Object getItem(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}
		/**ListView���Ԫ��ID���±꣬��0��ʼ��*/
		@Override
		public long getItemId(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}

		@Override
		public View getView(int postion, View converView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MyLayout myLayout;
			if (converView==null) {
				myLayout=new MyLayout(mcontext,postion);
				System.out.println(postion);
			}else{
				myLayout=(MyLayout) converView;
				
				myLayout.setTitle(mTitles[postion]);
				myLayout.setDialogue(mDialogue[postion]);
				myLayout.setVisibility(bView[postion]);
				System.out.println(postion);
			}
			return myLayout;
		}
		
		public void upData(){
//			������������Adapter��������
			notifyDataSetChanged();
		}
	}
	
//	�Զ��岼���ڲ���


	public class MyLayout extends LinearLayout{

		TextView bigTitles,endText;
		TextView textView1,textView2;
		ImageView imageView;
		public MyLayout(Context context,int index) {
			super(context);
			// TODO Auto-generated constructor stub
			setOrientation(VERTICAL);//���ô�ֱ����

			
			textView1=new TextView(context);//textView1ʵ����
			textView1.setTextColor(Color.BLACK);//����������ɫ
			textView1.setTextSize(20);//���������С
			textView1.setText(mTitles[index]);//��������
//			��textView1���ص�LinearLayout���������õ���ʾ�Ĵ�С��Χ
//			addView(textView1, new LinearLayout.LayoutParams(width, height));
			addView(textView1, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, //����
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));//ʵ�ʴ�С
			
			textView2=new TextView(context);//textView2ʵ����
			textView2.setTextColor(Color.GREEN);//����������ɫ
			textView2.setTextSize(15);//���������С
//			textView2.setText(mDialogue[index]);//��������
			addView(textView2, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, //����
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));//ʵ�ʴ�С
			
			textView2.setVisibility(VISIBLE);//�����Ƿ���ʾ��Ĭ����ʾ
//			textView2.setVisibility(GONE);//�����ǹر�
			
			
			
			imageView=new ImageView(context);//textView2ʵ����
			imageView.setImageResource(R.drawable.tiphelp);//��������
			addView(imageView, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, //ʵ�ʴ�С
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));//ʵ�ʴ�С
			
			imageView.setVisibility(VISIBLE);//�����Ƿ���ʾ��Ĭ����ʾ
		}

		/**���ñ���*/
		public void setTitle(String str){
			textView1.setText(str);//��������
			textView1.setVisibility(VISIBLE);//�����Ƿ���ʾ��Ĭ����ʾ
		}
		/**������Ϣ*/
		public void setDialogue(String str){
			textView2.setText(str);//��������
		}
		/**�����Ƿ���ʾ*/
		public void setVisibility(boolean bool){
			textView2.setVisibility(bool?VISIBLE:GONE);//�����Ƿ���ʾ��Ĭ����ʾ
		}
	}
}
