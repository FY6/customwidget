package com.wfy.test.view2;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 2016-8-21 ÉÏÎç12:25:46 ´´½¨ ScrollAnimation.java
 * 
 **/
public class ScrollAnimation extends Animation {

	private int startScrollX;
	private int targetScrollX;
	private View v;
	private int totalValue;

	public ScrollAnimation(int targetScrollX, View v) {
		super();
		this.targetScrollX = targetScrollX;
		this.v = v;
		this.startScrollX = v.getScrollX();

		totalValue = targetScrollX - startScrollX;

		int time = Math.abs(totalValue);
		setDuration(time);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);

		int currentScrollX = (int) (this.startScrollX + this.targetScrollX
				* interpolatedTime);
		v.scrollTo(currentScrollX, 0);
	}
}
