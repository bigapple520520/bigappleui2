package com.xuan.bigappleui.demo.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.view.popview.BUPopViewLayout;

public class DemoPopView extends BUPopViewLayout {
	public DemoPopView(Context context) {
		super(context);
	}

	public DemoPopView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DemoPopView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onCreateView() {
		setContentView(R.layout.demo_popview_content).setPopMode(
				BUPopViewLayout.POPMODE_FROM_BOTTOM);
	}

}
