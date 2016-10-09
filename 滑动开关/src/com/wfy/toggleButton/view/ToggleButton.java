package com.wfy.toggleButton.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * 滑动开关 2016-8-17 下午10:58:28 创建 ToggleButton.java
 * 
 **/
public class ToggleButton extends View {
	private ToggleState mToggleState = ToggleState.OPEN;
	private Bitmap mSlideBackground;
	private Bitmap mSwitchBackground;

	private int currentX;
	private boolean isSliding = false;// 是否正在滑动

	/**
	 * 如果你的view只在布局文件中使用，那就只是重写该构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public ToggleButton(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	/**
	 * 如果你的view需要在代码中动态创建的，那就要重写该构造方法
	 * 
	 * @param context
	 */
	public ToggleButton(Context context) {

		super(context);
	}

	/**
	 * 
	 */
	public enum ToggleState {
		OPEN, CLOSE
	}

	/**
	 * 设置滑块的背景图片
	 * 
	 * @param slideButton
	 */
	public void setSlideBackgroundResource(int slideButtonID) {
		mSlideBackground = BitmapFactory.decodeResource(getResources(),
				slideButtonID);
	}

	/**
	 * 设置开关背景图片
	 * 
	 * @param switchBackground
	 */
	public void setSwitchBackgroundResource(int switchBackgroundID) {
		mSwitchBackground = BitmapFactory.decodeResource(getResources(),
				switchBackgroundID);
	}

	/**
	 * 设置开关当前状态
	 * 
	 * @param open
	 */
	public void setSwitchState(ToggleState state) {
		mToggleState = state;
	}

	/**
	 * 设置当前view在父布局中的 宽和高
	 * 
	 * 如果我们重写这个方法，那么测量宽和高的就是子类的职责，至少是最小的宽和高。
	 * 
	 * @param widthMeasureSpec
	 *            依附在父view上的水平控件
	 * @param heightMeasureSpec
	 *            依附在父view上的垂直空间
	 * 
	 *            setMeasuredDimension(int measuredWidth, int measuredHeight)
	 *            方法必须在onMeasure(int, int)方法中调用该方法去保存测量的高和宽
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension(mSwitchBackground.getWidth(),
				mSwitchBackground.getWidth());
	}

	/**
	 * 画出当前view的内容
	 * 
	 * 每一个view都有自己的坐标系
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 在当前view的坐标系画出当前view的内容；
		// left即图片在当前view坐标系的坐标x，top就是当前view坐标系的坐标y
		canvas.drawBitmap(mSwitchBackground, 0, 0, null);

		// 绘制滑块
		if (isSliding) {
			int left = currentX - mSlideBackground.getWidth() / 2;
			if (left < 0) {
				left = 0;
			}
			int slideAndSwitchWidth = mSwitchBackground.getWidth()
					- mSlideBackground.getWidth();
			if (left > slideAndSwitchWidth) {
				left = slideAndSwitchWidth;
			}
			canvas.drawBitmap(mSlideBackground, left, 0, null);
		} else {
			if (mToggleState == ToggleState.OPEN) {
				// 开关处于开状态
				canvas.drawBitmap(
						mSlideBackground,
						mSwitchBackground.getWidth()
								- mSlideBackground.getWidth(), 0, null);
			} else {
				// 开关处于关闭状态
				canvas.drawBitmap(mSlideBackground, 0, 0, null);
			}
		}

	}

	/**
	 * 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		currentX = (int) event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isSliding = true;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			isSliding = false;
			int centerX = mSwitchBackground.getWidth() / 2;
			if (currentX > centerX) {
				// open
				// 开关必须是从另一个状态切换到另一个状态
				if (mToggleState != ToggleState.OPEN) {
					mToggleState = ToggleState.OPEN;
					// 只有当状态改变才回调，通知用户
					if (listener != null) {
						listener.onToggleStateChange(mToggleState);
					}
				}
			} else {
				// close
				// 开关必须是从另一个状态切换到另一个状态
				if (mToggleState != ToggleState.CLOSE) {
					mToggleState = ToggleState.CLOSE;
					// 只有当状态改变才回调，通知用户
					if (listener != null) {
						listener.onToggleStateChange(mToggleState);
					}
				}
			}
			break;
		}
		/**
		 * 该方法会导致，重调onDraw方法
		 */
		invalidate();
		return true;
	}

	/**
	 * 暴露接口，让用户知道，当前开关的状态
	 */
	private OnToggleStateChangeListener listener;

	public void setonToggleStateChangeListener(
			OnToggleStateChangeListener listener) {
		this.listener = listener;
	}

	public interface OnToggleStateChangeListener {
		void onToggleStateChange(ToggleState state);
	}

}
