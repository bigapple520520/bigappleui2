package com.xuan.bigappleui.demo.lettersort;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.lettersort.entity.ItemContent;
import com.xuan.bigappleui.lib.lettersort.entity.ItemDivide;
import com.xuan.bigappleui.lib.lettersort.entity.ItemTopContent;
import com.xuan.bigappleui.lib.lettersort.view.LetterSortAdapter;
import com.xuan.bigappleui.lib.view.BUSwipeView;
import com.xuan.bigappleui.lib.view.BUSwipeView.SwipeCompleteListener;

/**
 * 字母排序列表控件demo的数据适配器实现
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午5:00:36 $
 */
public class LetterSortDemoAdapter extends LetterSortAdapter {
	private final Context context;

	public LetterSortDemoAdapter(List<ItemContent> fromList, Context context) {
		super(fromList, context);
		this.context = context;
	}

	@Override
	public View drawItemTopContent(int position, View convertView,
			ViewGroup parent, ItemTopContent itemTopContent) {
		View content = LayoutInflater.from(context).inflate(
				R.layout.demo_swipe_above, null);
		TextView leftText = (TextView) content.findViewById(R.id.leftText);
		leftText.setText("level" + itemTopContent.getLevel());
		return content;
	}

	@Override
	public View drawItemContent(int position, View convertView,
			ViewGroup parent, ItemContent itemContent) {
		convertView = null;

		BUSwipeView swipeView = null;
		if (null == convertView) {
			// 侧滑的正面部分
			View content = LayoutInflater.from(context).inflate(
					R.layout.demo_swipe_above, null);
			TextView leftText = (TextView) content.findViewById(R.id.leftText);
			leftText.setText(itemContent.getName());

			// 侧滑的背后部分
			View behind = LayoutInflater.from(context).inflate(
					R.layout.demo_swipe_behind, null);

			// 侧滑容器
			swipeView = new BUSwipeView(context);
			swipeView.addContentAndBehind(content, behind);
			swipeView.setBehindWidthRes(R.dimen.demo_swipe_behind_offset);
		} else {
			swipeView = (BUSwipeView) convertView;
		}

		swipeView.setSwipeCompleteListener(new SwipeCompleteListener() {
			@Override
			public void whichScreen(int which) {
				if (which == BUSwipeView.CURSCREEN_CONTENT) {
					Log.d("", "我侧滑到了content页面");
				} else if (which == BUSwipeView.CURSCREEN_BEHIND) {
					Log.d("", "我侧滑到了behind页面");
				}

			}
		});

		final BUSwipeView swipeViewF = swipeView;
		// swipeView.getContent().setOnClickListener(new View.OnClickListener()
		// {
		// @Override
		// public void onClick(View v) {
		// if (BUSwipeView.CURSCREEN_BEHIND == swipeViewF.getCurScreen()) {
		// swipeViewF.showContent();
		// }
		// }
		// });

		swipeView.getContent().setClickable(true);
		swipeView.getContent().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					if (BUSwipeView.CURSCREEN_BEHIND == swipeViewF
							.getCurScreen()) {
						swipeViewF.showContent();
					}
				}
				return false;
			}
		});

		swipeView.getContent().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (BUSwipeView.CURSCREEN_BEHIND != swipeViewF.getCurScreen()) {
					Toast.makeText(context, "我被点击了", Toast.LENGTH_SHORT).show();
				}
			}
		});

		swipeView.getBehind().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeViewF.showContent();
				Toast.makeText(context, "我点击了删除", Toast.LENGTH_SHORT).show();
			}
		});

		swipeView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "我点击了item", Toast.LENGTH_SHORT).show();
			}
		});

		return swipeView;
	}

	@Override
	public View drawItemDivide(int position, View convertView,
			ViewGroup parent, ItemDivide itemDivide) {
		TextView textView = (TextView) LayoutInflater.from(context).inflate(
				R.layout.demo_lettersort_item_split, null);
		textView.setText(itemDivide.getLetter());
		return textView;
	}

}
