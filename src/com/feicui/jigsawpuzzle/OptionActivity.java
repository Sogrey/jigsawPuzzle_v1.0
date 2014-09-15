package com.feicui.jigsawpuzzle;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OptionActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	private Button mButton_Volume_Switch;// 音量开关按钮
	private Button mButton_Add, mButton_Min;// 音量加减控制按钮
	private SeekBar mSeekBar;// 音量显示控制进度条
	private AudioManager mAudioManager;// 声明音频管理对象
	public static boolean isPlay = true;// 是否播放音乐:true:播放；false：不放

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// 全屏
		setContentView(R.layout.option);// 加载的一个xml文件：about,关于
		mButton_Add = (Button) findViewById(R.id.option_volume_add);
		mButton_Add.setOnClickListener(this);
		mButton_Min = (Button) findViewById(R.id.option_volume_min);
		mButton_Min.setOnClickListener(this);
		mButton_Volume_Switch = (Button) findViewById(R.id.option_volume_switch);
		mButton_Volume_Switch.setOnClickListener(this);
		mSeekBar = (SeekBar) findViewById(R.id.option_volume_seekBar);
		mSeekBar.setOnSeekBarChangeListener(this);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);// Manager多用此方法得到，一项系统服务
		mSeekBar.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));// 获取多媒体最大音量
		mSeekBar.setProgress(mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));// 设置进度条当前音量
	}

	/**
	 * 全屏
	 * */
	public void setFullScreen() {
		// 去标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去信息栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.option_volume_add:// 音量加
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,// 需要改变音量的源（多媒体）
					AudioManager.ADJUST_RAISE,// 增加音量
					AudioManager.FLAG_PLAY_SOUND);// 调整时播放预览音

			break;
		case R.id.option_volume_min:// 音量减
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,// 需要改变音量的源（多媒体）
					AudioManager.ADJUST_LOWER,// 增加音量
					AudioManager.FLAG_PLAY_SOUND);// 调整时播放预览音

			break;

		case R.id.option_volume_switch:// 音量开关
			if (isPlay) {
				isPlay = false;
				mButton_Volume_Switch.setText("OFF");
				mButton_Add.setEnabled(false);
				mButton_Min.setEnabled(false);
				mSeekBar.setEnabled(false);
			} else {
				isPlay = true;
				mButton_Volume_Switch.setText("ON");
				mButton_Add.setEnabled(true);
				mButton_Min.setEnabled(true);
				mSeekBar.setEnabled(true);
			}
			System.out.println("isPlaying = " + isPlay);
			break;
		}
		mSeekBar.setProgress(mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));// 设置进度条当前音量

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) // 用户拖动进度条的时候才执行下面音量调整操作
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, // 需要改变的音量源（多媒体）
					progress, // 当前设置的进度（大小）
					AudioManager.FLAG_PLAY_SOUND);// 调整时播放预览音
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
