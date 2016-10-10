package com.share.jack.numberlockdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class MyPrefs {
	private static MyPrefs myPrefs;//私有化
	private SharedPreferences sp;
	//提供私有的构造方法
	private MyPrefs(){}
	/**
	 * 对外提供的初始化方法
	 * @return
	 */
	public static MyPrefs getInstance(){
		//初始化自身对象
		if(myPrefs == null){
			myPrefs = new MyPrefs();
		}
		return myPrefs;
	}

	/**
	 * 初始化SharedPreferences对象
	 * @param context
	 */
	public MyPrefs initSharedPreferences(Context context){
		//获取SharedPreferences对象
		if(sp == null){
			sp = context.getSharedPreferences(Consts.PREF_NAME,
					Context.MODE_PRIVATE);
		}
		return myPrefs;
	}

	/**
	 * 向SharedPreferences中写入String类型的数据
	 * @param key
	 * @param value
	 */
	public void writeString(String key, String value){
		//获取编辑器对象
		Editor editor = sp.edit();
		//写入数据
		editor.putString(key, value);
		editor.commit();//提交写入的数据
	}

	/**
	 * 根据key读取SharedPreferences中的String类型的数据
	 * @param key
	 * @return
	 */
	public String readString(String key){
		return sp.getString(key, "");
	}
}
