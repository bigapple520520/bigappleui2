package com.xuan.bigappleui.demo.widget.swipeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.utils.BUToastUtil;
import com.xuan.bigappleui.lib.widget.swipeview.adapters.BaseSwipeAdapter;

import java.util.List;

/**
 * 书架适配器
 * 
 * Created by xb on 2015/8/2.
 */
public class SwipeViewDemoAdapter extends BaseSwipeAdapter {
	protected Context context;
	protected List<String> dataList;

	public SwipeViewDemoAdapter(Context context, List<String> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipe;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(
				R.layout.demo_widgets_swipeview_item, null);
	}

	@Override
	public void fillValues(final int position, final View convertView) {
		closeAllItems();

		TextView behind1 = (TextView) convertView.findViewById(R.id.behind1);
		TextView behind2 = (TextView) convertView.findViewById(R.id.behind2);
		View above = convertView.findViewById(R.id.above);
		TextView aboveTv = (TextView) convertView.findViewById(R.id.aboveTv);

		aboveTv.setText(dataList.get(position));
		behind1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BUToastUtil.displayTextShort("" + position);
				// closeItem(position);
				closeAllItems();
			}
		});
		behind2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BUToastUtil.displayTextShort("" + position);
				// closeItem(position);
				closeAllItems();
			}
		});
		above.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BUToastUtil.displayTextShort("我被点击了");
			}
		});
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}