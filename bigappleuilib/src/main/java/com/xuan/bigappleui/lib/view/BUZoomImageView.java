package com.xuan.bigappleui.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

/**
 * 缩放图片的控件，继承ImageView，用法跟ImageView差不多(效果不是很好,建议使用photoview)
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-15 上午10:23:49 $
 */
@Deprecated
public class BUZoomImageView extends ImageView {

	// 放大的最大倍数
	private float mMaxScale = 2.0f;

	// 被旋转的图片的高和宽
	private float rotatedImageW;
	private float rotatedImageH;

	// 图片的高和宽
	private float imageW;
	private float imageH;

	// ZoomImageView的高和宽
	private float viewW;
	private float viewH;

	private final Matrix matrix = new Matrix();
	private final Matrix savedMatrix = new Matrix();// 记录原始Matrix对象

	// 记录当前图片缩放状态
	private static final int NONE = 0;// 初始状态
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 缩放
	private static final int ROTATE = 3;// 旋转
	private static final int ZOOM_OR_ROTATE = 4; // 缩放或旋转
	private int mode = NONE;

	// 手指按下的点
	private final PointF pA = new PointF();
	private final PointF pB = new PointF();
	private final PointF mid = new PointF();// A点和B点连线的中间点
	private final PointF lastClickPos = new PointF();

	private long lastClickTime = 0;
	private double rotation = 0.0;
	private float dist = 1f;// 两点间的距离超过该值，就判定为多点模式

	// 单击事件
	private long clickDownTime;
	private Runnable singleClickRunnalbe;
	private boolean singleClickRunnalbeIsToHandler = false;
	private OnClickListener onClickListener;

	// 长按事件
	private int mTouchSlop;
	private int longClickTime = 2000;// 长按触发事件
	private float lastX;
	private float lastY;
	private Runnable longClickRunnalbe;
	private boolean longClickRunnalbeIsToHandler = false;
	private OnLongClickListener onLongClickListener;

	private final Handler handler = new Handler();

	public BUZoomImageView(Context context) {
		super(context);
		init(context);
	}

	public BUZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BUZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(final Context context) {
		setScaleType(ImageView.ScaleType.MATRIX);// 设置ImageView的矩阵缩放模式
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		setImageWidthHeight();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		setImageWidthHeight();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		setImageWidthHeight();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// view大小改变了就会被调用，第一次oldw和oldh都为0
		viewW = w;
		viewH = h;
		if (oldw == 0) {
			// 第一次加载图片，初始化图片
			initImage();
		} else {
			fixScale();
			fixTranslation();
			setImageMatrix(matrix);
		}
	}

	// 修正缩放比例
	private void fixScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min(viewW / rotatedImageW, viewH / rotatedImageH);
		if (curScale < minScale) {
			if (curScale > 0) {
				double scale = minScale / curScale;
				p[0] = (float) (p[0] * scale);
				p[1] = (float) (p[1] * scale);
				p[3] = (float) (p[3] * scale);
				p[4] = (float) (p[4] * scale);
				matrix.setValues(p);
			} else {
				matrix.setScale(minScale, minScale);
			}
		}
	}

	// 修正平移
	private void fixTranslation() {
		RectF rect = new RectF(0, 0, imageW, imageH);
		matrix.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0;
		float deltaY = 0;

		if (width < viewW) {
			deltaX = (viewW - width) / 2 - rect.left;
		} else if (rect.left > 0) {
			deltaX = -rect.left;
		} else if (rect.right < viewW) {
			deltaX = viewW - rect.right;
		}

		if (height < viewH) {
			deltaY = (viewH - height) / 2 - rect.top;
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewH) {
			deltaY = viewH - rect.bottom;
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	private float maxPostScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min(viewW / rotatedImageW, viewH / rotatedImageH);
		float maxScale = Math.max(minScale, mMaxScale);
		return maxScale / curScale;
	}

	/**
	 * 两点的距离，直角三角形算法
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private float spacing(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 双击
	 * 
	 * @param x
	 * @param y
	 */
	private void doubleClick(float x, float y) {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min(viewW / rotatedImageW, viewH / rotatedImageH);
		if (curScale <= minScale + 0.01) { // 放大
			float toScale = Math.max(minScale, mMaxScale) / curScale;
			matrix.postScale(toScale, toScale, x, y);
		} else { // 缩小
			float toScale = minScale / curScale;
			matrix.postScale(toScale, toScale, x, y);
			fixTranslation();
		}
		setImageMatrix(matrix);
	}

	// 设置图片的宽和高
	private void setImageWidthHeight() {
		Drawable d = getDrawable();
		if (d == null) {
			return;
		}

		imageW = rotatedImageW = d.getIntrinsicWidth();
		imageH = rotatedImageH = d.getIntrinsicHeight();
		initImage();
	}

	// 第一次加载初始化图片
	private void initImage() {
		if (viewW <= 0 || viewH <= 0 || imageW <= 0 || imageH <= 0) {
			return;
		}

		mode = NONE;
		matrix.setScale(0, 0);
		fixScale();
		fixTranslation();
		setImageMatrix(matrix);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			pA.set(event.getX(), event.getY());
			pB.set(event.getX(), event.getY());
			mode = DRAG;

			clickDownTime = System.currentTimeMillis();
			addLongClickListener();
			lastX = event.getX();
			lastY = event.getY();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			dist = spacing(event.getX(0), event.getY(0), event.getX(1),
					event.getY(1));
			// 如果连续两点距离大于10，则判定为多点模式
			if (dist > 10f) {
				savedMatrix.set(matrix);
				pA.set(event.getX(0), event.getY(0));
				pB.set(event.getX(1), event.getY(1));
				mid.set((event.getX(0) + event.getX(1)) / 2,
						(event.getY(0) + event.getY(1)) / 2);
				mode = ZOOM_OR_ROTATE;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			removeLongClickListener();
			break;
		case MotionEvent.ACTION_UP:
			removeLongClickListener();

			if (mode == DRAG) {
				// 单击检查
				if ((spacing(pA.x, pA.y, pB.x, pB.y) < 50)
						&& (System.currentTimeMillis() - clickDownTime < 500)) {
					addSingleClickListener();
				}
			}
		case MotionEvent.ACTION_POINTER_UP:
			if (mode == DRAG) {
				if (spacing(pA.x, pA.y, pB.x, pB.y) < 50) {
					// 检测双击
					long now = System.currentTimeMillis();
					if (now - lastClickTime < 500
							&& spacing(pA.x, pA.y, lastClickPos.x,
									lastClickPos.y) < 50) {
						removeSingleClickListener();
						doubleClick(pA.x, pA.y);
						now = 0;
					}

					lastClickPos.set(pA);
					lastClickTime = now;
				}
			} else if (mode == ROTATE) {
				int level = (int) Math.floor((rotation + Math.PI / 4)
						/ (Math.PI / 2));
				if (level == 4) {
					level = 0;
				}
				matrix.set(savedMatrix);
				matrix.postRotate(90 * level, mid.x, mid.y);
				if (level == 1 || level == 3) {
					float tmp = rotatedImageW;
					rotatedImageW = rotatedImageH;
					rotatedImageH = tmp;
					fixScale();
				}
				fixTranslation();
				setImageMatrix(matrix);
			}
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:// 处理缩放或者旋转，处理完后就能确定是缩放还是旋转了
			if (checkMovce(lastX, lastY, event.getX(), event.getY())) {
				removeLongClickListener();
			}

			// 先判定是缩放还是旋转
			if (mode == ZOOM_OR_ROTATE) {
				PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
						event.getY(1) - event.getY(0) + pA.y);
				double a = spacing(pB.x, pB.y, pC.x, pC.y);
				double b = spacing(pA.x, pA.y, pC.x, pC.y);
				double c = spacing(pA.x, pA.y, pB.x, pB.y);
				if (a >= 10) {
					double cosB = (a * a + c * c - b * b) / (2 * a * c);
					double angleB = Math.acos(cosB);
					double PID4 = Math.PI / 4;
					if (angleB > PID4 && angleB < 3 * PID4) {
						mode = ROTATE;
						rotation = 0;
					} else {
						mode = ZOOM;
					}
				}
			}

			if (mode == DRAG) {
				// 拖动
				matrix.set(savedMatrix);
				pB.set(event.getX(), event.getY());
				matrix.postTranslate(event.getX() - pA.x, event.getY() - pA.y);
				fixTranslation();
				setImageMatrix(matrix);
			} else if (mode == ZOOM) {
				// 缩放
				float newDist = spacing(event.getX(0), event.getY(0),
						event.getX(1), event.getY(1));
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = Math.min(newDist / dist, maxPostScale());
					matrix.postScale(tScale, tScale, mid.x, mid.y);
					fixScale();
					fixTranslation();
					setImageMatrix(matrix);
				}
			} else if (mode == ROTATE) {
				// 旋转
				PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
						event.getY(1) - event.getY(0) + pA.y);
				double a = spacing(pB.x, pB.y, pC.x, pC.y);
				double b = spacing(pA.x, pA.y, pC.x, pC.y);
				double c = spacing(pA.x, pA.y, pB.x, pB.y);
				if (b > 10) {
					double cosA = (b * b + c * c - a * a) / (2 * b * c);
					double angleA = Math.acos(cosA);
					double ta = pB.y - pA.y;
					double tb = pA.x - pB.x;
					double tc = pB.x * pA.y - pA.x * pB.y;
					double td = ta * pC.x + tb * pC.y + tc;
					if (td > 0) {
						angleA = 2 * Math.PI - angleA;
					}
					rotation = angleA;
					matrix.set(savedMatrix);
					matrix.postRotate((float) (rotation * 180 / Math.PI),
							mid.x, mid.y);
					setImageMatrix(matrix);
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		this.onLongClickListener = l;
	}

	// 添加单击事件
	private void addSingleClickListener() {
		if (null == singleClickRunnalbe) {
			singleClickRunnalbe = new Runnable() {
				@Override
				public void run() {
					if (null != onClickListener) {
						singleClickRunnalbe = null;
						onClickListener.onClick(BUZoomImageView.this);
					}
				}
			};
		}

		handler.postDelayed(singleClickRunnalbe, 1000);
		singleClickRunnalbeIsToHandler = true;
	}

	private void removeSingleClickListener() {
		if (singleClickRunnalbeIsToHandler) {
			handler.removeCallbacks(singleClickRunnalbe);
			singleClickRunnalbeIsToHandler = false;
		}
	}

	// 添加长按事件
	private void addLongClickListener() {
		if (null == longClickRunnalbe) {
			longClickRunnalbe = new Runnable() {
				@Override
				public void run() {
					if (null != onLongClickListener) {
						onLongClickListener.onLongClick(BUZoomImageView.this);
						longClickRunnalbeIsToHandler = false;
					}
				}
			};
		}
		handler.postDelayed(longClickRunnalbe, longClickTime);
		longClickRunnalbeIsToHandler = true;
	}

	// 移除长按事件
	private void removeLongClickListener() {
		if (longClickRunnalbeIsToHandler) {
			handler.removeCallbacks(longClickRunnalbe);
			longClickRunnalbeIsToHandler = false;
		}
	}

	// 判断是否移动
	public boolean checkMovce(float lastX, float lastY, float localX,
			float localY) {
		final int xDiff = (int) Math.abs(lastX - localX);
		if (xDiff > mTouchSlop) {
			return true;
		}

		final int yDiff = (int) Math.abs(lastY - localY);
		if (yDiff > mTouchSlop) {
			return true;
		}

		return false;
	}

	// ///////////////////////////////////////////设置放大的最大倍数//////////////////////////////////////////////////////
	public void setMaxScale(float maxScale) {
		this.mMaxScale = maxScale;
	}

	public void setLongClickTime(int longClickTime) {
		this.longClickTime = longClickTime;
	}

}
