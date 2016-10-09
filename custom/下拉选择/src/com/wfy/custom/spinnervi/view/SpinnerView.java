package com.wfy.custom.spinnervi.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfy.custom.spinner.R;

/**
 * 2016-8-19 上午10:29:41 创建 SpinnerView.java
 * 
 **/
public class SpinnerView extends RelativeLayout implements OnClickListener {

	/* popupwindown的高度 */
	private static final int POPUPWINDOWNHEIGHT = 200;
	private EditText edittext;
	private ImageView iv_select;
	private ListView listView;
	private ArrayList<String> arrayList;
	private MyAdapter adapter;
	private PopupWindow popup;

	public SpinnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SpinnerView(Context context) {
		super(context);
		init();
	}

	private void init() {
		initView();
		initListener();
		initData();
		initListView();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		View view = View.inflate(getContext(), R.layout.spinner_layout, this);
		edittext = (EditText) view.findViewById(R.id.edittext);
		iv_select = (ImageView) view.findViewById(R.id.iv_select);
	}

	/**
	 * 设置监听
	 */
	private void initListener() {
		iv_select.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		arrayList = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			arrayList.add(90000 + i + "");
		}
	}

	/**
	 * 初始化listview
	 */
	private void initListView() {
		listView = new ListView(getContext());
		adapter = new MyAdapter();
		listView.setBackgroundResource(R.drawable.listview_background);
		listView.setAdapter(adapter);
		listView.setVerticalScrollBarEnabled(false);// 取消listview的滚动条
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				edittext.setText(arrayList.get(position));
				popup.dismiss();// 关闭popup
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_select:
			/* 创建popupwindown */
			if (popup == null) {
				popup = new PopupWindow(listView, edittext.getWidth(),
						POPUPWINDOWNHEIGHT);
			}
			popup.setFocusable(true);// 如果popup中包含一个可获得焦点的view，那么popu的焦点将会被可获得焦点的view夺取焦点
			popup.setBackgroundDrawable(new ColorDrawable());// 如果需要执行动画必须得有背景
			popup.setOutsideTouchable(true);// 在外边点击，popup小时，， 必须有背景，不然就无效
			// 依附在edittext上
			popup.showAsDropDown(edittext, 0, 0);
			break;

		default:
			break;
		}
	}

	/**
	 * 适配器
	 * 
	 * @author wfy
	 * 
	 */
	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(getContext(), R.layout.item_popup,
						null);
				TextView text = (TextView) convertView.findViewById(R.id.text);
				ImageView iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);

				holder.iv_delete = iv_delete;
				holder.text = text;

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final View view = convertView;

			holder.text.setText(arrayList.get(position));

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					arrayList.remove(position);
					adapter.notifyDataSetChanged();
					if (arrayList.size() == 0) {
						popup.dismiss();
					}
					// 根据item的高度和数量，得到listview的高度，并且更新popup的高度
					int listViewHight = view.getHeight() * arrayList.size();
					popup.update(
							edittext.getWidth(),
							listViewHight > POPUPWINDOWNHEIGHT ? POPUPWINDOWNHEIGHT
									: listViewHight);
				}
			});
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView iv_delete;
		TextView text;
	}

}
