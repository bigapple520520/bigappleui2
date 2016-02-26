package com.xuan.bigappleui.demo.widget.swipeview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.xuan.bigappleui.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeViewActivity extends Activity {

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

		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(new SwipeViewDemoAdapter(this, dataList));
	}

}
