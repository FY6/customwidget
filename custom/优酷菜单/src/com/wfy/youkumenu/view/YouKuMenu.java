package com.wfy.youkumenu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wfy.youkumenu.R;
import com.wfy.youkumenu.utils.AnimUtil;

/**
 * 
 * 思路： 观察发现，如果第一级隐藏，那么第一和第二是不会显示的，以此类推...
 * 
 * 2016-8-19 上午10:50:21 创建 YouKuMenu.java
 * 
 **/
public class YouKuMenu extends RelativeLayout implements OnClickListener {

	private RelativeLayout rl_level1;// 第一级菜单
	private RelativeLayout rl_level2;// 第二级菜单
	private RelativeLayout rl_level3;// 第三级菜单
	private ImageView icon_home;// home键
	private ImageView icon_menu;// 操蛋键

	// 记录菜单是否显示
	private boolean isShowlevel2 = true;
	private boolean isShowlevel3 = true;
	private boolean isShowMenu = true;
	private View view;// 把该布局文件转为ViewGroup，是因为获取子view比较容易
	private List<View> views;

	public YouKuMenu(Context context, AttributeSet attrs) {

		super(context, attrs);
		init();
	}

	public YouKuMenu(Context context) {

		super(context);
		init();
	}

	private void init() {
		initView();
		initListener();
	}

	/**
	 * 设置监听器
	 */
	private void initListener() {
		icon_home.setOnClickListener(this);
		icon_menu.setOnClickListener(this);

		// 为布局里面的子view设置监听
		for (int i = 0; i < views.size(); i++) {
			views.get(i).setOnClickListener(this);
		}
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		views = new ArrayList<View>();
		view = View.inflate(getContext(), R.layout.youku_menu_layout, this);
		rl_level1 = (RelativeLayout) view.findViewById(R.id.rl_level1);
		rl_level2 = (RelativeLayout) view.findViewById(R.id.rl_level2);
		rl_level3 = (RelativeLayout) view.findViewById(R.id.rl_level3);

		icon_home = (ImageView) view.findViewById(R.id.icon_home);
		icon_menu = (ImageView) view.findViewById(R.id.icon_menu);

		// 拿到所有的子view
		getChildViews();

		/*
		 * ImageView icon_search = (ImageView)
		 * view.findViewById(R.id.icon_search); views.add(icon_search);
		 * ImageView icon_myyouku = (ImageView) view
		 * .findViewById(R.id.icon_myyouku); views.add(icon_myyouku); ImageView
		 * channel1 = (ImageView) view.findViewById(R.id.channel1);
		 * views.add(channel1); ImageView channel2 = (ImageView)
		 * view.findViewById(R.id.channel2); views.add(channel2); ImageView
		 * channel3 = (ImageView) view.findViewById(R.id.channel3);
		 * views.add(channel3); ImageView channel4 = (ImageView)
		 * view.findViewById(R.id.channel4); views.add(channel4); ImageView
		 * channel5 = (ImageView) view.findViewById(R.id.channel5);
		 * views.add(channel5); ImageView channel6 = (ImageView)
		 * view.findViewById(R.id.channel6); views.add(channel6); ImageView
		 * channel7 = (ImageView) view.findViewById(R.id.channel7);
		 * views.add(channel7);
		 */
	}

	/**
	 * 拿到所有的子View并保存到集合中
	 * 
	 * 这样做就避免了多次调用findViewById()方法
	 */
	private void getChildViews() {
		for (int i = 0; i < rl_level3.getChildCount(); i++) {
			views.add(rl_level3.getChildAt(i));
		}

		for (int i = 0; i < rl_level2.getChildCount(); i++) {
			if (rl_level2.getChildAt(i).getId() != R.id.icon_menu) {
				views.add(rl_level2.getChildAt(i));
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (AnimUtil.animCount > 0) {
			return;
		}

		switch (v.getId()) {
		case R.id.icon_home:
			long startOffset = 0;
			if (isShowlevel2) {// 只有第二级显示的情况下，第三级才有可能是显示的状态
				if (isShowlevel3) {
					AnimUtil.closeAnim(rl_level3, startOffset);
					startOffset += 200;
					isShowlevel3 = !isShowlevel3;
				}
				AnimUtil.closeAnim(rl_level2, startOffset);
			} else {
				AnimUtil.showAnim(rl_level2, startOffset);
				startOffset += 200;
				AnimUtil.showAnim(rl_level3, startOffset);
				isShowlevel3 = !isShowlevel3;
			}
			isShowlevel2 = !isShowlevel2;
			break;
		case R.id.icon_menu:
			if (isShowlevel3) {
				AnimUtil.closeAnim(rl_level3, 0);
			} else {
				AnimUtil.showAnim(rl_level3, 0);
			}
			isShowlevel3 = !isShowlevel3;
			break;
		case R.id.channel7:
			if (listener != null) {
				listener.onChildViewClickChannel7();
			}
			break;
		case R.id.channel6:
			if (listener != null) {
				listener.onChildViewClickChannel6();
			}
			break;
		case R.id.channel5:
			if (listener != null) {
				listener.onChildViewClickChannel5();
			}
			break;
		case R.id.channel4:
			if (listener != null) {
				listener.onChildViewClickChannel4();
			}
			break;
		case R.id.channel3:
			if (listener != null) {
				listener.onChildViewClickChannel3();
			}
			break;
		case R.id.channel2:
			if (listener != null) {
				listener.onChildViewClickChannel2();
			}
			break;
		case R.id.channel1:
			if (listener != null) {
				listener.onChildViewClickChannel1();
			}
			break;
		case R.id.icon_search:
			if (listener != null) {
				listener.onChildViewClickSearch();
			}
			break;
		case R.id.icon_myyouku:
			if (listener != null) {
				listener.onChildViewClickMyyouku();
			}
			break;
		}

	}

	private OnChildViewIconClickListener listener;

	public void setOnChildViewIconClickListener(
			OnChildViewIconClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 该接口对应着布局文件中每一个icon的点击事件
	 * 
	 * @author wfy
	 * 
	 */
	public interface OnChildViewIconClickListener {
		void onChildViewClickChannel7();

		void onChildViewClickChannel6();

		void onChildViewClickChannel5();

		void onChildViewClickChannel4();

		void onChildViewClickChannel3();

		void onChildViewClickChannel2();

		void onChildViewClickChannel1();

		void onChildViewClickSearch();

		void onChildViewClickMyyouku();
	}
	
	/**
	 * 当按下物理键盘的menu键，根据各个菜单的状态，显示或隐藏菜单，这里重写该方法拦截menu键
	 * 
	 * 
	 * 为了能够在自定义view中调用onKeyDown方法，必须的让自定义view获取到焦点 在布局文件中
	 * android:focusableInTouchMode="true"
	 * 或者在代码中设置:setFocusableInTouchMode(true);
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(VIEW_LOG_TAG, "ooooooooo");
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			long startOffset = 0;
			if (isShowMenu) {
				if (isShowlevel3) {
					AnimUtil.closeAnim(rl_level3, startOffset);
					startOffset += 200;
					isShowlevel3 = !isShowlevel3;
				}
				if (isShowlevel2) {
					AnimUtil.closeAnim(rl_level2, startOffset);
					startOffset += 200;
					isShowlevel2 = !isShowlevel2;
				}
				AnimUtil.closeAnim(rl_level1, startOffset);
			} else {
				AnimUtil.showAnim(rl_level1, startOffset);
				startOffset += 200;
				AnimUtil.showAnim(rl_level2, startOffset);
				isShowlevel2 = !isShowlevel2;
				startOffset += 200;
				AnimUtil.showAnim(rl_level3, startOffset);
				isShowlevel3 = !isShowlevel3;
			}

			isShowMenu = !isShowMenu;

		}
		return super.onKeyDown(keyCode, event);
	}
}
