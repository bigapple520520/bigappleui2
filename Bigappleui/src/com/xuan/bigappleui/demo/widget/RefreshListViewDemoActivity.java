package com.xuan.bigappleui.demo.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.widget.BURefreshListView;

/**
 * 下拉上啦刷新控件测试
 * 
 * @author xuan
 */
public class RefreshListViewDemoActivity extends BPActivity {
	@InjectView(R.id.refreshView)
	private BURefreshListView refreshView;

	private List<String> dataList;
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_refreshlistview);
		initData();
		initWidgets();
	}

	private void initData() {
		dataList = new ArrayList<String>();
		dataList.add("二楼是王八");
		dataList.add("我是二楼");
		dataList.add("二楼在逗比吧");
		dataList.add("二楼威武");
		dataList.add("我是二楼爷爷的儿子");
		dataList.add("唉，你们都这么空");
		dataList.add("是你妈逼的");
		dataList.add("真的是你妈逼的，不是你爸");
		dataList.add("我是逗比");
		dataList.add("少年你太天真了，你以为真有这么多人给你回复啊，其实是我换了个账号一个人在回复，不信我换个账号再发个同样的话");
		dataList.add("少年你太天真了，你以为真有这么多人给你回复啊，其实是我换了个账号一个人在回复，不信我换个账号再发个同样的话");
		dataList.add("少年你太天真了，你以为真有这么多人给你回复啊，其实是我换了个账号一个人在回复，不信我换个账号再发个同样的话");
	}

	private void initWidgets() {
		adapter = new BaseAdapter() {
			@Override
			public View getView(int position, View view, ViewGroup arg2) {
				String data = dataList.get(position);

				TextView textView = new TextView(
						RefreshListViewDemoActivity.this);
				textView.setHeight(100);
				textView.setText(data);

				return textView;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return dataList.size();
			}
		};

		refreshView.setAdapter(adapter);
		refreshView.setRefreshListener(new BURefreshListView.RefreshListener() {
			@Override
			public void onPullUpRefresh(boolean isManual) {
				refreshData(false);
			}

			@Override
			public void onPullDownRefresh(boolean isManual) {
				refreshData(true);
			}
		});
	}

	// 模拟加载数据
	private void refreshData(final boolean isPullDown) {
		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... params) {
				try {
					// 模式耗时操作,睡个2秒
					Thread.sleep(4000);
					String text = null;
					if (isPullDown) {
						text = "我下拉刷新出来的："
								+ DateUtils.date2StringBySecond(new Date());
						dataList.add(0, text);
					} else {
						text = "我上拉刷新出来的："
								+ DateUtils.date2StringBySecond(new Date());
						dataList.add(text);
					}
				} catch (Exception e) {
					// Ignore
				}

				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				adapter.notifyDataSetChanged();
				refreshView.onRefreshComplete("最后更新："
						+ DateUtils.date2StringBySecond(new Date()));
			}
		}.execute();
	}

}
