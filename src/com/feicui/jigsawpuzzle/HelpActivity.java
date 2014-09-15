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


	private String[] mTitles={"游戏内容简介：","对调模式","移动模式","游戏界面功能介绍","样图放大功能","背景音乐设置","排名规则"};
	private String[] mDialogue={"\n　　为了更好的为广大玩家服务，特此推出了\"拼图\"这款老少男女皆宜的休闲益智游戏。\n　　该拼图游戏分为对调、移动两种游戏模式。每种模式中又根据切分的块数分为不同的难度。",
			"\n　　此模式难度分为5x5、6x6、7x7、8x8、9x9、10x10 六种。\n　　根据缩略图的提示，在拼图面板中，将您想移动的小图块移至你认为所选小图块原来的位置，点击欲移动的图块即可实现对调。重复以上过程，直至拼出原图。",
			"\n　　此模式难度分为3x3、4x4、5x5、6x6 四种。\n　　根据缩略图的提示，在拼图面板中，点击空闲图块附近的小图块，该小图块将移动到空闲图块的位置。重复以上过程，直至拼出原图。",
			"\n　　\"对调模式\"和\"移动模式\"游戏界面功能类似。\n　　界面左上角为\"样图\"，可作为拼图提示。\n　　进入游戏右上角的\"控制面板中\"会显示当前游戏模式和游戏级别，游戏计时、计步信息。\n　　控制面板底部是一排功能按钮：开始（暂停）游戏，存档（暂未实现），撤销（一步），回放（从头回放玩家的走步，同时会显示回放步数），返回重新选图（模式不变），退出当前游戏",
			"\n　　点击\"样图\"可以出现放大的样图。放大样图与原图大小相同，显示时间为5秒钟。",
			"\n　　目前只为游戏界面添加了背景音乐，共7首（随机播放）：Annies Wonderland，Just a little smile bandari，Snow Dream ，Write to the ocean，One day in spring（春日），初雪，童年。\n　　设置音乐播放与否、音量大小均可在\"游戏设置\"界面操作，如正在游戏中，也可直接点击菜单键[MENU]跳转到设置界面设置，此时游戏将处于暂停状态，再按返回键返回游戏中，须点击开始游戏按钮继续游戏。",
			"\n    可以自由拼图无时间限制，如果成绩优秀，将进入前10名排行榜。\n　　注：排行同样分为对调模式和移动模式，并对应各自的难度排行"};
	ListView listView;
	boolean bView[]={false,false,false,false,false,false,false};
	MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();//全屏
		setContentView(R.layout.help);
		myAdapter=new MyAdapter(this);
		System.out.println(mTitles.length+","+mDialogue.length);
		listView=(ListView) findViewById(R.id.listView_help);
		listView.setAdapter(myAdapter);
		
//		listView的触控事件
		listView.setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {//arg2:下标
						// TODO Auto-generated method stub
						bView[arg2]=!bView[arg2];
						myAdapter.upData();
					}
				});
	}
	
	/**
	 * 全屏
	 * */
	public void setFullScreen(){
		//去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}

	public class MyAdapter extends BaseAdapter{ 
		Context mcontext;
		public MyAdapter(Context context){
			mcontext=context;
		}
		/**ListView里有多少个元素*/
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTitles.length;//返回mTitles长度
		}
		/**ListView里的元素内容*/
		@Override
		public Object getItem(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}
		/**ListView里的元素ID（下标，从0开始）*/
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
//			更新适配器（Adapter）的数据
			notifyDataSetChanged();
		}
	}
	
//	自定义布局内部类


	public class MyLayout extends LinearLayout{

		TextView bigTitles,endText;
		TextView textView1,textView2;
		ImageView imageView;
		public MyLayout(Context context,int index) {
			super(context);
			// TODO Auto-generated constructor stub
			setOrientation(VERTICAL);//设置垂直布局

			
			textView1=new TextView(context);//textView1实例化
			textView1.setTextColor(Color.BLACK);//设置字体颜色
			textView1.setTextSize(20);//设置字体大小
			textView1.setText(mTitles[index]);//设置内容
//			将textView1加载到LinearLayout布局里设置的显示的大小范围
//			addView(textView1, new LinearLayout.LayoutParams(width, height));
			addView(textView1, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, //满屏
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));//实际大小
			
			textView2=new TextView(context);//textView2实例化
			textView2.setTextColor(Color.GREEN);//设置字体颜色
			textView2.setTextSize(15);//设置字体大小
//			textView2.setText(mDialogue[index]);//设置内容
			addView(textView2, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, //满屏
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));//实际大小
			
			textView2.setVisibility(VISIBLE);//设置是否显示，默认显示
//			textView2.setVisibility(GONE);//设置是关闭
			
			
			
			imageView=new ImageView(context);//textView2实例化
			imageView.setImageResource(R.drawable.tiphelp);//设置内容
			addView(imageView, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, //实际大小
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));//实际大小
			
			imageView.setVisibility(VISIBLE);//设置是否显示，默认显示
		}

		/**设置标题*/
		public void setTitle(String str){
			textView1.setText(str);//设置内容
			textView1.setVisibility(VISIBLE);//设置是否显示，默认显示
		}
		/**设置信息*/
		public void setDialogue(String str){
			textView2.setText(str);//设置内容
		}
		/**设置是否显示*/
		public void setVisibility(boolean bool){
			textView2.setVisibility(bool?VISIBLE:GONE);//设置是否显示，默认显示
		}
	}
}
