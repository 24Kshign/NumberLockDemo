package com.share.jack.numberlockdemo.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class SystemUtils {

	/**
	 * 获取屏幕的宽和高
	 * @param context
	 * 					参数为上下文对象Context
	 * @return
	 * 			返回值为长度为2int型数组,其中
	 * 			int[0] -- 表示屏幕的宽度
	 * 			int[1] -- 表示屏幕的高度
	 */
	public static int[] getSystemDisplay(Context context){
		//创建保存屏幕信息类
		DisplayMetrics dm = new DisplayMetrics();
		//获取窗口管理类
		WindowManager wm =  (WindowManager) context.getSystemService(
				Context.WINDOW_SERVICE);
		//获取屏幕信息并保存到DisplayMetrics中
		wm.getDefaultDisplay().getMetrics(dm);
		//声明数组保存信息
		int[] displays = new int[2];
		displays[0] = dm.widthPixels;//屏幕宽度(单位:px)
		displays[1] = dm.heightPixels;//屏幕高度
		return displays;
	}
}
