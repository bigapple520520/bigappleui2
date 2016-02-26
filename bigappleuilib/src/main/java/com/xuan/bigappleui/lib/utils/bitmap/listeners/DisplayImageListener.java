package com.xuan.bigappleui.lib.utils.bitmap.listeners;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.xuan.bigappleui.lib.utils.bitmap.BitmapDisplayConfig;

/**
 * 加载完图片后的回调显示接口
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-8-1 下午6:15:28 $
 */
public interface DisplayImageListener {

	/**
	 * 图片加载完成 回调的方法
	 * 
	 * @param imageView
	 *            图片显示控件
	 * @param bitmap
	 *            成功要显示的图片
	 * @param config
	 *            显示图片配置
	 */
	void loadCompleted(ImageView imageView, Bitmap bitmap,
					   BitmapDisplayConfig config);

	/**
	 * 图片加载失败回调的方法
	 * 
	 * @param imageView
	 *            图片显示控件
	 * @param config
	 *            显示配置
	 */
	void loadFailed(ImageView imageView, BitmapDisplayConfig config);

}
