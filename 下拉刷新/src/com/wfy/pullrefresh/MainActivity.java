package com.wfy.pullrefresh;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wfy.pullrefresh.view.PullRefreshListView;
import com.wfy.pullrefresh.view.PullRefreshListView.OnRefreshListener;

/**
 * 自定义ListView
 * 
 * @author wfy
 * 
 */
public class MainActivity extends Activity {

	private PullRefreshListView pullRefreshListView;
	private ArrayList<String> lists = new ArrayList<String>();
	private MyAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapter.notifyDataSetChanged();
			pullRefreshListView.completeRefresh();// 完成下拉刷新，更新listviewUI
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置当前activity为无title
		setContentView(R.layout.activity_main);
		pullRefreshListView = (PullRefreshListView) findViewById(R.id.pullrefreshlistview);

		// 设置下拉刷新，求情数据，当listview下拉时，回调 就请求数据，，，啥时候调用，只有listview知道
		pullRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void pullRefresh() {
				requestDataFromServer(false);
			}

			@Override
			public void loadingMore() {
				requestDataFromServer(true);
			}
		});
	}

	/**
	 * 模拟请求服务器数据
	 */
	private void requestDataFromServer(final boolean isLoadingMore) {
		new Thread() {
			public void run() {
				SystemClock.sleep(3000);
				if (isLoadingMore) {
					lists.add("加载更多的数据-1");
					lists.add("加载更多的数据-2");
					lists.add("加载更多的数据-3");
				} else {
					lists.add(0, "下拉刷新,卧槽，这么牛X?");
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		for (int i = 0; i < 30; i++) {
			lists.add("我曹，好牛逼啊!" + i);
		}
		if (adapter == null) {
			adapter = new MyAdapter();
			pullRefreshListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = new TextView(MainActivity.this);
			textView.setTextSize(16);
			textView.setPadding(16, 16, 16, 16);
			textView.setText(lists.get(position));
			return textView;
		}

	}
}
