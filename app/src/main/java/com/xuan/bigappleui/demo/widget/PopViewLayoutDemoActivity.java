package com.xuan.bigappleui.demo.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuan.bigappleui.R;

/**
 * 弹出层View测试
 * 
 * @author xuan
 */
public class PopViewLayoutDemoActivity extends Activity {
	private DemoPopView demoPopView;

	private Button button;

	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_popview);

		button = (Button)findViewById(R.id.button);
		button1 = (Button)findViewById(R.id.button1);

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
