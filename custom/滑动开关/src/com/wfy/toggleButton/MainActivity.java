package com.wfy.toggleButton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.wfy.toggleButton.view.ToggleButton;
import com.wfy.toggleButton.view.ToggleButton.OnToggleStateChangeListener;
import com.wfy.toggleButton.view.ToggleButton.ToggleState;

public class MainActivity extends Activity {

	private ToggleButton mToggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);

		mToggleButton.setSlideBackgroundResource(R.drawable.slide_button);
		mToggleButton.setSwitchBackgroundResource(R.drawable.switch_background);
		// mToggleButton.setSwitchState(ToggleState.CLOSE);

		mToggleButton
				.setonToggleStateChangeListener(new OnToggleStateChangeListener() {

					@Override
					public void onToggleStateChange(ToggleState state) {
						Toast.makeText(MainActivity.this,
								state == ToggleState.OPEN ? "开关开启" : "开关关闭",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
}
