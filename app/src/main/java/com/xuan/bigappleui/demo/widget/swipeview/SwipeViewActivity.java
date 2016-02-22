package com.xuan.bigappleui.demo.widget.swipeview;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigappleui.R;

public class SwipeViewActivity extends BPActivity {

	@InjectView(R.id.listView)
	private ListView listView;

	private static List<String> dataList = new ArrayList<String>();

	static {
		for (int i = 0; i < 100; i++) {
			dataList.add("我是第" + i + "个");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_list);

		listView.setAdapter(new SwipeViewDemoAdapter(this, dataList));
	}
}
