package com.xuan.bigappleui.demo.pull2refresh;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.xuan.bigappleui.lib.pullrefresh.core.PullToRefreshBase;
import com.xuan.bigappleui.lib.pullrefresh.widget.PullToRefreshListView;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于ListView实现下拉刷新
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:45:35 $
 */
public class ListViewDemo extends Activity {
	private final List<String> dataList = new LinkedList<String>();

	private ListView listView;
	private BaseAdapter adapter;
	private PullToRefreshListView pullToRefreshListView;

	private boolean mIsStart;
	private int i = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		pullToRefreshListView = new PullToRefreshListView(this);
		setContentView(pullToRefreshListView);

		listView = pullToRefreshListView.getRefreshableView();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		//pullToRefreshListView.setPullDownRefreshEnabled(true);
		//pullToRefreshListView.setPullUpRefreshEnabled(true);
		// ListView特有的，滚到到底部后自动就加载更多方式

		// 设置下拉刷新和上拉加载回调
		pullToRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mIsStart = true;
						new GetDataTask().execute();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mIsStart = false;
						new GetDataTask().execute();
					}
				});
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if (mIsStart) {
				dataList.add(0, "下拉刷新");
			} else {
				dataList.add("上拉加载");
			}

			adapter.notifyDataSetChanged();
			pullToRefreshListView.onPullDownRefreshComplete();
			pullToRefreshListView.onPullUpRefreshComplete();
			// mPullListView.setHasMoreData(hasMoreData);
			pullToRefreshListView.setLastUpdatedLabel("哈哈");
			i--;
			if (i <= 0) {
				// pullToRefreshListView.setHasMoreData(false);
			}
			super.onPostExecute(result);
		}
	}

	private void initData() {
		dataList.add("1111111");
		dataList.add("222222");
		dataList.add("333333");
		dataList.add("44444444");
		dataList.add("5555555");
		dataList.add("6666666");
		dataList.add("7777777");
		dataList.add("8888888");
		dataList.add("99999999");
		dataList.add("1111111");
		dataList.add("1111111");
		dataList.add("1111111");
		dataList.add("222222");
		dataList.add("333333");
		dataList.add("44444444");
		dataList.add("5555555");
		dataList.add("6666666");
		dataList.add("7777777");
		dataList.add("8888888");
		dataList.add("99999999");
		dataList.add("1111111");
		dataList.add("1111111");
	}

}
