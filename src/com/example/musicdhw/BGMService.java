package com.example.musicdhw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BGMService extends Service implements OnCompletionListener {

	private MediaPlayer player;
	//演奏される音楽ID格納先
	private ArrayList<Integer> listPlayMediaNo; 
	//演奏中の位置
	private int selPosition;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// 初期値設定
		selPosition = 0;
		listPlayMediaNo = new ArrayList<Integer>();
		setListDt();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int preMediaNo;
		// コマンドによって処理を変える
		if (intent.getAction().equals(Const.INTENT_COMMAND_START)) {
			// 再生時
			playMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_PAUSE)) {
			// 停止時
			player.pause();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_STOP)) {
			// 停止時
			stopMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_NEXT)) {
			// 次
			playNextMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_PRE)) {
			// 前
			playPreMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_REPEATE)) {
			// リピート（配列を初期状態に戻す⇒元に戻した状態での現在位置を取得）
			preMediaNo = listPlayMediaNo.get(selPosition);
			setListDt();
			selPosition = getselPosition(preMediaNo);
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_SHUFFLE)) {
			// シャッフル（配列をシャッフルする⇒シャッフルした状態での現在位置を取得）
			preMediaNo = listPlayMediaNo.get(selPosition);
			Collections.shuffle(listPlayMediaNo);
			selPosition = getselPosition(preMediaNo);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// MediaPlayer再生
	public void playMusic() {
		if (player == null) {
			player = MediaPlayer.create(this, listPlayMediaNo.get(selPosition));
		}
		// ブロードキャストの送信（アクティビティ側に現在演奏中の番号を渡す）
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra(Const.INTENT_KEY_MEDIA_NO,
				getListviewPosition(listPlayMediaNo.get(selPosition)));
		broadcastIntent.setAction("MY_ACTION");
		getBaseContext().sendBroadcast(broadcastIntent);
		// 再生
		player.start();
		player.setOnCompletionListener(this);
	}

	// MediaPlayer停止
	public void stopMusic() {
		player.release();
		player = null;
	}

	// 前の音楽再生
	public void playNextMusic() {
		stopMusic();
		if (selPosition == listPlayMediaNo.size() - 1) {
			selPosition = 0;
		} else {
			selPosition++;
		}
		playMusic(); // 再生時
	}

	// 次の音楽再生
	public void playPreMusic() {
		stopMusic();
		if (selPosition == 0) {
			selPosition = listPlayMediaNo.size() - 1;
		} else {
			selPosition--;
		}
		playMusic(); // 再生時
	}

	// 音楽IDより現在の配列位置を取得
	public int getselPosition(int mediaNo) {
		// 現在のリスト位置を変える
		for (int i = 0; i < listPlayMediaNo.size(); i++) {
			if (mediaNo == listPlayMediaNo.get(i)) {
				return i;
			}
		}
		return 0;
	}

	// 音楽IDよりリストヴューの位置を取得
	public int getListviewPosition(int mediaNo) {
		// 現在のリスト位置を変える
		for (int i = 0; i < Const.MEDIA_SOURCE_ID.length; i++) {
			if (mediaNo == Const.MEDIA_SOURCE_ID[i]) {
				return i;
			}
		}
		return 0;
	}

	// リストデータセット
	public void setListDt() {
		listPlayMediaNo.clear();
		for (int i = 0; i < Const.MEDIA_SOURCE_ID.length; i++) {
			listPlayMediaNo.add(Const.MEDIA_SOURCE_ID[i]);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopMusic();
	}

	// 音楽終了時に実行される
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// 次の音楽実行
		playNextMusic();
	}

}
