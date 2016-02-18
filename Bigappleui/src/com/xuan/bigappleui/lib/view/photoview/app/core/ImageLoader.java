package com.xuan.bigappleui.lib.view.photoview.app.core;

import android.widget.ImageView;

import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.utils.Validators;

/**
 * 图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 上午11:46:00 $
 */
public abstract class ImageLoader {

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 * @param url
	 */
	public static void display(ImageView imageView, String url,
			BitmapDisplayConfig config) {
		if (null == imageView || Validators.isEmpty(url)) {
			return;
		}

		BPBitmapLoader.getInstance().display(imageView, url);
	}

}
