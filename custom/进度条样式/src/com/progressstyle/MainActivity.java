package com.progressstyle;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ProgressBar;

/**
 * 自定义进度条样式
 * 
 * @author wfy
 * 
 */
public class MainActivity extends Activity {

	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pb = (ProgressBar) findViewById(R.id.pb);

		pb.setMax(100);

		new Thread() {
			public void run() {

				for (int i = 0; i <= 100; i++) {
					SystemClock.sleep(300);
					pb.setProgress(i);
				}
			}
		}.start();
	}
}
