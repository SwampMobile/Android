package com.quanify.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip.IconTabProvider;
import com.quanify.R;
import com.quanify.app.DeviceList;
import com.quanify.fragments.CommentFragment;
import com.quanify.fragments.ConfigureFragment;
import com.quanify.fragments.SuperAwesomeCardFragment;
import com.quanify.models.DeviceRecord;

public class ShipmentConfigurationActivity extends BaseActivity {

	private final Handler handler = new Handler();
	private DeviceRecord deviceInfo;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	int position = 0;
	private int currentColor = 0xffb74d3f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_data);
		String deviceId = this.getIntent().getStringExtra("device_id");
        deviceInfo = DeviceList.getInstance().getDeviceById(deviceId);
        position = this.getIntent().getIntExtra("position", 0);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		changeColor(currentColor);
		pager.setCurrentItem(position);
	}

	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);
		currentColor = newColor;

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter implements IconTabProvider {

		private final String[] TITLES = { "Configuration","Comments" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
			case 0:
				return new ConfigureFragment();
			case 1:
				return new CommentFragment();
			}
			return SuperAwesomeCardFragment.newInstance(position);
		}

		@Override
		public int getPageIconResId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}


	}
	
	public DeviceRecord getDevice() {
		return deviceInfo;
	}
	

    @Override
    public void onBackPressed()
    {
    	super.onBackPressed();
    	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
