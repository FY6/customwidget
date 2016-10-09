package com.wfy.slidemenu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.wfy.slidemenu.R;

/**
 * 2016-8-19 上午3:24:06 创建 SlideMenu.java
 * 
 **/
public class SlideMenu extends ViewGroup/* FrameLayout */implements
		OnClickListener {
	/**
	 * 存放SlideMenu的子view
	 */
	private List<View> childViews;
	private Context context;
	private Scroller scroller;
	private boolean switchState = false;

	/**
	 * 用于在代码中动态创建
	 * 
	 * @param context
	 */
	public SlideMenu(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * 用于在xml布局文件中调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		childViews = new ArrayList<View>();
		scroller = new Scroller(context);// 看一下插补器
	}

	/**
	 * 当所有一级view完成加载后调用此方法，可以用来初始化子view的引用
	 * 
	 * 注意：这里无法获取子view的宽高
	 */
	@Override
	protected void onFinishInflate() {
		for (int i = 0; i < getChildCount(); i++) {
			childViews.add(getChildAt(i));
		}
		menuWidth = childViews.get(0).getLayoutParams().width;
		initVListener();
	}

	/**
	 * 
	 */
	private void initVListener() {
		menuSwitch = (ImageView) findViewById(R.id.menuSwitch);
		menuSwitch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		menuSwitch();
	}

	private void menuSwitch() {
		// Log.e(VIEW_LOG_TAG, "switchState: " + switchState);
		if (switchState) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	/**
	 * 在自定义ViewGroup中一般不需要去实现onMeasure, 我们去实现系统已有的ViewGroup,比如FrameLayout,
	 * RelativeLayout.....因为他们已经实现好了onMeasure方法，我们只要重写onLayout方法，对我们的子view
	 * 进行布局就行了
	 * 
	 * 它会帮我们区实现onMeasure方法，我们只需要重写onLayout方法，定义我们的子view如何摆放,并且我们可以通过
	 * onLayout方法修改系统原有的ViewGroup如FrameLayout的布局方式。
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量所有字view的宽高
		View menuView = childViews.get(0);
		// 通过getLayoutParams()方法可以获取到布局文件中的宽高
		menuView.measure(menuView.getLayoutParams().width, heightMeasureSpec);
		View mainView = childViews.get(1);
		// 直接使用SlideMenu的宽高，因为它的宽高都是充满父窗体
		mainView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 为自己的子view进行布局，摆放子view
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View menuView = childViews.get(0);
		menuView.layout(-menuView.getLayoutParams().width, 0, 0, b);
		View mainView = childViews.get(1);
		mainView.layout(l, t, r, b);
	}

	private int downX;
	private int menuWidth;
	private ImageView menuSwitch;

	/**
	 * 设置触摸监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int deltaX = moveX - downX;
			int newScrllX = getScrollX() - deltaX;

			if (newScrllX < -menuWidth) {
				newScrllX = -menuWidth;
			}
			if (newScrllX > 0) {
				newScrllX = 0;
			}
			scrollTo(newScrllX, 0);
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			// 自定义动画
			// ScrollerAnimation scrollerAnimation;
			// if (getScrollX() > -menuWidth / 2) {
			// // 关闭菜单
			// // scrollTo(0, 0);
			// scrollerAnimation = new ScrollerAnimation(0, this);
			// } else {
			// // 打开菜单
			// // scrollTo(-menuWidth, 0);
			// scrollerAnimation = new ScrollerAnimation(-menuWidth, this);
			// }
			// startAnimation(scrollerAnimation);

			// 2,Scroller

			if (getScrollX() > -menuWidth / 2) {
				// Log.e(VIEW_LOG_TAG, getScrollX() + "----" + -menuWidth / 2);
				// 关闭菜单
				// dx 为正数，那么内容将滚动去左边
				closeMenu();
			} else {
				// 打开菜单
				// Log.e(VIEW_LOG_TAG, getScrollX() + "====" + -menuWidth / 2);
				openMenu();
			}
			break;
		}
		return true;
	}

	/**
	 * 拦截TouchEvent:
	 * 
	 * dispatchTouchEvent:分发事件，如果onInterceptTouchEvent返回true，就直接自己处理（
	 * onTouchEvent） 否则将事件分发给子view
	 * 
	 * onInterceptTouchEvent:拦截事件，返回true，表示拦截，返回， false表示不拦截
	 * 
	 * onTouchEvent:处理事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int deltaX = moveX - downX;

			if (Math.abs(deltaX) > 8) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * Scroller不主动去调用这个方法
	 * 
	 * Scroller:就是一个模拟的过程，但是我们可以拿到，它模拟过程中的x和y的值，从而我们可以滚动我们的view
	 * 
	 * 而invalidate()可以掉这个方法 invalidate->draw->computeScroll
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {// 返回true,表示动画没结束
			int currX = scroller.getCurrX();
			scrollTo(currX, 0);
			invalidate();
		}
	}

	/**
	 * 关闭菜单
	 */
	private void closeMenu() {
		switchState = !switchState;
		scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 500);
		invalidate();
	}

	/**
	 * 打开菜单
	 */
	private void openMenu() {
		switchState = !switchState;
		scroller.startScroll(getScrollX(), 0, -menuWidth - getScrollX(), 0, 500);
		invalidate();
	}
}
