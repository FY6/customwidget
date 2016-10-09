package com.wfy.slidemenu;

import com.wfy.slidemenu.view.SlideMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private SlideMenu mSlidemwnu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSlidemwnu = (SlideMenu) findViewById(R.id.slidemwnu);
	}
}
