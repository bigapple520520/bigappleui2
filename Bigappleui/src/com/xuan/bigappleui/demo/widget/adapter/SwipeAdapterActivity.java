package com.xuan.bigappleui.demo.widget.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xuan.bigapple.lib.ioc.app.BPActivity;

/**
 * 列表侧滑测试
 * 
 * @author xuan
 */
public class SwipeAdapterActivity extends BPActivity {
	private static final List<String> dataList = new ArrayList<String>();

	static {
		for (int i = 0; i < 50; i++) {
			dataList.add("我是第" + i + "个");
		}
	}

}
