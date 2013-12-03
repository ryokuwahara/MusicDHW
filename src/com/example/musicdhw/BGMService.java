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
	//���t����鉹�yID�i�[��
	private ArrayList<Integer> listPlayMediaNo; 
	//���t���̈ʒu
	private int selPosition;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// �����l�ݒ�
		selPosition = 0;
		listPlayMediaNo = new ArrayList<Integer>();
		setListDt();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int preMediaNo;
		// �R�}���h�ɂ���ď�����ς���
		if (intent.getAction().equals(Const.INTENT_COMMAND_START)) {
			// �Đ���
			playMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_PAUSE)) {
			// ��~��
			player.pause();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_STOP)) {
			// ��~��
			stopMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_NEXT)) {
			// ��
			playNextMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_PRE)) {
			// �O
			playPreMusic();
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_REPEATE)) {
			// ���s�[�g�i�z���������Ԃɖ߂��ˌ��ɖ߂�����Ԃł̌��݈ʒu���擾�j
			preMediaNo = listPlayMediaNo.get(selPosition);
			setListDt();
			selPosition = getselPosition(preMediaNo);
		} else if (intent.getAction().equals(Const.INTENT_COMMAND_SHUFFLE)) {
			// �V���b�t���i�z����V���b�t������˃V���b�t��������Ԃł̌��݈ʒu���擾�j
			preMediaNo = listPlayMediaNo.get(selPosition);
			Collections.shuffle(listPlayMediaNo);
			selPosition = getselPosition(preMediaNo);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// MediaPlayer�Đ�
	public void playMusic() {
		if (player == null) {
			player = MediaPlayer.create(this, listPlayMediaNo.get(selPosition));
		}
		// �u���[�h�L���X�g�̑��M�i�A�N�e�B�r�e�B���Ɍ��݉��t���̔ԍ���n���j
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra(Const.INTENT_KEY_MEDIA_NO,
				getListviewPosition(listPlayMediaNo.get(selPosition)));
		broadcastIntent.setAction("MY_ACTION");
		getBaseContext().sendBroadcast(broadcastIntent);
		// �Đ�
		player.start();
		player.setOnCompletionListener(this);
	}

	// MediaPlayer��~
	public void stopMusic() {
		player.release();
		player = null;
	}

	// �O�̉��y�Đ�
	public void playNextMusic() {
		stopMusic();
		if (selPosition == listPlayMediaNo.size() - 1) {
			selPosition = 0;
		} else {
			selPosition++;
		}
		playMusic(); // �Đ���
	}

	// ���̉��y�Đ�
	public void playPreMusic() {
		stopMusic();
		if (selPosition == 0) {
			selPosition = listPlayMediaNo.size() - 1;
		} else {
			selPosition--;
		}
		playMusic(); // �Đ���
	}

	// ���yID��茻�݂̔z��ʒu���擾
	public int getselPosition(int mediaNo) {
		// ���݂̃��X�g�ʒu��ς���
		for (int i = 0; i < listPlayMediaNo.size(); i++) {
			if (mediaNo == listPlayMediaNo.get(i)) {
				return i;
			}
		}
		return 0;
	}

	// ���yID��胊�X�g�����[�̈ʒu���擾
	public int getListviewPosition(int mediaNo) {
		// ���݂̃��X�g�ʒu��ς���
		for (int i = 0; i < Const.MEDIA_SOURCE_ID.length; i++) {
			if (mediaNo == Const.MEDIA_SOURCE_ID[i]) {
				return i;
			}
		}
		return 0;
	}

	// ���X�g�f�[�^�Z�b�g
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

	// ���y�I�����Ɏ��s�����
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// ���̉��y���s
		playNextMusic();
	}

}
