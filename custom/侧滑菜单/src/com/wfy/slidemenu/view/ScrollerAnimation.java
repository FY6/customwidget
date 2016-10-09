package com.wfy.slidemenu.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 2016-8-20 下午11:03:27 创建 ScrollerAnimation.java
 * 
 * 滚动动画: 让指定view在一段时间内scrollTo到指定位置
 * 
 **/
public class ScrollerAnimation extends Animation {

	private int startScrollX;
	private int targetScrollX;
	private View view;
	private int totalValue;

	public ScrollerAnimation(int targetScrollX, View view) {
		super();
		this.targetScrollX = targetScrollX;
		this.view = view;

		// 开始的位置就是,已经滚动的距离
		this.startScrollX = view.getScrollX();
		// 需要滚动多少距离
		this.totalValue = this.targetScrollX - startScrollX;

		// 因为时间有可能是 负值
		int time = Math.abs(totalValue);
		// 设置动画执行的时间
		setDuration(time * 2);
	}

	/**
	 * 在指定的时间内一直执行该方法，直到动画结束
	 * 
	 * interpolatedTime：0-1 标识动画执行的进度或者百分比， 根据该值可以，精确的在指定时间内，让动画和滚动一起结束
	 * 
	 * 当前的值 = 起始值 + 总的差值*interpolatedTime
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);

		/**
		 * 如：startScrollX =10 totalValue：50 interpolatedTime 0.5，即动画已经执行到一半，
		 * 那么currentScrollX =
		 * startScrollX+totalValue*interpolatedTime=10+50*0.5=35
		 */
		int currentScrollX = (int) (startScrollX + totalValue
				* interpolatedTime);
		view.scrollTo(currentScrollX, 0);
	}
}
