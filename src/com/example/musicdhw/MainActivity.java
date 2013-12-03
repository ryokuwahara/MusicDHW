package com.example.musicdhw;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	// private MediaPlayer mediaPlayer = new MediaPlayer();
	private ImageButton btnPlay;
	private ImageButton btnStop;
	private ImageButton btnPre;
	private ImageButton btnNext;
	private ImageButton btnSuffle;
	private TextView textTitle;

	// �I�����ꂽ���X�g�ʒu
	private int selPosition = 0;
	// �{�^����������̏���(true=Start,false=Stop)
	private boolean doStart = true;
	private boolean doRepeat = true;
	private ListView listTitle;
	private MyBroadcastReceiver receiver;
	private IntentFilter intentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnStop = (ImageButton) findViewById(R.id.btnStop);
		btnPre = (ImageButton) findViewById(R.id.btnPre);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnSuffle = (ImageButton) findViewById(R.id.btnSuffle);
		textTitle = (TextView) findViewById(R.id.txtTitle);

		listTitle = (ListView) findViewById(R.id.listTitle);
		listTitle.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// ListView�Ƀf�[�^�\��
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice,
				Const.LIST_TITLE);
		listTitle.setAdapter(adapter);

		// �w��̍s���`�F�b�N��Ԃɐݒ�		
		textTitle.setText(Const.LIST_TITLE.get(selPosition));
		listTitle.setItemChecked(selPosition, true);
		
		// �C�x���g�ǉ�
		listTitle.setOnItemClickListener(this);
		btnPlay.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnPre.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnSuffle.setOnClickListener(this);

		// �u���[�h�L���X�g���V�[�o�̓o�^
		receiver = new MyBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction("MY_ACTION");
		registerReceiver(receiver, intentFilter);

	}

	public void setPosition(int position) {
		listTitle.setItemChecked(position, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		listTitle.setItemChecked(selPosition, true);;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// BGM�T�[�r�X�N��
		Intent intent = new Intent(MainActivity.this, BGMService.class);

		// �e�R�}���h���s
		switch (v.getId()) {
		case R.id.btnPlay: // �Đ��F�ꎞ��~
			if (doStart) {
				intent.setAction(Const.INTENT_COMMAND_START);
				btnPlay.setImageResource(R.drawable.pause);
			} else {
				intent.setAction(Const.INTENT_COMMAND_PAUSE);
				btnPlay.setImageResource(R.drawable.play);
			}
			doStart = doStart ? false : true;
			break;
		case R.id.btnStop: // ��~
			intent.setAction(Const.INTENT_COMMAND_STOP);
			btnPlay.setImageResource(R.drawable.play);
			doStart = true;
			break;
		case R.id.btnPre:// �O�Ɉړ�
			intent.setAction(Const.INTENT_COMMAND_PRE);
			btnPlay.setImageResource(R.drawable.pause);
			doStart = false;
			break;
		case R.id.btnNext:// ���Ɉړ�
			intent.setAction(Const.INTENT_COMMAND_NEXT);
			btnPlay.setImageResource(R.drawable.pause);
			doStart = false;
			break;
		case R.id.btnSuffle:// �V���b�t�����[�h�F���s�[�g���[�h
			if (doRepeat) {
				intent.setAction(Const.INTENT_COMMAND_SHUFFLE);
				btnSuffle.setImageResource(R.drawable.shuffle);
			} else {
				intent.setAction(Const.INTENT_COMMAND_REPEATE);
				btnSuffle.setImageResource(R.drawable.repeat);
			}
			btnPlay.setImageResource(R.drawable.pause);
			doRepeat = doRepeat ? false : true;
			doStart = false;
			break;
		default:
			break;
		}
		startService(intent);

	}

	// �T�[�r�X����̏����󂯎��
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			int titleNo = bundle.getInt(Const.INTENT_KEY_MEDIA_NO);
			textTitle.setText(Const.LIST_TITLE.get(titleNo));
			listTitle.setItemChecked(titleNo, true);
			listTitle.setSelection(titleNo);
			selPosition = titleNo;
		}
	}

}
