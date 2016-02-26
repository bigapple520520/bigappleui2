package com.xuan.bigappleui.lib.utils.bitmap.core.impl.net;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xuan.bigappleui.lib.utils.BULogUtil;
import com.xuan.bigappleui.lib.utils.asynctask.helper.CompatibleAsyncTask;
import com.xuan.bigappleui.lib.utils.bitmap.BitmapDisplayConfig;
import com.xuan.bigappleui.lib.utils.bitmap.BitmapGlobalConfig;
import com.xuan.bigappleui.lib.utils.bitmap.core.cache.BitmapCacheManager;
import com.xuan.bigappleui.lib.utils.bitmap.core.impl.IBitmapLoader;
import com.xuan.bigappleui.lib.utils.bitmap.listeners.ClearCacheListener;

import java.lang.ref.WeakReference;

/**
 * 网络图片加载工具类<br>
 * 注意： <br>
 * 1、使用者在使用时请保持单例，这样缓存设置的最大阀值才能被限制住<br>
 * 2、创建AnBitmapUtils实例所用的Context请使用Application对象，不要用Activity对象，防止Activity内存泄露<br>
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-8-9 下午4:48:21 $
 */
public class NetBitmapLoaderImpl implements IBitmapLoader {
	public boolean DUBEG = false;

	private boolean pauseTask = false;
	private final Object pauseTaskLock = new Object();

	private final Context application;
	private BitmapGlobalConfig defaultBitmapGlobalConfig;
	private BitmapDisplayConfig defaultBitmapDisplayConfig;
	private final BitmapCacheManager bitmapCacheManager;

	// ///////////////////////////////
	// 创建实例，用户使用时可自行保持单例/////////////////////////////////////////////
	public NetBitmapLoaderImpl(Context application) {
		this.application = application;
		defaultBitmapGlobalConfig = new BitmapGlobalConfig(application);
		defaultBitmapDisplayConfig = new BitmapDisplayConfig();
		bitmapCacheManager = BitmapCacheManager
				.getInstance(defaultBitmapGlobalConfig.getBitmapCache());
	}

	/**
	 * 获取默认的图片显示配置
	 * 
	 * @return
	 */
	@Override
	public BitmapDisplayConfig getDefaultBitmapDisplayConfig() {
		return defaultBitmapDisplayConfig;
	}

	/**
	 * 设置默认的图片显示配置
	 * 
	 * @param defaultBitmapDisplayConfig
	 */
	@Override
	public IBitmapLoader setDefaultBitmapDisplayConfig(
			BitmapDisplayConfig defaultBitmapDisplayConfig) {
		this.defaultBitmapDisplayConfig = defaultBitmapDisplayConfig;
		return this;
	}

	/**
	 * 获取图片加载各种参数配置
	 * 
	 * @return
	 */
	@Override
	public BitmapGlobalConfig getDefaultBitmapGlobalConfig() {
		return defaultBitmapGlobalConfig;
	}

	/**
	 * 设置图片加载各种参数配置
	 * 
	 * @param defaultBitmapGlobalConfig
	 */
	@Override
	public IBitmapLoader setDefaultBitmapGlobalConfig(
			BitmapGlobalConfig defaultBitmapGlobalConfig) {
		this.defaultBitmapGlobalConfig = defaultBitmapGlobalConfig;
		return this;
	}

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 *            显示图片控件
	 * @param uri
	 *            图片地址
	 * @param displayConfig
	 *            图片显示规格
	 */
	@Override
	public void display(ImageView imageView, String uri,
			BitmapDisplayConfig displayConfig) {
		if (null == imageView) {
			BULogUtil.e("图片加载不处理，原因：图片显示控件imageView为空");
			return;
		}

		if (null == displayConfig) {
			displayConfig = defaultBitmapDisplayConfig;
		}

		if (TextUtils.isEmpty(uri)) {
			displayConfig.getDisplayImageListener().loadFailed(imageView,
					displayConfig);
			return;
		}

		// 内存缓存中取
		String cacheKey = defaultBitmapGlobalConfig.getMakeCacheKeyListener()
				.makeCacheKey(uri);
		Bitmap bitmap = defaultBitmapGlobalConfig.getBitmapCache()
				.getBitmapFromMemCache(cacheKey, displayConfig);
		if (null != bitmap) {
			if (DUBEG) {
				BULogUtil.d("-----------------yes!!! memory cache shot!!!");
			}

			// 内存缓存命中
			displayConfig.getDisplayImageListener().loadCompleted(imageView,
					bitmap, displayConfig);
		} else if (!bitmapLoadTaskExist(imageView, uri)) {
			// 启动任务类：从网络下载或者从磁盘中获取
			final BitmapLoadTask loadTask = new BitmapLoadTask(imageView,
					displayConfig);
			final AsyncBitmapDrawable asyncBitmapDrawable = new AsyncBitmapDrawable(
					application.getResources(),
					displayConfig.getLoadingBitmap(), loadTask);
			imageView.setImageDrawable(asyncBitmapDrawable);// 设置下载任务资源
			loadTask.executeOnExecutor(
					defaultBitmapGlobalConfig.getBitmapLoadExecutor(), uri);
		}
	}

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 *            显示图片控件
	 * @param uri
	 *            图片地址
	 */
	public void display(ImageView imageView, String uri) {
		display(imageView, uri, null);
	}

	/**
	 * 清理内存缓存和磁盘缓存
	 * 
	 * @param listener
	 *            清理后回调
	 */
	@Override
	public void clearCacheAll(ClearCacheListener listener) {
		bitmapCacheManager.clearCache(listener);
	}

	/**
	 * 清理指定内存缓存和磁盘缓存
	 * 
	 * @param uri
	 *            图片地址
	 * @param listener
	 *            清理后回调
	 */
	@Override
	public void clearCache(String uri, ClearCacheListener listener) {
		bitmapCacheManager.clearCache(uri, listener);
	}

	/**
	 * 从缓存中获取已缓存的图片，如果没有，返回null
	 * 
	 * @param uri
	 *            图片地址
	 * @return
	 */
	@Override
	public Bitmap getBitmapFromCache(String uri,
			BitmapDisplayConfig displayConfig) {
		if (null == displayConfig) {
			displayConfig = defaultBitmapDisplayConfig;
		}

		Bitmap bitmap = defaultBitmapGlobalConfig.getBitmapCache()
				.getBitmapFromMemCache(uri, displayConfig);
		if (null == bitmap) {
			bitmap = defaultBitmapGlobalConfig.getBitmapCache()
					.getBitmapFromDiskCache(uri, displayConfig);
		}

		return bitmap;
	}

	/**
	 * 清理内存缓存和磁盘缓存
	 */
	public void clearCache() {
		bitmapCacheManager.clearCache(null);
	}

	/**
	 * 清理内存缓存
	 * 
	 * @param listener
	 *            清理后回调
	 */
	public void clearMemoryCache(ClearCacheListener listener) {
		bitmapCacheManager.clearMemoryCache(listener);
	}

	/**
	 * 清理内存缓存
	 */
	public void clearMemoryCache() {
		bitmapCacheManager.clearMemoryCache(null);
	}

	/**
	 * 清理磁盘缓存
	 * 
	 * @param listener
	 *            清理后回调
	 */
	public void clearDiskCache(ClearCacheListener listener) {
		bitmapCacheManager.clearDiskCache(listener);
	}

	/**
	 * 清理磁盘缓存
	 */
	public void clearDiskCache() {
		bitmapCacheManager.clearDiskCache(null);
	}

	/**
	 * 清理指定内存缓存和磁盘缓存
	 * 
	 * @param uri
	 *            图片地址
	 */
	public void clearCache(String uri) {
		bitmapCacheManager.clearCache(uri, null);
	}

	/**
	 * 清理指定内存缓存
	 * 
	 * @param uri
	 *            图片地址
	 * @param listener
	 *            清理后回调
	 */
	public void clearMemoryCache(String uri, ClearCacheListener listener) {
		bitmapCacheManager.clearMemoryCache(uri, listener);
	}

	/**
	 * 清理指定内存缓存
	 * 
	 * @param uri
	 *            图片地址
	 */
	public void clearMemoryCache(String uri) {
		bitmapCacheManager.clearMemoryCache(uri, null);
	}

	/**
	 * 清理指定磁盘缓存
	 * 
	 * @param uri
	 *            图片地址
	 * @param listener
	 *            清理后回调
	 */
	public void clearDiskCache(String uri, ClearCacheListener listener) {
		bitmapCacheManager.clearDiskCache(uri, listener);
	}

	/**
	 * 清理指定磁盘缓存
	 * 
	 * @param uri
	 *            图片地址
	 */
	public void clearDiskCache(String uri) {
		bitmapCacheManager.clearDiskCache(uri, null);
	}

	/**
	 * flush内存缓存和磁盘缓存
	 * 
	 * @param listener
	 *            清理后回调
	 */
	public void flushCache(ClearCacheListener listener) {
		bitmapCacheManager.flushCache(listener);
	}

	/**
	 * flush内存缓存和磁盘缓存
	 */
	public void flushCache() {
		bitmapCacheManager.flushCache(null);
	}

	/**
	 * 关闭内存缓存和磁盘缓存
	 * 
	 * @param listener
	 *            清理后回调
	 */
	@Override
	public void closeCache(ClearCacheListener listener) {
		bitmapCacheManager.closeCache(listener);
	}

	// //////////////////////////////////任务暂定开始操作///////////////////////////////////////////////////////////////
	/**
	 * 重启加载任务
	 */
	@Override
	public void resumeTasks() {
		pauseTask = false;
	}

	/**
	 * 暂停加载任务
	 */
	@Override
	public void pauseTasks() {
		pauseTask = true;
		flushCache(null);
	}

	/**
	 * 停止加载任务。一般退出程序时可以调用，用来释放，所有被暂定的任务
	 */
	public void stopTasks() {
		pauseTask = true;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	// ///////////////////////////////判断获取ImageView的下载任务///////////////////////////////////////////////////////
	// 获取ImageView控配置的下载任务类，第一次肯定不存在，不过设置过一次后就会存在
	private static BitmapLoadTask getBitmapTaskFromImageView(ImageView imageView) {
		if (null != imageView) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncBitmapDrawable) {
				final AsyncBitmapDrawable asyncBitmapDrawable = (AsyncBitmapDrawable) drawable;
				return asyncBitmapDrawable.getBitmapLoadTask();
			}
		}
		return null;
	}

	// 判断ImageView针对uri这个地址是否已存在下载任务类，如果已存在了，那就不用处理了
	private static boolean bitmapLoadTaskExist(ImageView imageView, String uri) {
		final BitmapLoadTask oldLoadTask = getBitmapTaskFromImageView(imageView);

		if (null != oldLoadTask) {
			final String oldUri = oldLoadTask.uri;
			if (TextUtils.isEmpty(oldUri) || !oldUri.equals(uri)) {
				oldLoadTask.cancel(true);// 取消原先在下载的地址任务
			} else {
				return true;
			}
		}
		return false;
	}

	// ///////////////////////////////////内部类定义////////////////////////////////////////////////////////////////////
	/**
	 * 包含加载任务的图片资源
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2013-9-17 下午2:44:24 $
	 */
	private class AsyncBitmapDrawable extends BitmapDrawable {
		private final WeakReference<BitmapLoadTask> bitmapLoadTask;

		public AsyncBitmapDrawable(Resources res, Bitmap bitmap,
				BitmapLoadTask loadTask) {
			super(res, bitmap);
			bitmapLoadTask = new WeakReference<BitmapLoadTask>(loadTask);
		}

		public BitmapLoadTask getBitmapLoadTask() {
			return bitmapLoadTask.get();
		}
	}

	/**
	 * 图片加载任务
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2013-9-17 下午2:45:22 $
	 */
	private class BitmapLoadTask extends
			CompatibleAsyncTask<Object, Void, Bitmap> {
		private String uri;
		private final WeakReference<ImageView> targetImageViewReference;
		private final BitmapDisplayConfig displayConfig;

		public BitmapLoadTask(ImageView imageView, BitmapDisplayConfig config) {
			targetImageViewReference = new WeakReference<ImageView>(imageView);
			displayConfig = config;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			if (null != params && params.length > 0) {
				uri = (String) params[0];
			} else {
				return null;
			}

			Bitmap bitmap = null;
			synchronized (pauseTaskLock) {
				while (pauseTask && !this.isCancelled()) {
					try {
						// 这里是如果调用者设置了暂定，并这个任务没有被取消，那么就停留在这里等待
						pauseTaskLock.wait();
					} catch (InterruptedException e) {
						// Ignore
					}
				}
			}

			/* 先试图从磁盘缓存中读取图片 */
			if (!pauseTask && !this.isCancelled()
					&& null != this.getTargetImageView()) {
				String cacheKey = defaultBitmapGlobalConfig
						.getMakeCacheKeyListener().makeCacheKey(uri);
				bitmap = defaultBitmapGlobalConfig.getBitmapCache()
						.getBitmapFromDiskCache(cacheKey, displayConfig);
				if (null != bitmap && DUBEG) {
					BULogUtil.d("-----------------yes!!! disk cache shot!!!");
				}
			} else {
				if (DUBEG) {
					BULogUtil.d("-----------------o god!!! i want disk cache,but you not need me anymore!!!");
				}
			}

			/* 当磁盘缓存中的图片不存在了，就去网络上加载需要的图片 */
			if (null == bitmap && !pauseTask && !this.isCancelled()
					&& null != this.getTargetImageView()) {
				if (DUBEG) {
					BULogUtil.d("-----------------no!!! cache not shot,i need download!!!");
				}

				bitmap = defaultBitmapGlobalConfig.getBitmapCache()
						.cacheBitmapFromUri(uri, displayConfig);
			} else {
				if (DUBEG) {
					BULogUtil.d("-----------------o god!!! i want download,but you not need me anymore!!!");
				}
			}

			return bitmap;
		}

		// 获取图片任务完成
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled() || pauseTask) {
				bitmap = null;
			}

			final ImageView imageView = this.getTargetImageView();
			if (imageView != null) {
				if (bitmap != null) {
					// 显示图片
					displayConfig.getDisplayImageListener().loadCompleted(
							imageView, bitmap, displayConfig);
				} else {
					// 显示获取错误图片
					displayConfig.getDisplayImageListener().loadFailed(
							imageView, displayConfig);
				}
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
			synchronized (pauseTaskLock) {
				pauseTaskLock.notifyAll();
			}
		}

		// 获取线程匹配的imageView,防止出现闪动的现象
		private ImageView getTargetImageView() {
			final ImageView imageView = targetImageViewReference.get();
			final BitmapLoadTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

}
