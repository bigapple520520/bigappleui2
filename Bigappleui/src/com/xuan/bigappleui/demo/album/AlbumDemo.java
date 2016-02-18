package com.xuan.bigappleui.demo.album;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.utils.DialogUtils;
import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;

/**
 * 相册选择测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:29:05 $
 */
public class AlbumDemo extends Activity {
	public static final int ACTIVITY_RESULT_1 = 1;
	public static final int ACTIVITY_RESULT_2 = 2;
	public static final int ACTIVITY_RESULT_3 = 3;

	private final List<String> selPathList = new ArrayList<String>();
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_album);

		// 选择主题
		final Button theme = (Button) findViewById(R.id.theme);
		theme.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DialogUtils.select2(AlbumDemo.this, "请选择不同主题", true,
						new String[] { "绿色", "蓝色", "默认" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0,
									int position) {
								switch (position) {
								case 0:
									break;
								case 1:
									break;
								case 2:
									break;
								}
							}
						});
			}
		});

		// 单选
		Button danxuan = (Button) findViewById(R.id.danxuan);
		danxuan.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BUAlbum.gotoAlbumForSingle(AlbumDemo.this, ACTIVITY_RESULT_1);
			}
		});

		// 多选
		Button duoxuan = (Button) findViewById(R.id.duoxuan);
		duoxuan.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BUAlbum.gotoAlbumForMulti(AlbumDemo.this, ACTIVITY_RESULT_2);
			}
		});

		// 多选有限制张数
		Button duoxuan2 = (Button) findViewById(R.id.duoxuan2);
		duoxuan2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});

		// 图片存放
		GridView gridView = (GridView) findViewById(R.id.gridView);
		adapter = new BaseAdapter() {
			@Override
			public View getView(int position, View arg1, ViewGroup arg2) {
				RelativeLayout root = new RelativeLayout(AlbumDemo.this);
				ImageView imageView = new ImageView(AlbumDemo.this);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						200, 200);
				imageView.setLayoutParams(lp);
				root.addView(imageView);

				BitmapDisplayConfig config = new BitmapDisplayConfig();
				config.setBitmapMaxHeight(200);
				config.setBitmapMaxWidth(200);
				BPBitmapLoader.getInstance().display(imageView,
						selPathList.get(position), config);
				return root;
			}

			@Override
			public int getCount() {
				return selPathList.size();
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}
		};
		gridView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case ACTIVITY_RESULT_1:
			List<ImageItem> temp1 = BUAlbum.getSelListAndClear();
			for (ImageItem bi : temp1) {
				selPathList.add(bi.imagePath);
			}
			break;
		case ACTIVITY_RESULT_2:
			List<ImageItem> temp2 = BUAlbum.getSelListAndClear();
			for (ImageItem bi : temp2) {
				selPathList.add(bi.imagePath);
			}
			break;
		case ACTIVITY_RESULT_3:
			List<ImageItem> temp3 = BUAlbum.getSelListAndClear();
			for (ImageItem bi : temp3) {
				selPathList.add(bi.imagePath);
			}
			break;
		}
		adapter.notifyDataSetChanged();
	}

}
