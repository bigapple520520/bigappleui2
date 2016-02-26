package com.xuan.bigappleui;

import android.app.Application;

import com.xuan.bigappleui.lib.BigappleUI;

/**
 * 程序入口
 * 
 * @author xuan
 */
public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		BigappleUI.init(this);
	}

}
