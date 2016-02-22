package com.xuan.bigappleui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xuan.bigappleui.demo.DrawImageActivityDemo;
import com.xuan.bigappleui.demo.album.AlbumDemo;
import com.xuan.bigappleui.demo.fileexplorer.FileExplorerDemo;
import com.xuan.bigappleui.demo.gifview.GifViewDemoActivity;
import com.xuan.bigappleui.demo.lettersort.LetterSortDemoActivity;
import com.xuan.bigappleui.demo.pull2refresh.DemoActivity;
import com.xuan.bigappleui.demo.slidingmenu.SlidingMenuDemoActivity;
import com.xuan.bigappleui.demo.slidingupdown.SlidingUpDownDemoActivity;
import com.xuan.bigappleui.demo.view.NumRadioButtonDemoActivity;
import com.xuan.bigappleui.demo.view.SlipButtonDemoActivity;
import com.xuan.bigappleui.demo.view.gridview.DragGridViewDemo;
import com.xuan.bigappleui.demo.view.roundedimageview.RoundedImageViewDemoActivity;
import com.xuan.bigappleui.demo.view.tab.SwTabHostDemo;
import com.xuan.bigappleui.demo.view.zoomimageview.ZoomImageViewDemoActivity;
import com.xuan.bigappleui.demo.viewpage.CyclePageDemoActivity;
import com.xuan.bigappleui.demo.viewpage.ViewPageDemoActivity;
import com.xuan.bigappleui.demo.widget.PopViewLayoutDemoActivity;
import com.xuan.bigappleui.demo.widget.RefreshListViewDemoActivity;
import com.xuan.bigappleui.demo.widget.swipeview.SwipeViewActivity;

public class BigappleUiMain extends Activity {
	private LinearLayout content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		content = (LinearLayout) findViewById(R.id.content);

		addButton("DragGridView", DragGridViewDemo.class);
		addButton("代码画的图片", DrawImageActivityDemo.class);
		addButton("gif图、圆角图、旋转图", GifViewDemoActivity.class);
		addButton("viewpage图片切页实现", ViewPageDemoActivity.class);
		addButton("cyclepage图片切页实现", CyclePageDemoActivity.class);
		addButton("列表字母分类排序 + 侧滑删除", LetterSortDemoActivity.class);
		addButton("侧滑模式实现", SlidingMenuDemoActivity.class);
		addButton("上下滑动有惊喜实现", SlidingUpDownDemoActivity.class);
		addButton("下拉刷新控件", DemoActivity.class);
		addButton("圆角图片实现", RoundedImageViewDemoActivity.class);
		addButton("SlipButton滑块demo", SlipButtonDemoActivity.class);
		addButton("NumRadioButton测试", NumRadioButtonDemoActivity.class);

		addButton("查看大图", ZoomImageViewDemoActivity.class);
		addButton("album选择相册", AlbumDemo.class);
		addButton("文件选择器", FileExplorerDemo.class);
		addButton("SwTabHost框架", SwTabHostDemo.class);
		addButton("PopViewLayout测试", PopViewLayoutDemoActivity.class);
		// addButton("WebView测试", BUWebViewDemo.class);
		addButton("BURefreshView测试", RefreshListViewDemoActivity.class);
		addButton("SwipeView测试", SwipeViewActivity.class);

		// addButton("Camera测试", CameraDemo.class);
	}

	private void addButton(String text, final Class<?> clazz) {
		Button button = new Button(this);
		button.setText(text);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(BigappleUiMain.this, clazz);
				startActivity(intent);
			}
		});
		content.addView(button);
	}

}
