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

	private Button mButton_Volume_Switch;// �������ذ�ť
	private Button mButton_Add, mButton_Min;// �����Ӽ����ư�ť
	private SeekBar mSeekBar;// ������ʾ���ƽ�����
	private AudioManager mAudioManager;// ������Ƶ�������
	public static boolean isPlay = true;// �Ƿ񲥷�����:true:���ţ�false������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen();// ȫ��
		setContentView(R.layout.option);// ���ص�һ��xml�ļ���about,����
		mButton_Add = (Button) findViewById(R.id.option_volume_add);
		mButton_Add.setOnClickListener(this);
		mButton_Min = (Button) findViewById(R.id.option_volume_min);
		mButton_Min.setOnClickListener(this);
		mButton_Volume_Switch = (Button) findViewById(R.id.option_volume_switch);
		mButton_Volume_Switch.setOnClickListener(this);
		mSeekBar = (SeekBar) findViewById(R.id.option_volume_seekBar);
		mSeekBar.setOnSeekBarChangeListener(this);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);// Manager���ô˷����õ���һ��ϵͳ����
		mSeekBar.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));// ��ȡ��ý���������
		mSeekBar.setProgress(mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));// ���ý�������ǰ����
	}

	/**
	 * ȫ��
	 * */
	public void setFullScreen() {
		// ȥ����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȥ��Ϣ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.option_volume_add:// ������
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,// ��Ҫ�ı�������Դ����ý�壩
					AudioManager.ADJUST_RAISE,// ��������
					AudioManager.FLAG_PLAY_SOUND);// ����ʱ����Ԥ����

			break;
		case R.id.option_volume_min:// ������
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,// ��Ҫ�ı�������Դ����ý�壩
					AudioManager.ADJUST_LOWER,// ��������
					AudioManager.FLAG_PLAY_SOUND);// ����ʱ����Ԥ����

			break;

		case R.id.option_volume_switch:// ��������
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
				.getStreamVolume(AudioManager.STREAM_MUSIC));// ���ý�������ǰ����

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) // �û��϶���������ʱ���ִ������������������
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, // ��Ҫ�ı������Դ����ý�壩
					progress, // ��ǰ���õĽ��ȣ���С��
					AudioManager.FLAG_PLAY_SOUND);// ����ʱ����Ԥ����
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
