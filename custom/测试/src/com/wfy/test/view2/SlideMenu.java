package com.wfy.test.view2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.wfy.test.R;

/**
 * 2016-8-19 上午3:24:06
 * 
 * 创建 SlideMenu.java
 * 
 **/
public class SlideMenu extends /* ViewGroup */FrameLayout {

	private Context context;

	public SlideMenu(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void init() {
		// 返回的是root != null,则返回root，否则返回view
		View.inflate(context, R.layout.slide_menu, this);
		View.inflate(context, R.layout.main_layout, this);

		slideMenu = this.findViewById(R.id.slide);
		mainMenu = this.findViewById(R.id.main);

		menuWidth = slideMenu.getLayoutParams().width;

	}

	/**
	 * 为自己的子view进行布局，摆放子view
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		slideMenu.layout(-menuWidth, 0, 0, b);
		mainMenu.layout(l, t, r, b);
	}

	private int downX;
	private int menuWidth;
	private View slideMenu;
	private View mainMenu;

	/**
	 * 触摸监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// mDetector.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int deltaX = moveX - downX;
			int newScrllX = getScrollX() - deltaX; // Log.e("hahaha",
													// newScrllX+"");

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
			// ScrollAnimation mScrollAnimation;
			if (getScrollX() > -menuWidth / 2) {
				scrollTo(-menuWidth, 0);
				// 显示菜单
				// mScrollAnimation = new ScrollAnimation(0, this);
			} else {
				scrollTo(0, 0);
				// 关闭菜单
				// mScrollAnimation = new ScrollAnimation(-menuWidth, this);
			}
			// startAnimation(mScrollAnimation);
			break;
		}

		return true;
	}
}
