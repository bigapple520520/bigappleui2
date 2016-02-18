package com.xuan.bigappleui.demo.view.zoomimageview;

import java.util.HashMap;

import android.app.Activity;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageActivity;
import com.xuan.bigappleui.lib.view.photoview.app.handler.BUViewImageBaseHandler;
import com.xuan.bigappleui.lib.view.photoview.app.viewholder.WraperFragmentView;

/**
 * 这里可以自定义一些图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午1:53:47 $
 */
public class MyViewImageActivity extends BUViewImageActivity {
	public static final String LOADTYPE1 = "loadtype1";

	@Override
	protected void onInitViewImageHandler(
			HashMap<String, BUViewImageBaseHandler> handlerMap) {
		addViewImageHandler(new MyViewImageHandler());
	}

	/**
	 * 自定义实现一个图片加载处理
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午2:02:14 $
	 */
	class MyViewImageHandler extends BUViewImageBaseHandler {
		@Override
		public String getLoadType() {
			return LOADTYPE1;
		}

		@Override
		public void onHandler(String url,
				WraperFragmentView wraperFragmentView, Activity activity,
				Object[] datas) {
			wraperFragmentView.photoView.setImageResource(R.drawable.pic1);// 这里可以自己实现要加载的图片我这里测试而已
		}
	}

}
