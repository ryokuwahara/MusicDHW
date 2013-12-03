package com.example.musicdhw;

import java.util.Arrays;
import java.util.List;

public class Const {

	// 音声ソース一覧
	public static final Integer[] MEDIA_SOURCE_ID = new Integer[] { R.raw.nc2422,
			R.raw.nc7400, R.raw.nc10100, R.raw.nc10812, R.raw.nc11577,
			R.raw.nc13447, R.raw.nc20349, R.raw.nc20612, R.raw.nc29204, };

	// タイトル一覧
	public static final List<String> LIST_TITLE = Arrays.asList("オルゴール",
			"捨てられた雪原", "ループ用BGM008", "ループ用BGM026", "春の陽気", "亡き王女の為のセプテット",
			"おてんば恋娘", "お茶の時間", "Starting Japan");

	// サービスコマンドキー
	public static final String INTENT_COMMAND_START = "START";
	public static final String INTENT_COMMAND_PAUSE = "PAUSE";
	public static final String INTENT_COMMAND_STOP = "STOP";
	public static final String INTENT_COMMAND_NEXT = "NEXT";
	public static final String INTENT_COMMAND_PRE = "PRE";
	public static final String INTENT_COMMAND_SHUFFLE = "SHUFFLE";
	public static final String INTENT_COMMAND_REPEATE = "REPEATE";
	
	// インテントキー
	public static final String INTENT_KEY_MEDIA_NO = "MEDIA_NO";
	

}
