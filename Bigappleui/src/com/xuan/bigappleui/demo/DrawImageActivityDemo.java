package com.xuan.bigappleui.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xuan.bigappleui.lib.utils.ui.drawable.ArrowDrawable;
import com.xuan.bigappleui.lib.utils.ui.drawable.HookDrawable;
import com.xuan.bigappleui.lib.utils.ui.drawable.checkbox.CheckBoxNormalDrawable;
import com.xuan.bigappleui.lib.utils.ui.drawable.checkbox.CheckBoxSelectedDrawable;
import com.xuan.bigappleui.lib.utils.ui.drawable.fileicon.DefaultFileDrawable;
import com.xuan.bigappleui.lib.utils.ui.drawable.fileicon.FolderDrawable;
import com.xuan.bigappleui.lib.utils.ui.drawable.fileicon.TxtFileDrawable;
import com.xuan.bigappleui.lib.view.img.BUArrowImageView;
import com.xuan.bigappleui.lib.view.img.BUHookImageView;

/**
 * 测试自己绘制的一些图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-24 上午11:28:34 $
 */
public class DrawImageActivityDemo extends Activity {
	private LinearLayout root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		root = new LinearLayout(this);
		root.setOrientation(LinearLayout.VERTICAL);
		setContentView(root);

		addView(new BUArrowImageView(this));
		addView(new BUHookImageView(this));

		ImageView i = new ImageView(this);
		i.setImageDrawable(new ArrowDrawable());
		addView(i);

		ImageView i2 = new ImageView(this);
		i2.setImageDrawable(new HookDrawable());
		addView(i2);

		ImageView i3 = new ImageView(this);
		i3.setImageDrawable(new FolderDrawable());
		addView(i3);

		ImageView i4 = new ImageView(this);
		i4.setImageDrawable(new DefaultFileDrawable());
		addView(i4);

		ImageView i5 = new ImageView(this);
		i5.setImageDrawable(new CheckBoxNormalDrawable());
		addView(i5);

		ImageView i6 = new ImageView(this);
		i6.setImageDrawable(new CheckBoxSelectedDrawable());
		addView(i6);

		ImageView i7 = new ImageView(this);
		i7.setImageDrawable(new TxtFileDrawable());
		addView(i7);
	}

	private void addView(View view) {
		view.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
		root.addView(view);
	}

}
