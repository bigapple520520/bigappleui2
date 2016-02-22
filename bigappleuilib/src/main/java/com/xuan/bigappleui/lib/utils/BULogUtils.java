package com.xuan.bigappleui.lib.utils;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author xuan
 * 
 */
public abstract class BULogUtils {
	/**
	 * d日志
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		Log.d(BUConstants.TAG, msg);
	}

	/**
	 * e日志
	 * 
	 * @param msg
	 */
	public static void e(String msg, Exception e) {
		Log.e(BUConstants.TAG, msg, e);
	}

	/**
	 * e日志
	 * 
	 * @param msg
	 */
	public static void e(String msg) {
		Log.e(BUConstants.TAG, msg);
	}

}
