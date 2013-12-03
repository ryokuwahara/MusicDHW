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

	// 選択されたリスト位置
	private int selPosition = 0;
	// ボタンを押下後の処理(true=Start,false=Stop)
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

		// ListViewにデータ表示
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice,
				Const.LIST_TITLE);
		listTitle.setAdapter(adapter);

		// 指定の行をチェック状態に設定		
		textTitle.setText(Const.LIST_TITLE.get(selPosition));
		listTitle.setItemChecked(selPosition, true);
		
		// イベント追加
		listTitle.setOnItemClickListener(this);
		btnPlay.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnPre.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnSuffle.setOnClickListener(this);

		// ブロードキャストレシーバの登録
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
		// BGMサービス起動
		Intent intent = new Intent(MainActivity.this, BGMService.class);

		// 各コマンド発行
		switch (v.getId()) {
		case R.id.btnPlay: // 再生：一時停止
			if (doStart) {
				intent.setAction(Const.INTENT_COMMAND_START);
				btnPlay.setImageResource(R.drawable.pause);
			} else {
				intent.setAction(Const.INTENT_COMMAND_PAUSE);
				btnPlay.setImageResource(R.drawable.play);
			}
			doStart = doStart ? false : true;
			break;
		case R.id.btnStop: // 停止
			intent.setAction(Const.INTENT_COMMAND_STOP);
			btnPlay.setImageResource(R.drawable.play);
			doStart = true;
			break;
		case R.id.btnPre:// 前に移動
			intent.setAction(Const.INTENT_COMMAND_PRE);
			btnPlay.setImageResource(R.drawable.pause);
			doStart = false;
			break;
		case R.id.btnNext:// 次に移動
			intent.setAction(Const.INTENT_COMMAND_NEXT);
			btnPlay.setImageResource(R.drawable.pause);
			doStart = false;
			break;
		case R.id.btnSuffle:// シャッフルモード：リピートモード
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

	// サービスからの情報を受け取る
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
