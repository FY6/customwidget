package com.wfy.youkumenu.utils;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * 菜单动画工具类
 * 
 * @author wfy
 * 
 */
public class AnimUtil {
	/**
	 * 通过动画监听，判断是否有正在执行的动画没有结束 如果等于0，则认为动画已经全部执行完毕
	 */
	public static int animCount = 0;

	/**
	 * 关闭菜单的动画
	 * 
	 * @param rl
	 * @param startOffset
	 *            延迟执行动画的时间
	 */
	public static void closeAnim(RelativeLayout rl, long startOffset) {
		/**
		 * 因为不是属性动画，当补间动画执行， 移动至别处，但是点击事件依然可以在原位置可触发。
		 * 所以当菜单是隐藏的，我们就应该把所有的，子view设置为不可点击
		 */
		for (int i = 0; i < rl.getChildCount(); i++) {
			rl.getChildAt(i).setEnabled(false);
		}

		RotateAnimation ra = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
		ra.setDuration(500);
		ra.setFillAfter(true);
		ra.setStartOffset(startOffset);// 延迟执行动画
		rl.startAnimation(ra);
		// 给动画设置监听，监听动画的状态
		ra.setAnimationListener(new MyAnimationListener());
	}

	/**
	 * 显示菜单的动画
	 * 
	 * @param rl
	 * @param startOffset
	 *            延迟执行动画的时间
	 */
	public static void showAnim(RelativeLayout rl, long startOffset) {
		/**
		 * 因为不是属性动画，当补间动画执行， 移动至别处，但是点击事件依然可以在原位置可触发。
		 * 所以当菜单是隐藏的，我们就应该把所有的，子view设置为不可点击
		 */
		for (int i = 0; i < rl.getChildCount(); i++) {
			rl.getChildAt(i).setEnabled(true);
		}

		RotateAnimation ra = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
		ra.setDuration(500);
		ra.setFillAfter(true);
		ra.setStartOffset(startOffset);
		rl.startAnimation(ra);
		// 给动画设置监听，监听动画的状态
		ra.setAnimationListener(new MyAnimationListener());
	}

	static class MyAnimationListener implements AnimationListener {

		// 动画开始执行时，调用此方法
		@Override
		public void onAnimationStart(Animation animation) {
			animCount++;
		}

		// 动画执行结束时，调用此方法
		@Override
		public void onAnimationEnd(Animation animation) {
			animCount--;
		}

		// 动画正在执行时，调用此方法
		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}
}
