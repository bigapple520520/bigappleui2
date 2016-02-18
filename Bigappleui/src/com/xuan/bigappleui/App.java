package com.xuan.bigappleui;

import android.app.Application;

import com.xuan.bigapple.lib.Bigapple;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;

/**
 * 程序入口
 * 
 * @author xuan
 */
public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		BPBitmapLoader.init(this);
		Bigapple.init(this);
	}

}
