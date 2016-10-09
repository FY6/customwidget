package com.wfy.youkumenu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.wfy.youkumenu.view.BaseChildViewIconClickListener;
import com.wfy.youkumenu.view.YouKuMenu;
import com.wfy.youkumenu.view.YouKuMenu.OnChildViewIconClickListener;

/**
 * 
 * 
 * @author wfy
 * 
 */
public class MainActivity extends Activity {
	private YouKuMenu youku;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		youku = (YouKuMenu) findViewById(R.id.youkumwnu);

		/**
		 * 实现对应接口，根据id处理代码逻辑
		 */
		youku.setOnChildViewIconClickListener(new BaseChildViewIconClickListener() {

			@Override
			public void onChildViewClickSearch() {
				Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickMyyouku() {
				Toast.makeText(MainActivity.this, "youku", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel7() {
				Toast.makeText(MainActivity.this, "c7", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel6() {
				Toast.makeText(MainActivity.this, "c6", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel5() {
				Toast.makeText(MainActivity.this, "c5", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel4() {
				Toast.makeText(MainActivity.this, "c4", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel3() {
				Toast.makeText(MainActivity.this, "c3", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel2() {
				Toast.makeText(MainActivity.this, "c2", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onChildViewClickChannel1() {
				Toast.makeText(MainActivity.this, "c1", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
