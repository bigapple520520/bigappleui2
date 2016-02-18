package com.xuan.bigappleui.demo.pull2refresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.xuan.bigappleui.R;

/**
 * 下拉刷新控件，使用时，需要引入<br>
 * 一个箭头图片：pull2refresh_arrow.png<br>
 * 一个头部布局文件：pull2refresh_footer.xml<br>
 * 一个尾部布局文件：pull2refresh_head.xml（这个不是必须的，只有想用尾部刷新时，引入即可）
 * 
 * @author xuan
 */
public class DemoActivity extends Activity {

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_pull2refresh_main);
		Button scrollViewBtn = (Button) findViewById(R.id.scrollViewBtn);
		scrollViewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this,
						ScrollViewDemo.class));
			}
		});

		Button webviewBtn = (Button) findViewById(R.id.webviewBtn);
		webviewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this, WebViewDemo.class));
			}
		});

		Button listViewBtn = (Button) findViewById(R.id.listViewBtn);
		listViewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this, ListViewDemo.class));
			}
		});

		Button gridViewBtn = (Button) findViewById(R.id.gridViewBtn);
		gridViewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this, GridViewDemo.class));
			}
		});

		Button anPullToRefreshListViewBtn = (Button) findViewById(R.id.anPullToRefreshListViewBtn);
		anPullToRefreshListViewBtn
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(DemoActivity.this,
								BUPullToRefreshViewDemoActivity.class));
					}
				});
	}

}
