package com.xuan.bigappleui.demo.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigappleui.R;

/**
 * 弹出层View测试
 * 
 * @author xuan
 */
public class PopViewLayoutDemoActivity extends BPActivity {
	private DemoPopView demoPopView;

	@InjectView(R.id.button)
	private Button button;

	@InjectView(R.id.button1)
	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_popview);

		demoPopView = new DemoPopView(this);

		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				demoPopView.toggle();
			}
		});

		button1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				int m = (demoPopView.getPopMode() + 1) % 5;
				if (0 == m) {
					m++;
				}
				demoPopView.setPopMode(m);
				button1.setText("切换模式" + "(" + m + ")");
			}
		});
	}

}
