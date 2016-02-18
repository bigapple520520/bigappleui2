package com.xuan.bigappleui.demo.viewpage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.viewpage.BUViewPage;
import com.xuan.bigappleui.lib.viewpage.listeners.OnScrollCompleteListener;

/**
 * 图片切换页demo
 * 
 * @author xuan
 */
public class ViewPageDemoActivity extends Activity {
	private BUViewPage viewPage;
	private TextView textview;
	private Button button1;
	private Button button2;

	private final List<Integer> dataList = new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_viewpage_main);

		viewPage = (BUViewPage) findViewById(R.id.viewPage);
		textview = (TextView) findViewById(R.id.textview);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);

		dataList.add(R.drawable.demo_viewpage_pic1);
		dataList.add(R.drawable.demo_viewpage_pic2);
		dataList.add(R.drawable.demo_viewpage_pic3);
		dataList.add(R.drawable.demo_viewpage_pic4);
		dataList.add(R.drawable.demo_viewpage_pic5);

		// 设置偏移量
		int offset = getIntent().getIntExtra("offset", 0);
		viewPage.setOffset(offset);

		// 设置滚动后的监听器
		viewPage.setOnScrollCompleteListener(new OnScrollCompleteListener() {
			@Override
			public void onScrollComplete(int toPosition) {
				textview.setText("我滑动到了屏幕：[" + toPosition + "]");
			}
		});

		viewPage.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View arg1, ViewGroup arg2) {
				ImageView view = (ImageView) LayoutInflater.from(
						ViewPageDemoActivity.this).inflate(
						R.layout.demo_viewpage_imagelayout, null);
				view.setImageResource(dataList.get(position));
				return view;
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
		});

		// 设置按钮，暴力跳转到指定界面
		button1.setText("跳转到第1个界面");
		button1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPage.setToScreen(0);
			}
		});

		// 设置偏移量
		if (offset == 0) {
			button2.setText("设置50px的偏移量");
			button2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(ViewPageDemoActivity.this,
							ViewPageDemoActivity.class);
					intent.putExtra("offset", 50);
					startActivity(intent);
					ViewPageDemoActivity.this.finish();
				}
			});
		} else {
			button2.setText("取消偏移量");
			button2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(ViewPageDemoActivity.this,
							ViewPageDemoActivity.class);
					startActivity(intent);
					ViewPageDemoActivity.this.finish();
				}
			});
		}
	}

}
