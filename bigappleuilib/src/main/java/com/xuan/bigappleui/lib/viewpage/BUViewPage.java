package com.xuan.bigappleui.lib.viewpage;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Scroller;

import com.xuan.bigappleui.lib.viewpage.listeners.OnScrollCompleteListener;

/**
 * 模仿launcher左右滑动的布局控件，可以设置偏移量
 * 
 * @author xuan
 */
public class BUViewPage extends ViewGroup {
	private static final int TOUCH_STATE_REST = 0;// 空闲状态
	private static final int TOUCH_STATE_SCROLLING = 1;// 正在滚动状态
	private int touchState = TOUCH_STATE_REST;

	private OnScrollCompleteListener internalOnScrollCompleteListener;
	private OnScrollCompleteListener mOnScrollCompleteListener;

	private Scroller scroller;
	private VelocityTracker velocityTracker;

	private int curScreen;// 当前屏幕的位置

	private static final int SNAP_VELOCITY = 600;// 速率，单位是：600px/s

	private int touchSlop;// 触发后小于该距离的，不移动
	private float lastMotionX;// 记录最后一次x坐标值
	private int offset = 0;// 偏移量

	private BaseAdapter mAdapter;// 数据适配器

	public BUViewPage(Context context) {
		super(context);
		init();
	}

	public BUViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BUViewPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	private void init() {
		scroller = new Scroller(getContext());
		curScreen = 0;

		ViewConfiguration config = ViewConfiguration.get(getContext());
		touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(config);

		// 内部实现
		internalOnScrollCompleteListener = new OnScrollCompleteListener() {
			@Override
			public void onScrollComplete(int toPosition) {
				if (null != mOnScrollCompleteListener) {
					mOnScrollCompleteListener.onScrollComplete(toPosition);
				}
			}
		};
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		scrollTo(curScreen * (width - 2 * offset), 0);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = offset;

		for (int i = 0, n = getChildCount(); i < n; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				int childWidth = childView.getMeasuredWidth() - 2 * offset;
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());

				// 下一个View距离父亲左边的距离 = 上一个View距离父亲左边的距离 + 上一个View的宽
				childLeft = childLeft + childWidth;
			}
		}
	}

	/**
	 * 根据当先Scroller停留位置，计算跳转到某个界面
	 */
	public void snapToDestination() {
		final int getWidth = getWidth() - 2 * offset;
		final int destScreen = (getScrollX() - offset + getWidth / 2)
				/ getWidth;
		snapToScreen(destScreen);
	}

	/**
	 * 平滑的切换到指定屏幕
	 * 
	 * @param whichScreen
	 */
	public void snapToScreen(int whichScreen) {
		int getWidth = getWidth() - 2 * offset;

		// 保证在[0,getChildCount() - 1]范围内
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		if (getScrollX() != (whichScreen * getWidth)) {
			int delta = (whichScreen * getWidth) - getScrollX();// 差距=目地-当前地

			// 从(getScrollX(),0)到(getScrollX()+delta,0)滚动，持续时间时间为delta的两倍（单位毫秒）
			scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);

			if (curScreen != whichScreen) {
				int temp = curScreen;
				curScreen = whichScreen;
				internalOnScrollCompleteListener.onScrollComplete(curScreen);
			}

			invalidate();
		}
	}

	/**
	 * 直接切换到指定屏幕
	 * 
	 * @param whichScreen
	 */
	public void setToScreen(int whichScreen) {
		int getWidth = getWidth() - 2 * offset;
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		int temp = curScreen;
		curScreen = whichScreen;
		scrollTo(whichScreen * getWidth, 0);
		internalOnScrollCompleteListener.onScrollComplete(curScreen);
		invalidate();
	}

	public int getCurScreen() {
		return curScreen;
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			invalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (null == velocityTracker) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			lastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (lastMotionX - x);
			lastMotionX = x;
			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP:
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();

			if (velocityX > SNAP_VELOCITY && curScreen > 0) {
				snapToScreen(curScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& curScreen < getChildCount() - 1) {
				snapToScreen(curScreen + 1);
			} else {
				snapToDestination();
			}

			if (null != velocityTracker) {
				velocityTracker.recycle();
				velocityTracker = null;
			}

			touchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			touchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		// 如果事件是正在移动，且触发状态不在空闲状态，就拦截事件，不让事件往子控件传递
		if ((action == MotionEvent.ACTION_MOVE)
				&& (touchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastMotionX = x;
			touchState = scroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(lastMotionX - x);
			if (xDiff > touchSlop) {
				touchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			touchState = TOUCH_STATE_REST;
			break;
		}

		return touchState != TOUCH_STATE_REST;
	}

	/**
	 * 添加滚动后屏幕变化的监听事件
	 * 
	 * @param l
	 */
	public void setOnScrollCompleteListener(OnScrollCompleteListener l) {
		this.mOnScrollCompleteListener = l;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setOffsetByResid(int resid) {
		this.offset = (int) getResources().getDimension(resid);
	}

	/**
	 * 设置适配器
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		this.mAdapter = adapter;
		removeAllViews();
		int count = adapter.getCount();
		if (count < 1) {
			// 适配器没有数据哦
			return;
		}

		for (int i = 0; i < count; i++) {
			addView(adapter.getView(i, null, null));
		}
	}

}
