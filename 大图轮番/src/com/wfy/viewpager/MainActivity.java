package com.wfy.viewpager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.wfy.viewpager.domain.AD;

/**
 * ViewPager 大图轮番
 * 
 * @author wfy
 * 
 */

public class MainActivity extends Activity {

	private ViewPager viewpager;
	private TextView tv_intro;
	private LinearLayout dot_layout;
	private List<AD> ads = new ArrayList<AD>();
	protected final String tag = "MainActivity";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int currentItem = viewpager.getCurrentItem();
			viewpager.setCurrentItem(currentItem + 1);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();

		// 定时切换
		tiemrTrasition();
	}

	/**
	 * 定时切换
	 */
	private void tiemrTrasition() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 5000, 5000);
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		dot_layout = (LinearLayout) findViewById(R.id.dot_layout);
	}

	private void initListener() {
		viewpager.addOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * This method will be invoked when a new page becomes selected
			 * 当一个新的页面变为选中时，调用此方法
			 */
			@Override
			public void onPageSelected(int position) {
				updateIntroAndDot();
			}

			/**
			 * This method will be invoked when the current page is scrolled
			 * 如果当前页面正在滚动，将不断调用此方法
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			/**
			 * Called when the scroll state changes，begins dragging, when the
			 * pager is automatically settling to the current page, or when it
			 * is fully stopped/idle. 当滚动状态发生改变时， 调用此方法 三种状态： SCROLL_STATE_IDLE
			 * = 0 : 闲置 SCROLL_STATE_DRAGGING = 1 : 正在拖拽 SCROLL_STATE_SETTLING =
			 * 2 : 固定
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	/**
	 * 更新文本
	 */
	protected void updateIntroAndDot() {
		int currentItem = viewpager.getCurrentItem() % ads.size();
		AD ad = ads.get(currentItem);
		tv_intro.setText(ad.getIntro());

		for (int i = 0; i < dot_layout.getChildCount(); i++) {
			dot_layout.getChildAt(i).setEnabled(currentItem == i);
		}
	}

	private void initData() {

		ads.add(new AD(R.drawable.a, "巩俐不低俗，我不能低俗!"));
		ads.add(new AD(R.drawable.b, "朴树又回来了，再唱经典老歌引万人同唱啊!"));
		ads.add(new AD(R.drawable.c, "揭秘北京电影如何升级?"));
		ads.add(new AD(R.drawable.d, "乐视网TV版大派送."));
		ads.add(new AD(R.drawable.e, "热血屌丝的大反杀?"));

		initDotLayotu();
		viewpager.setAdapter(new MyPageAdapter());

		int centerValue = Integer.MAX_VALUE / 2;
		int value = centerValue % ads.size();
		viewpager.setCurrentItem(centerValue - value);

		updateIntroAndDot();
	}

	/**
	 * 更新dot
	 */
	private void initDotLayotu() {
		for (int i = 0; i < ads.size(); i++) {
			View view = new View(this);
			LinearLayout.LayoutParams params = new LayoutParams(8, 8);

			if (i != 0) {
				params.leftMargin = 5;
			}
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.dot_selector);

			dot_layout.addView(view);
		}

	}

	class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * 
		 * 返回 true 从缓存中拿，不创建view， 返回 false 则需要调用instantiateItem方法创建view
		 * view:当前拖动的view object:正在进入的view
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		/**
		 * 
		 * 
		 * 类此BaseAdapter中的getView方法 container:viewpager position:创建第几个page
		 * 用于创建子view，并把子view添加到viewpager中
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(MainActivity.this, R.layout.adapte_ad,
					null);
			AD ad = ads.get(position % ads.size());
			ImageView image = (ImageView) view.findViewById(R.id.image);
			image.setImageResource(ad.getResID());

			container.addView(view);// 把子view添加到viewpager中
			return view;
		}

		/**
		 * position:当前销毁第几个page object:当前消耗的page
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

	}
}
