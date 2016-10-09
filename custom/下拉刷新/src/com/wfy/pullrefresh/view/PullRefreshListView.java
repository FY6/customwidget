package com.wfy.pullrefresh.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wfy.pullrefresh.R;

/**
 * 自定义ListView 触摸键挺 滚动监听
 * 自定义监听器，暴露下拉刷新监听接口，让用户知道什么时候ListView下拉监听，如果用户没有设置监听，当用户下拉时，跑出没有 设置监听事件异常
 * 
 * @author wfy
 * 
 */
public class PullRefreshListView extends ListView implements OnScrollListener {

	private static final String TAG = "PullRefreshListView";
	private int downY; // 按下时的Y坐标
	private View headerView;// headerView
	private int headerViewHeight;// headerView高
	private View footerView;
	private int footerViewHeight;

	/**
	 * 定义下拉刷新状态
	 * 
	 * @param context
	 */

	private final int PULL_REFRESH = 0;// 下拉刷新状态
	private final int RELEASE_REFRESH = 1;// 松开刷新状态
	private final int REFRESHING = 2;// 正在刷新

	private int cuurentState = PULL_REFRESH;// 默认状态为下拉刷新

	private ProgressBar indicate_rotate;
	private ImageView indicator_arrow;
	private TextView belowtext, toptext;

	private RotateAnimation upAnimation, downAnimation;

	private boolean isLoadingMore = false;// 当前是否正在处于加载更多

	// 代码创建控件
	public PullRefreshListView(Context context) {
		super(context);
		init();
	}

	// xml中使用
	public PullRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * headerView.getMeasuredHeight()和getHeight()的区别：
	 * getHeight():必须要onLayout方法执行完成之后，才能拿到值
	 * getMeasuredHeight():当onMeasure方法执行之后就能获取到值 调用headerView.measure(0,
	 * 0)系统会自动去执行onMeasure
	 */
	private void init() {
		setOnScrollListener(this);// 一定要设置滚动监听
		initHeaderView();
		initAnimation();
		initFooterView();
	}

	/**
	 * 初始化headerView
	 */
	private void initHeaderView() {
		// 填充lisyview头布局
		headerView = inflate(getContext(), R.layout.listview_header, null);
		initView();
		headerView.measure(0, 0);
		headerViewHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		this.addHeaderView(headerView);
	}

	private void initFooterView() {
		footerView = View.inflate(getContext(), R.layout.listview_footer, null);
		footerView.measure(0, 0);
		footerViewHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		addFooterView(footerView);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);

		downAnimation = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		downAnimation.setDuration(300);
		downAnimation.setFillAfter(true);
	}

	/**
	 * 初始化headerView布局的子view
	 */
	private void initView() {
		indicate_rotate = (ProgressBar) headerView
				.findViewById(R.id.indicate_rotate);
		indicator_arrow = (ImageView) headerView
				.findViewById(R.id.indicator_arrow);
		toptext = (TextView) headerView.findViewById(R.id.toptext);
		belowtext = (TextView) headerView.findViewById(R.id.belowtext);
	}

	/**
	 * 触摸监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_UP:

			// 根据当前状态，判断是否更新:
			// cuurentState == RELEASE_REFRESH --- 需要刷新 ，则状态立刻转入刷新状态cuurentState
			// = REFRESHING;
			// 调用refreshHeaderView();更新UI
			// cuurentState == PULL_REFRESH --- 不需要，把headerView设置 隐藏

			if (cuurentState == PULL_REFRESH) {
				headerView.setPadding(0, -headerViewHeight, 0, 0);
			} else if (cuurentState == RELEASE_REFRESH) {
				headerView.setPadding(0, 0, 0, 0);
				cuurentState = REFRESHING;
				refreshHeaderView();
				/**
				 * 下拉刷新，执行用户请求获取服务器数据的代码
				 */
				if (listener != null) {
					listener.pullRefresh();
				} else {
					throw new RuntimeException(
							"没有设置下拉刷新监听，请实现OnRefreshListener接口");
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:

			if (cuurentState == REFRESHING) {
				break;
			}

			int deltaY = (int) (ev.getY() - downY);
			int paddingTop = -headerViewHeight + deltaY;
			if (paddingTop > -headerViewHeight
					&& getFirstVisiblePosition() == 0) {
				headerView.setPadding(0, paddingTop, 0, 0);

				// 1、当-headerViewHeight + deltaY < 0时----说明paddingTop <
				// 0;（headerView隐藏）下拉刷新状态 PULL_REFRESH
				// 2、当-headerViewHeight + deltaY = 0时----说明paddingTop =
				// 0；（刚好显示完成） 临界刷新状态REFRESHING
				// 3、当-headerViewHeight + deltaY > 0时----说明paddingTop >
				// 0;（完全显示）松开刷新状态 RELEASE_REFRESH

				if (paddingTop >= 0 && cuurentState == PULL_REFRESH) {
					// 从拉刷新状态 -----> 松开刷新状态
					cuurentState = RELEASE_REFRESH;
					refreshHeaderView();
				} else if (paddingTop < 0 && cuurentState == RELEASE_REFRESH) {
					// 从松开刷新状态-----> 下拉刷新状态
					cuurentState = PULL_REFRESH;
					refreshHeaderView();
				}

				return true;// 拦截TouchMove不让listView处理盖茨Move事件，造成listview无法滑动
				// 避免双重处理，造成移动速度过快，滑动过程中跳跃现象的情况。
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 更新headerView
	 */
	private void refreshHeaderView() {
		switch (cuurentState) {
		case PULL_REFRESH:
			toptext.setText("下拉刷新...");
			indicator_arrow.startAnimation(downAnimation);
			break;
		case RELEASE_REFRESH:
			toptext.setText("松开刷新...");
			indicator_arrow.startAnimation(upAnimation);
			break;
		case REFRESHING:

			indicator_arrow.clearAnimation();
			indicator_arrow.setVisibility(View.INVISIBLE);
			indicate_rotate.setVisibility(View.VISIBLE);
			toptext.setText("正在刷新...");
			break;
		}
	}

	/**
	 * 完成刷新操作，重置状态,在你获取完数据并更新adapter之后，去UI线程调用该方法
	 */
	public void completeRefresh() {
		if (isLoadingMore) {
			footerView.setPadding(0, -footerViewHeight, 0, 0);
			isLoadingMore = false;
		} else {
			headerView.setPadding(0, -headerViewHeight, 0, 0);
			cuurentState = PULL_REFRESH;
			indicate_rotate.setVisibility(View.INVISIBLE);
			indicator_arrow.setVisibility(View.VISIBLE);
			toptext.setText("下拉刷新...");

			belowtext.setText("最后刷新时间:" + getCurrentTime());
		}
	}

	/**
	 * 因为用户不知道此时状态是什么，，是下拉刷新状态，还是加载更多，所以我们要暴露一个接口，当对应状态下要做什么样操作。
	 * 
	 * 下拉刷新，就是要求情数据
	 */
	private OnRefreshListener listener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	/**
	 * 用户必须实现该接口，去做获取数据的操作
	 * 
	 * @author wfy
	 * 
	 */
	public interface OnRefreshListener {
		void pullRefresh();

		void loadingMore();
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		return dateFormat.format(new Date());
	}

	/**
	 * SCROLL_STATE_IDLE = 0 闲置状态 
	 * SCROLL_STATE_TOUCH_SCROLL = 1 滚动状态
	 * SCROLL_STATE_FLING = 2 惯性滚动状态
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		 Log.e(TAG, scrollState + "------");

		if (OnScrollListener.SCROLL_STATE_IDLE == scrollState
				&& getLastVisiblePosition() == (getCount() - 1)) {
			isLoadingMore = true;

			footerView.setPadding(0, 0, 0, 0);// 显示出footerView
			// setSelection(getCount());// 让listview最后一条显示出来

			if (listener != null) {
				listener.loadingMore();
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
}
